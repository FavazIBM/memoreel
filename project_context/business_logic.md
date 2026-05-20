# Business Logic & Domain Knowledge

---

## 1. Domain Overview

**Memoreel** is a **memory reel sharing platform** where users create emotionally rich video reels for special occasions and memorials using uploaded media and predefined templates.

The system automates:

* Media organization and sequencing
* Template-based video generation using FFmpeg
* Public sharing via unique links and QR codes
* Memorial creation with tribute features
* Project state management

**Source of Truth Alignment**:

* This document defines domain rules and business intent
* [`architecture.md`](project_context/architecture.md) defines system structure, state flow, and product behavior
* [`api_documentation.md`](project_context/api_documentation.md) defines request/response contracts and operational API rules
* If any conflict exists, [`architecture.md`](project_context/architecture.md) and [`api_documentation.md`](project_context/api_documentation.md) take precedence over this document

---

## 2. Canonical Enums & Reference Values

### Project Status

* `draft`
* `processing`
* `completed`
* `published`
* `failed`

### Video Job Status

* `queued`
* `processing`
* `completed`
* `failed`

### Privacy Values

* `public`
* `friends`
* `private`

**Phase 1 Rule**:

* Only `public` is effectively available for published reels in the current implementation
* `friends` and `private` are future-facing values and are not fully enforced in Phase 1
* Privacy may still be stored in the database and shown in the UI
* Non-public values may appear disabled in the UI until future implementation is enabled

### Occasion Categories

**Celebration**:
* `birthday`
* `anniversary`
* `wedding`
* `graduation`
* `baby_shower`
* `housewarming`
* `retirement`

**Social**:
* `farewell`
* `trip_memory`
* `friendship`
* `reunion`
* `milestone`
* `achievement`

**Emotional (Memorial)**:
* `memorial`
* `condolence`
* `remembrance_day`

**Custom**:
* `custom`

---

## 3. Core Business Concepts

### Project

**Definition**: A container representing a memory video.

**Purpose**: Central unit for storing media, text, generated video, and occasion-specific metadata.

**Canonical Fields**:

* `id`
* `userId`
* `title`
* `type`
* `status`
* `privacy`
* `createdAt`
* `updatedAt`
* `publishedAt`
* `metadata`

**Rules**:

* Each project belongs to one user
* A project must have a title and occasion type
* A project must follow a strict lifecycle
* Occasion-specific details are stored inside `metadata`
* Memorials are modeled as projects using memorial-related occasion types
* A project is always owned by the authenticated user who created it

---

### Media

**Definition**: Images or videos uploaded by user.

**Purpose**: Input for video generation.

**Rules**:

* Must belong to a project
* Must have [`orderIndex`](project_context/business_logic.md)
* Must be validated for type, size, and MIME type
* Sequence MUST be preserved
* Media ordering directly affects final reel sequencing
* Media can be reordered only while project editing is allowed

---

### Video

**Definition**: Generated output of a project.

**Purpose**: Final consumable content.

**Rules**:

* Generated asynchronously
* Must follow template rules
* Only one active generated video per project
* Output format is MP4
* Output quality is 1080p
* Video is stored permanently in S3
* Regeneration replaces the previous generated video for the project

---

### Video Job

**Definition**: Queue-managed background job for video generation.

**Purpose**: Track asynchronous rendering and processing.

**Canonical Fields**:

* `id`
* `projectId`
* `status`
* `startedAt`
* `completedAt`

**Rules**:

* Only one generation job per project can exist at a time
* Jobs are processed asynchronously
* Failure must update the related project to `failed`
* Retry behavior follows the system queue rules defined in [`architecture.md`](project_context/architecture.md)

---

### Template

**Definition**: Predefined styling and structure for video generation.

**Purpose**: Ensure consistent output quality.

**Rules**:

* Selected based on occasion + category + mood
* Fallback may use category
* NO randomness allowed
* Template selection must be deterministic
* Templates define transitions, duration, music, and text styling

---

### Memorial

**Definition**: A memorial-oriented project represented through memorial-related occasion types and memorial-specific metadata.

**Purpose**: Create permanent digital memorials that can be shared physically and digitally.

