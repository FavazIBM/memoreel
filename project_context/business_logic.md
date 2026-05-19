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

---

## 2. Core Business Concepts

### Project

**Definition**: A container representing a memory video.

**Purpose**: Central unit for storing media, text, and generated video.

**Rules**:

* Each project belongs to one user
* A project must have a title and occasion type
* A project must follow a strict lifecycle

---

### Media

**Definition**: Images or videos uploaded by user.

**Purpose**: Input for video generation.

**Rules**:

* Must have orderIndex
* Must be validated (type + size)
* Sequence MUST be preserved

---

### Video

**Definition**: Generated output of a project.

**Purpose**: Final consumable content.

**Rules**:

* Generated asynchronously
* Must follow template rules
* Only one active video per project

---

### Template

**Definition**: Predefined styling and structure for video generation.

**Purpose**: Ensure consistent output quality.

**Rules**:

* Selected based on occasion + category + mood
* NO randomness allowed

---

### Memorial

**Definition**: Special project type for tribute and remembrance of deceased persons.

**Purpose**: Create permanent digital memorials that can be shared physically and digitally.

**Rules**:

* Must include personName (required)
* Must include deathDate (required)
* Must include description/tribute (required)
* Optional: birthDate, profileImage, relationship, lifeStory
* Uses calm, respectful visual templates
* Automatically generates QR code on publish
* QR code can be printed for physical placement (tombstones, memorial cards)
* Memorial URL is permanent and never expires
* Privacy can be set (public/private)

---

### QR Code

**Definition**: Scannable image that links directly to a published memorial or reel.

**Purpose**: Enable physical-digital integration for memorials and easy sharing.

**Rules**:

* Generated automatically when project is published
* Must be permanent (never expires or changes)
* Must map to unique public URL
* Format: PNG image (512x512px)
* High error correction level for reliability
* Downloadable in high resolution for printing
* Can be scanned from any mobile device
* Direct link (no intermediate pages)

**Use Cases**:
* Engraved on tombstones
* Printed on memorial cards
* Displayed at funeral services
* Included in obituaries
* Shared digitally via social media

---

## 3. Business Rules

### Project Rules

* Projects start in "draft" state upon creation
* Cannot publish before video generation completes
* Only completed projects can be published
* Published projects cannot be unpublished (permanent)
* Failed projects can be regenerated
* Draft projects can be edited at any time
* Processing projects cannot be modified

---

### Media Rules

* Media must be uploaded before video generation
* orderIndex must be unique per project
* orderIndex determines sequence in video
* Media cannot be modified during processing
* Minimum 1 media item required for generation
* Maximum file sizes: Images 5MB, Videos 50MB
* Supported formats: JPG, PNG, MP4, MOV
* Media can be reordered before generation

---

### Video Rules

* Generation must be asynchronous (queue-based)
* Only one generation job per project at a time
* Failed jobs must update project status to "failed"
* Users can retry failed generations
* Video output format: MP4, 1080p
* Video stored permanently in S3
* Preview available immediately after completion
* Regeneration creates new video (replaces old)

---

### Publish Rules

* Publish allowed ONLY if status = "completed"
* Publish is a one-way action (cannot unpublish)
* Publish automatically generates:
  * Unique public slug
  * Public URL
  * QR code image
* Published projects appear in "Memorials" section
* Privacy setting applied at publish time
* Publish timestamp recorded

---

### Memorial-Specific Rules

* Memorial type requires additional fields:
  * personName (required)
  * deathDate (required)
  * description (required)
* Memorial templates use calm, respectful styling
* Memorial QR codes are optimized for physical printing
* Memorial URLs never expire
* Memorial privacy defaults to "public"
* Memorial can include profile photo of deceased

---

### Privacy Rules (Phase 1)

* **Current Implementation**: All published reels are PUBLIC
* **Future**: Friends and Private options
* Privacy setting stored in database
* Privacy selector shown in UI (non-public disabled)
* Public reels accessible without authentication
* Private reels require authentication (future)
* Friends-only reels require friend relationship (future)

---

## 4. Workflows

### Complete Reel Creation Flow

1. **Authentication**
   * User registers or logs in
   * JWT token issued

2. **Occasion Selection**
   * User browses occasion templates
   * Selects appropriate occasion type
   * System loads occasion-specific form

3. **Details Entry**
   * User fills occasion-specific questions
   * For memorials: personName, dates, description
   * For celebrations: names, dates, messages
   * System validates required fields

4. **Project Creation**
   * System creates project (status = draft)
   * Stores occasion metadata
   * Returns project ID

5. **Media Upload**
   * User uploads photos/videos via drag-and-drop
   * System validates each file (type, size)
   * System uploads to S3
   * System saves media metadata with orderIndex
   * User can reorder media

6. **Video Generation**
   * User clicks "Create Reel"
   * System validates: media exists, required fields complete
   * System creates VideoJob (queued)
   * Project status → processing
   * Queue picks up job
   * FFmpeg processes media with template
   * Video uploaded to S3
   * Project status → completed (or failed)

7. **Preview**
   * User views generated video
   * Option to regenerate if not satisfied

8. **Publish**
   * User clicks "Publish"
   * System validates status = completed
   * System generates unique slug
   * System generates QR code
   * System creates public link
   * Project status → published
   * Project moves to "Memorials"

9. **Share**
   * User downloads QR code
   * User shares public URL
   * Others access via link or QR scan

---

### Memorial Creation Flow (Specific)

1. User selects "Memorial" occasion
2. System displays memorial-specific form:
   * Deceased person's name (required)
   * Birth date (optional)
   * Death date (required)
   * Relationship (optional)
   * Memorial description (required)
   * Profile photo upload (optional)