**Modeling Rule**:

* Memorial is not a separate root entity from project in the current product model
* Memorial behavior is driven by project `type` and its `metadata`

**Required Fields**:

* `personName`
* `deathDate`
* `description`

**Optional Fields**:

* `birthDate`
* `profileImage`
* `relationship`
* `lifeStory`

**Rules**:

* Uses calm, respectful visual templates
* May use soft background music
* Automatically generates QR code on publish
* QR code can be printed for physical placement
* Memorial public URL is permanent after publish
* Memorial display includes tribute details and uploaded media
* Privacy is stored at project level, but Phase 1 public behavior still applies

---

### Public Link

**Definition**: Permanent public URL created when a completed project is published.

**Purpose**: Provide direct access to a published reel or memorial.

**Rules**:

* Generated at publish time
* Must be unique across all projects
* Must use URL-safe characters only
* Format is random alphanumeric, 8-12 characters
* Example format: `memoreel.app/m/abc123xyz`
* Public link never changes after creation

---

### QR Code

**Definition**: Scannable image that links directly to a published memorial or reel.

**Purpose**: Enable physical-digital integration and simplified sharing.

**Rules**:

* Generated automatically when a project is published
* Must be permanent and continue pointing to the same public URL
* Must map to a unique public URL
* Format: PNG image
* Size: 512x512px
* High error correction level for reliability
* Downloadable in high resolution for printing
* Can be scanned from mobile devices
* Intended to link directly to the memorial or reel page

**Use Cases**:
* Scanned at funeral services
* Engraved on tombstones
* Printed on memorial cards
* Displayed at funeral services
* Included in obituaries
* Shared digitally via social media

---

## 4. Business Rules

### Project Rules

* Projects start in `draft` state upon creation
* Project ownership is immutable
* Cannot publish before video generation completes
* Only completed projects can be published
* Published projects cannot be unpublished
* Draft projects can be edited
* Processing projects cannot be modified
* Failed projects can be regenerated
* Generation starts from `draft` or `failed` according to API behavior
* Completed projects may be regenerated through the explicit regeneration flow
* Regeneration replaces the previous generated video
* Published-project regeneration must not be assumed unless explicitly added to [`architecture.md`](project_context/architecture.md) and [`api_documentation.md`](project_context/api_documentation.md)

---

### Media Rules

* Media must be uploaded before video generation
* [`orderIndex`](project_context/business_logic.md) must be unique per project
* [`orderIndex`](project_context/business_logic.md) determines sequence in the final video
* Media cannot be modified during processing
* Minimum 1 media item required for generation
* Maximum media per project: 50 items
* Maximum file sizes:
  * Images: 5MB
  * Videos: 50MB
* Maximum project media size: 500MB
* Supported formats: JPG, PNG, MP4, MOV
* MIME type must match file extension
* Media can be reordered before generation while editing is allowed
* Media is stored in S3

---

### Video Rules

* Generation must be asynchronous and queue-based
* Only one generation job per project at a time
* Failed jobs must update project status to `failed`
* Users can retry failed generations
* Video output format: MP4
* Video output quality: 1080p
* Video stored permanently in S3
* Preview available immediately after completion
* Video generation pipeline must:
  * validate input
  * select deterministic template
  * normalize media
  * sort by [`orderIndex`](project_context/business_logic.md)
  * apply transitions
  * apply text overlays
  * add background music
  * render and upload final output

---

### Template Rules

* Template selection is based on occasion type, category, and mood
* Category fallback is allowed
* Random template selection is forbidden
* The same valid input set should lead to the same template selection path

---

### Publish Rules

* Publish allowed ONLY if project status is `completed`
* Project must have at least one media item
* Project must have a generated video
* User must own the project
* Publish is a one-way action and cannot be reversed
* Publish automatically generates:
  * unique public slug
  * public URL
  * QR code image
* Publish timestamp must be recorded
* Published projects appear in the Memorials section of the product organization
* Public link and QR mapping must remain stable after publish

---

### Memorial-Specific Rules

* Memorial behavior applies when project type is memorial-related
* Memorial-related projects require:
  * `personName`
  * `deathDate`
  * `description`
* Memorial templates use calm, respectful styling
* Memorial QR codes are optimized for physical printing
* Memorial URLs never expire after publish
* Memorial can include profile photo of deceased
* Memorial display may include:
  * hero image
  * person name
  * birth and death dates
  * tribute text
  * gallery of uploaded media
  * memorial video player
  * QR code for sharing

---

### Privacy Rules (Phase 1)

* **Current implementation**: All published reels behave as PUBLIC
* `privacy` is still stored as project data
* UI may show privacy selector, but non-public options are disabled or future-facing
* Public reels are accessible without authentication
* `friends` and `private` are reserved for future implementation
* Non-public access behavior must not be treated as active business logic until fully implemented in [`architecture.md`](project_context/architecture.md) and [`api_documentation.md`](project_context/api_documentation.md)

---

### Public Link & Slug Rules

* Slug is generated only at publish time
* Slug must be unique across all projects
* Slug must be URL-safe
* Slug format is random alphanumeric with 8-12 characters
* Public URL is permanent and never changes
* QR code must continue resolving to the same URL even if the QR image is regenerated

---

## 5. Workflows

### Complete Reel Creation Flow

1. **Authentication**
   * User registers or logs in
   * JWT token issued

2. **Occasion Selection**
   * User browses occasion templates
   * Selects an occasion type
   * System loads occasion-specific form

3. **Details Entry**
   * User fills occasion-specific questions
   * For memorials: `personName`, dates, `description`
   * For celebrations and social occasions: names, dates, messages, and occasion-specific data
   * System validates required fields

4. **Project Creation**
   * System creates project with status = `draft`
   * Stores metadata
   * Returns project ID

5. **Media Upload**
   * User uploads photos/videos
   * System validates each file
   * System uploads accepted files to S3
   * System saves media metadata with [`orderIndex`](project_context/business_logic.md)
   * User can reorder media while editing is allowed

6. **Video Generation**
   * User clicks Create Reel
   * System validates that media exists and required fields are complete
   * System creates VideoJob in queued state
   * Project status becomes `processing`
   * Queue picks up job
   * FFmpeg processes media with deterministic template rules
   * Video uploaded to S3
   * Project status becomes `completed` or `failed`

7. **Preview**
   * User views generated video
   * User may regenerate if allowed by state and API rules

8. **Publish**
   * User clicks Publish
   * System validates status = `completed`
   * System generates unique slug
   * System generates QR code
   * System creates public link
   * Project status becomes `published`
   * Project appears in Memorials section

9. **Share**
   * User downloads QR code
   * User shares public URL
   * Others access via link or QR scan

---

### Memorial Creation Flow

1. User selects a memorial-related occasion such as `memorial`
2. System displays memorial-specific form:
   * deceased person's name (required)
   * birth date (optional)
   * death date (required)
   * relationship (optional)
   * memorial description (required)
   * profile photo upload (optional)
3. User uploads memorial photos/videos
4. System generates video with calm template
5. User previews memorial video
6. User publishes memorial
7. System generates QR code for physical use
8. User downloads QR for printing

---

### Project Regeneration Flow

1. User views eligible project
2. User clicks Regenerate or Recreate
3. System confirms that existing generated video will be replaced
4. System creates new VideoJob
5. Project status becomes `processing`
6. New video generated
7. Previous video is replaced
8. Project status becomes `completed` on success or `failed` on failure

**Eligibility Rules**:

* Regeneration is explicitly allowed for `completed` or `failed` projects
* Generation endpoint behavior must follow [`api_documentation.md`](project_context/api_documentation.md)
* Published project regeneration is not assumed here

---

## 6. Validation Rules

### Project Validation

* `title`: required, min 3 chars, max 100 chars
* `type`: required and must be a valid occasion type from the canonical list
* `userId`: must exist and match authenticated user
* `status`: must be one of `draft`, `processing`, `completed`, `published`, `failed`
* `privacy`: must be one of `public`, `friends`, `private`
* `metadata` must satisfy the rules for the selected occasion type

---

### Media Validation

* File type must be JPG, PNG, MP4, or MOV
* File size:
  * Images: max 5MB
  * Videos: max 50MB