3. User uploads memorial photos/videos
4. System generates video with calm template
5. User previews memorial video
6. User publishes memorial
7. System generates QR code for physical use
8. User downloads QR for printing (tombstone, cards)

---

### Project Regeneration Flow

1. User views completed project
2. User clicks "Recreate"
3. System confirms action (will replace video)
4. System creates new VideoJob
5. Project status → processing
6. New video generated
7. Old video replaced
8. Project status → completed

---

## 5. Validation Rules

### Project Validation

* title: required, min 3 chars, max 100 chars
* type: must be valid occasion type from predefined list
* userId: must exist and match authenticated user
* status: must be valid state (draft, processing, completed, published, failed)

---

### Media Validation

* file type: must be JPG, PNG, MP4, or MOV
* file size:
  * Images: max 5MB
  * Videos: max 50MB
* MIME type: must match file extension
* orderIndex: must be unique within project
* projectId: must exist and belong to user

---

### Memorial Validation

* personName: required, min 2 chars, max 100 chars
* deathDate: required, must be valid date, cannot be future
* birthDate: optional, must be valid date, must be before deathDate
* description: required, min 10 chars, max 1000 chars
* profileImage: optional, must be valid image format, max 5MB

---

### Occasion-Specific Validation

**Birthday:**
* personName: required
* birthDate: required
* age: optional, must be positive integer

**Anniversary:**
* couple names: required
* anniversaryDate: required
* years: optional, must be positive integer

**Wedding:**
* couple names: required
* weddingDate: required
* venue: optional

---

### Publish Validation

* Project status must be "completed"
* Project must have at least one media item
* Project must have generated video
* User must own the project
* Slug must be unique (system-generated)

---

## 6. State Machine

### Project State Flow

```
draft → processing → completed → published
           ↓
         failed
```

### Transitions

* draft → processing: video generation starts
* processing → completed: success
* processing → failed: error
* completed → published: publish action

---

## 7. Permissions & Access Control

* Users can ONLY access their own projects
* Public pages are accessible without login
* Private projects are restricted

---

## 8. Data Lifecycle

* Media stored in S3
* Videos stored permanently
* Public links remain active after publish

---

## 9. Edge Cases

### Case: No Media Uploaded

* Generation must be blocked

---

### Case: Duplicate orderIndex

* Must be rejected

---

### Case: Video Processing Failure

* Status → failed
* User can retry

---

### Case: Publish Before Completion

* Must be rejected

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

### Email Notifications (Optional)

* Video generation completed
* Memorial published successfully
* Weekly summary of activity

---

## 11. Key Metrics & Analytics

### User Metrics

* Total projects created per user
* Completed vs. incomplete projects
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
* **Behavior**: Block generation, show error message
* **Message**: "Please upload at least one photo or video"

---

### Case: Duplicate orderIndex

* **Scenario**: System assigns duplicate orderIndex
* **Behavior**: Auto-correct by reassigning indices
* **Prevention**: Use atomic increment for orderIndex

---

### Case: Video Processing Failure

* **Scenario**: FFmpeg fails during generation
* **Behavior**:
  * Status → failed
  * Log error details
  * Notify user
  * Provide retry option
* **User Action**: Click "Regenerate" to retry

---

### Case: Publish Before Completion

* **Scenario**: User tries to publish draft or processing project
* **Behavior**: Block action, show error
* **Message**: "Please wait for video generation to complete"

---

### Case: Large File Upload

* **Scenario**: User uploads file exceeding size limit
* **Behavior**:
  * Reject upload immediately
  * Show error with size limit
  * Suggest compression tools

---

### Case: Invalid Media Format

* **Scenario**: User uploads unsupported file type
* **Behavior**:
  * Reject upload
  * Show supported formats
  * Provide format conversion guidance

---

### Case: Concurrent Generation Attempts

* **Scenario**: User clicks generate multiple times
* **Behavior**:
  * Only first request processed
  * Subsequent requests ignored
  * Show "Already processing" message

---

### Case: QR Code Generation Failure

* **Scenario**: QR generation fails during publish
* **Behavior**:
  * Retry automatically (3 attempts)
  * If all fail, publish without QR
  * Log error for manual resolution
  * Notify user of partial success

---

### Case: Deleted Media During Processing

* **Scenario**: Media deleted while video is processing
* **Behavior**:
  * Generation fails
  * Status → failed
  * Clear error message
  * Require re-upload and regeneration

---

### Case: Memorial with Future Death Date

* **Scenario**: User enters future date for deathDate
* **Behavior**:
  * Validation error
  * Block form submission
  * Message: "Death date cannot be in the future"

---

## 13. Business Constraints

### Technical Constraints

* Video processing time: 2-5 minutes per reel
* Maximum media per project: 50 items
* Maximum project size: 500MB total
* Concurrent processing: 5 jobs maximum
* QR code generation: < 1 second

### User Constraints

* Free tier: 5 projects per month
* Premium tier: Unlimited projects
* Storage limit: 5GB per user (free), 50GB (premium)
* Video length: 1-5 minutes

### Content Constraints

* No inappropriate content
* No copyrighted music (use provided tracks)
* No offensive memorial content
* Respect privacy of deceased

---

## 14. Future Enhancements

### Privacy Features

* Friends-only sharing
* Private memorials (password-protected)
* Expiring links (temporary access)

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

## 15. AI Constraints (CRITICAL)

* DO NOT invent business rules
* DO NOT change workflows
* DO NOT skip validation
* FOLLOW architecture.md + api_documentation.md strictly
* DO NOT assume memorial behavior - follow specifications

IF ANYTHING IS UNCLEAR:
→ STOP AND ASK USER

---

**Last Updated**: 2026-05-19