* MIME type must match file extension
* [`orderIndex`](project_context/business_logic.md) must be unique within a project
* `projectId` must exist and belong to the authenticated user
* Media count per project cannot exceed 50
* Total project media size cannot exceed 500MB

---

### Memorial Validation

* `personName`: required, min 2 chars, max 100 chars
* `deathDate`: required, must be valid date, cannot be future
* `birthDate`: optional, must be valid date, must be before `deathDate`
* `description`: required, min 10 chars, max 1000 chars
* `profileImage`: optional, must be valid image format, max 5MB

---

### Occasion-Specific Validation

**Birthday**:
* `personName`: required
* `birthDate`: required
* `age`: optional, must be positive integer

**Anniversary**:
* `coupleNames`: required
* `anniversaryDate`: required
* `years`: optional, must be positive integer

**Wedding**:
* `coupleNames`: required
* `weddingDate`: required
* `venue`: optional

**Memorial**:
* `personName`: required
* `deathDate`: required
* `description`: required

---

### Publish Validation

* Project status must be `completed`
* Project must have at least one media item
* Project must have generated video
* User must own the project
* Slug must be unique and system-generated

---

## 7. State Machine

### Project State Flow

```text
draft → processing → completed → published
           ↓
        failed
```

### Transitions

* `draft` → `processing`: video generation starts
* `failed` → `processing`: retry or regenerate starts
* `processing` → `completed`: generation succeeds
* `processing` → `failed`: generation fails
* `completed` → `published`: publish action

### State Rules

* `draft`: editable
* `processing`: locked for modification
* `completed`: ready for preview and publish
* `published`: permanently public in Phase 1 behavior
* `failed`: recoverable through retry or regenerate flow

---

## 8. Permissions & Access Control

* Users can only access their own authenticated project resources
* Public pages are accessible without login
* Public endpoints are read-only
* Published project ownership does not change after sharing
* Future non-public access requires explicit support in [`architecture.md`](project_context/architecture.md) and [`api_documentation.md`](project_context/api_documentation.md)

---

## 9. Data Lifecycle

* Media files are stored in S3
* Videos are stored permanently
* Public links remain active after publish
* QR codes remain downloadable after publish
* Regeneration replaces the previous generated video for the same project
* Published URLs and slugs remain stable once created

---

## 10. Notifications & User Feedback

### System Notifications

* **Video Processing Started**: "Your reel is being created..."
* **Video Completed**: "Your reel is ready! Click to preview."
* **Video Failed**: "Video generation failed. Please try again."
* **Project Published**: "Your memorial is now live!"
* **QR Code Ready**: "QR code is ready for download."
* **Upload Progress**: Real-time progress bar for media uploads
* **Media Validation Errors**: Immediate feedback on invalid files

### Feedback Rules

* Validation errors should be shown inline where appropriate
* Network failures should show user-friendly messages
* Upload failures should support retry behavior
* Video generation failures should expose retry or regenerate action where allowed

### Email Notifications (Optional)

* Video generation completed
* Memorial published successfully
* Weekly summary of activity

---

## 11. Key Metrics & Analytics

### User Metrics

* Total projects created per user
* Completed vs incomplete projects
* Published memorials count
* Average time to complete project

### System Metrics

* Video generation success rate
* Average processing time per video
* Failed generation reasons
* Storage usage per user
* QR code downloads

### Memorial Metrics

* Total memorials created
* Memorial views (future)
* QR code scans (future)
* Most popular occasion types

---

## 12. Edge Cases & Error Scenarios

### Case: No Media Uploaded

* **Scenario**: User tries to generate video without media
* **Behavior**: Block generation and show error message
* **Message**: "Please upload at least one photo or video"

---

### Case: Duplicate [`orderIndex`](project_context/business_logic.md)

* **Scenario**: Duplicate media order is submitted within the same project
* **Behavior**: Reject request or resolve through controlled reorder logic
* **Rule**: Final stored media sequence must remain unique and deterministic

---

### Case: Video Processing Failure

* **Scenario**: FFmpeg or asynchronous generation fails
* **Behavior**:
  * Status → `failed`
  * Log error details
  * Notify user
  * Provide retry option

---

### Case: Publish Before Completion

* **Scenario**: User tries to publish a `draft`, `processing`, or `failed` project
* **Behavior**: Block action and show error
* **Message**: "Please wait for video generation to complete"

---

### Case: Large File Upload

* **Scenario**: User uploads file exceeding size limit
* **Behavior**:
  * Reject upload immediately
  * Show error with the relevant size limit
  * Suggest compression guidance if needed

---

### Case: Invalid Media Format

* **Scenario**: User uploads unsupported file type
* **Behavior**:
  * Reject upload
  * Show supported formats
  * Provide correction guidance

---

### Case: Concurrent Generation Attempts

* **Scenario**: User triggers generation multiple times while a job is already active
* **Behavior**:
  * Only one active job is allowed per project
  * Additional attempts are blocked or ignored
  * Show an "Already processing" style message

---

### Case: QR Code Generation Failure

* **Scenario**: QR generation fails during publish
* **Behavior**:
  * Retry according to configured system strategy where implemented
  * Log error for manual resolution
  * Notify user of failure or partial completion according to actual implementation
* **Constraint**:
  * Do not assume alternate publish behavior unless defined in [`architecture.md`](project_context/architecture.md) or [`api_documentation.md`](project_context/api_documentation.md)

---

### Case: Deleted Media During Processing

* **Scenario**: Media becomes unavailable while video is processing
* **Behavior**:
  * Generation fails
  * Status → `failed`
  * Show clear error message
  * Require re-upload and regeneration

---

### Case: Memorial with Future `deathDate`

* **Scenario**: User enters future date for `deathDate`
* **Behavior**:
  * Validation error
  * Block form submission
  * Message: "Death date cannot be in the future"

---

## 13. Business Constraints

### Technical Constraints

* Video processing time target: 2-5 minutes per reel
* Maximum media per project: 50 items
* Maximum project media size: 500MB total
* Concurrent processing target: 5 jobs maximum
* QR code generation target: less than 1 second

### User Constraints

* Free tier: 5 projects per month
* Premium tier: unlimited projects
* Storage limit: 5GB per user for free plan
* Storage limit: 50GB per user for premium plan
* Video length target: 1-5 minutes

### Content Constraints

* No inappropriate content
* No copyrighted music outside approved/provided tracks
* No offensive memorial content
* Respect privacy and dignity of deceased persons

---

## 14. Future Enhancements

### Privacy Features

* Friends-only sharing
* Private memorials
* Password-protected access
* Expiring links

### Social Features

* Comments on memorials
* Condolence messages
* Memorial guestbook
* Share to social media

### Advanced Features

* Custom templates
* Video editing tools
* Collaborative projects
* Memorial analytics
* QR code tracking

---

## 15. Source-of-Truth Notes

### Architecture-Derived Rules

The following should be treated as architecture-backed constraints:

* Canonical project fields and state machine
* Occasion categories and memorial-related occasion types
* Deterministic template selection
* Video pipeline stages
* Public link format and permanence
* QR code size, format, and reliability requirements
* Phase 1 privacy behavior
* Product organization of My Projects and Memorials

### API-Derived Rules

The following should be treated as API-backed operational rules:

* Request validation shape for project creation and memorial metadata
* Allowed project update and delete states
* Video generation and regeneration eligibility
* Publish endpoint restrictions
* Public endpoint behavior
* QR retrieval and download behavior
* Error codes and response structures

### Conflict Resolution

* If this document is less specific than [`architecture.md`](project_context/architecture.md) or [`api_documentation.md`](project_context/api_documentation.md), use the more specific source
* If this document implies unsupported behavior, do not implement that behavior until the authoritative document is updated

---

## 16. AI Constraints (CRITICAL)

* DO NOT invent business rules
* DO NOT change workflows without updating authoritative documents
* DO NOT skip validation
* FOLLOW [`architecture.md`](project_context/architecture.md) and [`api_documentation.md`](project_context/api_documentation.md) strictly
* DO NOT assume memorial behavior beyond documented specifications

IF ANYTHING IS UNCLEAR:
→ STOP AND ASK USER

---

**Last Updated**: 2026-05-20