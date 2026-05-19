# System Architecture & Product Specification
---

## 1. Product Overview

**Memoreel** is a web platform for creating emotionally rich memory video reels for occasions and memorials.

Core capabilities:

* User account creation and authentication
* Template-based reel creation for different occasions
* Upload media (images/videos)
* Automated video generation via backend video editor
* Preview, recreate, and publish reels
* Share via public links and QR codes
* Memorial system with QR for physical usage (tombstones, memorial cards)
* Project management (My Projects for drafts, Memorials for published)

---

## 2. Core User Flow (STRICT – MUST NOT CHANGE)

### Wizard Flow

1. **Select Occasion**: Choose from predefined templates (birthday, anniversary, memorial, etc.)
2. **Enter Details**: Answer basic info questions about the occasion (title, dates, person details for memorials)
3. **Upload Media**: Upload photos and videos with drag-and-drop interface
4. **Generate Video**: Backend automatically creates reel using FFmpeg
5. **Preview**: Watch the generated video
6. **Recreate (Optional)**: Regenerate if changes needed
7. **Publish**: Make reel public and generate QR code

---

### Wizard UI Requirements

* Step indicator (1–7 steps)
* Active + completed states
* Back / Next navigation
* State persistence across steps
* Occasion-specific forms (different questions based on occasion type)

---

## 3. Authentication System

### Features

* Email/password registration and login
* Google OAuth login
* Account settings management
* Profile picture upload
* Password change functionality

### Backend Rules

* Password hashing (bcrypt)
* JWT authentication
* Secure sessions
* Token refresh mechanism
* Email validation

---

## 4. Authorization Rules (STRICT)

* Users can ONLY access their own projects
* Public endpoints are read-only
* Unauthorized access MUST return 403

---

## 5. Dashboard System

### Sidebar Navigation

* Dashboard (home/overview)
* My Projects (incomplete/draft projects)
* Memorials (published reels)
* Templates (browse available templates)
* Settings (account settings)

---

### Dashboard Sections

* Welcome banner with gradient background
* Quick stats cards:
  * Total Projects (all projects)
  * Completed Projects (ready to publish)
  * Published Projects (live memorials)
* Recent projects grid
* Quick action: "Create New Reel" button

---

### Project Card Components

* Thumbnail image (from first media or default)
* Title
* Occasion badge (with icon and color)
* Status badge (draft, processing, completed, published)
* Date created/modified
* Action buttons:
  * Edit (for drafts)
  * Generate (for drafts with media)
  * Preview (for completed)
  * Publish (for completed)
  * View (for published)

---

## 6. Project System

### Fields

* id (unique identifier)
* userId (owner)
* title (user-defined)
* type (occasion type)
* status (draft, processing, completed, published, failed)
* privacy (public, friends, private - currently only public)
* createdAt
* updatedAt
* publishedAt
* metadata (occasion-specific data)

---

### State Machine

```
draft → processing → completed → published
           ↓
        failed (can retry)
```

**RULES:**

* Projects start in "draft" state
* Can only generate video from "draft" state
* Can only publish from "completed" state
* Failed projects can be regenerated
* Published projects cannot be unpublished (permanent)

---

## 7. Occasion System

### Categories

#### Celebration
* birthday
* anniversary
* wedding
* graduation
* baby_shower
* housewarming
* retirement

#### Social
* farewell
* trip_memory
* friendship
* reunion
* milestone
* achievement

#### Emotional (Memorial)
* memorial
* condolence
* remembrance_day

#### Custom
* custom (user-defined)

---

### Occasion-Specific Forms

Each occasion type has specific questions:

**Birthday:**
* Person's name
* Age/Birthday date
* Relationship to user
* Special message

**Anniversary:**
* Couple names
* Anniversary date
* Years together
* Special message

**Memorial:**
* Deceased person's name (required)
* Birth date (optional)
* Death date (required)
* Relationship
* Memorial description (required)
* Profile image (optional)

**Wedding:**
* Couple names
* Wedding date
* Venue
* Special message

---

## 8. Media Upload System

### Formats

* Images: JPG, PNG
* Videos: MP4, MOV

---

### Limits

* Images: 5MB
* Videos: 50MB

---

### Backend Validation (STRICT)

* Validate MIME type
* Validate file size
* Reject invalid uploads

---

### UI Behavior

* Drag & drop
* Upload progress
* Media grid
* Order index display
* Selection + bulk actions

---

## 9. Storage Structure (STRICT)

S3 paths:

* /users/{userId}/projects/{projectId}/media/{file}
* /videos/{projectId}.mp4
* /qr/{projectId}.png

---

## 10. Media Data Rules

* Each media MUST have orderIndex
* Sequence MUST be preserved
* No duplicate orderIndex

---

## 11. Text Content System

* Stored in project metadata
* Occasion-specific text fields
* Linked to project
* Injected into video pipeline as overlays
* Supports multi-line text
* Font styling based on template

---

## 12. Video Generation Pipeline (STRICT)

**Process Flow:**

1. **Validate Input**
   * Check media exists
   * Verify media formats
   * Validate orderIndex sequence

2. **Select Template**
   * Based on occasion type
   * Match category and mood
   * NO randomness - deterministic selection

3. **Normalize Media**
   * Resize images to standard dimensions
   * Convert videos to standard format
   * Optimize file sizes

4. **Sort Media**
   * Order by orderIndex (ascending)
   * Preserve user-defined sequence

5. **Apply Transitions**
   * Fade, slide, zoom based on template
   * Timing based on template duration

6. **Apply Text Overlays**
   * Title screens
   * Occasion-specific text
   * Credits/closing

7. **Add Background Music**
   * Template-specific music track
   * Fade in/out
   * Loop if needed

8. **Render Video**
   * FFmpeg processing
   * Output: MP4 format
   * Quality: 1080p
   * Upload to S3

---

## 13. Video Job System (QUEUE)

### Model

VideoJob:

* id
* projectId
* status (queued, processing, completed, failed)
* startedAt
* completedAt

---

### Queue Rules

* Queue: Spring Batch + Quartz Scheduler
* Job type: video_generation
* Retries: 3
* Backoff: exponential
* Use @Async for asynchronous processing

---

## 14. Template System

### Structure

* id
* name
* occasionType
* mood
* duration
* transitions[]
* musicTrack
* textStyle

---

### Selection Rules

* Based on occasion + category + mood
* Fallback → category
* NO randomness

---

## 15. Preview System

* Video player
* Progress bar
* Duration
* Actions: regenerate, publish

---

## 16. Public Viewer System

### Hero Section

* title
* author
* date
* badges:

  * category
  * duration
  * momentCount

---

### Rules

momentCount = total media items

---

### Content

* video player
* description
* thumbnails

---

## 17. Memorial System

### Required Fields

* personName (deceased person's full name)
* deathDate (date of passing)
* description (memorial message/tribute)

### Optional Fields

* birthDate (date of birth)
* profileImage (photo of deceased)
* relationship (to user)
* lifeStory (extended biography)

---

### Memorial-Specific Features

* Calm, respectful visual templates
* Soft background music
* Privacy settings (public/private)
* QR code generation for physical placement
* Permanent public URL
* View counter (optional)

---

### Memorial Display

* Hero section with person's photo
* Name and dates (birth - death)
* Memorial video player
* Description/tribute text
* Photo gallery from uploaded media
* QR code for sharing

---

## 18. QR Code System

### Generation

* Generated automatically on publish
* Links to public memorial URL
* Format: PNG image
* Size: 512x512px
* Error correction: High level

### Features

* Downloadable (high resolution)
* Printable quality
* Permanent (never expires)
* Scannable from mobile devices
* Direct link to memorial page

### Use Cases

* Print on memorial cards
* Display at funeral/memorial service
* Engrave on tombstones
* Include in obituaries
* Share digitally

---

## 19. Public Link Rules

### Slug Generation

* Generated at publish time
* Must be unique across all projects
* URL-safe characters only
* Format: random alphanumeric (8-12 chars)
* Example: `memoreel.app/m/abc123xyz`

### Link Behavior

* Permanent (never changes)
* Publicly accessible (no auth required)
* SEO-friendly
* Shareable via social media
* Embeddable (future feature)

---

## 20. Settings System

### Account Settings Sections

**Profile Information:**
* Name
* Email
* Profile picture upload
* Bio (optional)

**Security:**
* Change password
* Two-factor authentication (future)
* Active sessions management

**Preferences:**
* Email notifications toggle
* Privacy defaults
* Language selection
* Theme (light/dark)

**Subscription/Plan:**
* Current plan display
* Usage statistics
* Upgrade options
* Billing information

---

## 21. Database Schema

### User

* id
* email
* passwordHash
* avatarUrl
* planType

---

### Project

* id
* userId
* title
* type
* status
* privacy

---

### Media

* id
* projectId
* type
* url
* orderIndex

---

### TextContent

* id
* projectId
* content

---

### Video

* id
* projectId
* url

---

### VideoJob

* id
* projectId
* status
* startedAt
* completedAt

---

### PublicLink

* id
* projectId
* slug

---

### QRCode

* id
* projectId
* qrImageUrl

---

## 22. API Contract (STRICT)

### Auth

* POST /auth/register
* POST /auth/login
* POST /auth/google

---

### User

* PUT /user/profile
* PUT /user/password

---

### Projects

* POST /projects
* GET /projects
* GET /projects/:id

---

### Media

* POST /media/upload
* DELETE /media/:id

---

### Video

* POST /video/generate
* GET /video/:projectId

---

### Publish

* POST /projects/:id/publish

---

### Public

* GET /public/:slug

---

### QR

* GET /qr/:projectId

---

## 23. Data Flow (STRICT)

1. Upload → S3
2. Save → DB
3. Generate → Queue
4. Process → FFmpeg
5. Save video
6. Generate link + QR

---

## 24. Error Handling (STRICT)

* Upload failure → rollback
* Processing failure → status = failed
* APIs MUST return structured errors

---

## 25. Security (STRICT)

* JWT authentication
* Input validation
* Rate limiting
* Secure file uploads

---

## 26. Infrastructure

* Frontend: React + TypeScript
* Backend: Java 17 + Spring Boot 3.2
* Queue: Spring Batch + Quartz Scheduler
* Storage: AWS S3
* Processing: FFmpeg
* Database: PostgreSQL 15
* Cache: Redis 7
* Build Tools: Maven (Backend), Vite (Frontend)

---

## 27. UI Design Enforcement

* Celebration → vibrant
* Memorial → minimal
* Card-based UI
* Smooth transitions

---

## 28. AI Constraints (CRITICAL – ZERO FAILURE MODE)

AI MUST:

* NOT hallucinate
* NOT assume missing data
* NOT invent DB schema, APIs, or flows

IF ANYTHING IS UNCLEAR:
→ STOP
→ ASK USER

AI MUST strictly follow:

* This document
* BobRules.md
* UI structure
* Data model

---

## 29. Non-Functional Requirements

* Scalable
* Async processing
* Handles large uploads
* Responsive UI
* Fast performance

---

## UI Component Mapping (STRICT)

Dashboard:
- Sidebar (fixed width)
- Header (top bar)
- Content (scrollable)

Create Project:
- StepIndicator component
- OccasionCard grid
- TitleInput section

Upload Page:
- UploadZone component
- MediaGrid component
- BulkActionBar component

Public Viewer:
- HeroSection
- VideoPlayer
- ThumbnailGrid
- ShareBar

Settings:
- Sidebar
- SectionCard
- ToggleRow

## UI Styling Rules (STRICT)

- Border radius: 12px–20px
- Cards: white background + light border
- Gradients:
  - Primary: #6366F1 → #EC4899
- Font: clean sans-serif
- Shadows: soft (no heavy shadows)

Hover:
- slight scale (1.02)
- shadow increase

Spacing:
- consistent padding (16–32px)

DO NOT:
- use random colors
- change layout structure


## Project Structure (STRICT)

The project MUST follow this exact structure.
AI MUST NOT change folder hierarchy.

---

### Root

```
project-root/
├── frontend/
├── backend/
├── shared/
├── .env
├── .env.example
├── README.md
```

---

## Frontend Structure (React)

```
frontend/
  src/
    components/      # Reusable UI components (cards, buttons, modals)
    pages/           # Page-level components (Dashboard, Upload, Preview, etc.)
    layouts/         # Layout wrappers (Sidebar, Header)
    services/        # API calls
    store/           # State management
    hooks/           # Custom hooks
    utils/           # Helper functions
    config/          # API base URLs, constants
    assets/          # Images, icons
    types/           # TypeScript types
```

### Rules

* Pages MUST map to routes
* Components MUST be reusable
* Business logic MUST NOT be inside UI components

---

## Backend Structure (Spring Boot Modular Architecture)

```
backend/
  src/
    main/
      java/
        com/
          memoreel/
            auth/
              controller/
                AuthController.java
              service/
                AuthService.java
              repository/
                UserRepository.java
              dto/
                LoginRequest.java
                RegisterRequest.java
              entity/
                User.java

            project/
              controller/
                ProjectController.java
              service/
                ProjectService.java
              repository/
                ProjectRepository.java
              dto/
                ProjectDTO.java
                CreateProjectRequest.java
              entity/
                Project.java

            media/
              controller/
                MediaController.java
              service/
                MediaService.java
              repository/
                MediaRepository.java
              entity/
                Media.java

            video/
              controller/
                VideoController.java
              service/
                VideoService.java
              batch/
                VideoProcessingJob.java
                VideoProcessor.java
              entity/
                Video.java
                VideoJob.java

            qr/
              controller/
                QRCodeController.java
              service/
                QRCodeService.java
              entity/
                QRCode.java

            config/
              SecurityConfig.java
              S3Config.java
              RedisConfig.java
              BatchConfig.java

            exception/
              GlobalExceptionHandler.java
              CustomExceptions.java

            util/
              FFmpegUtil.java
              FileUtil.java

      resources/
        application.properties
        application-dev.properties
        application-prod.properties
        db/
          migration/
            V1__create_users_table.sql
            V2__create_projects_table.sql

    test/
      java/
        com/
          memoreel/
            # Test classes mirror main structure
```

### Rules

* Each module MUST be self-contained
* Follow Spring Boot package conventions
* Business logic MUST be inside services
* Controllers MUST only handle request/response
* Use DTOs for API requests/responses
* Entities for database mapping only
* Use constructor injection for dependencies

---

## Shared Structure

```
shared/
  types/              # Shared TypeScript types for frontend
```

---

## Environment Configuration

```
backend/src/main/resources/
  application.properties           # Main config
  application-dev.properties       # Dev environment
  application-prod.properties      # Production environment
  
# Secrets (DO NOT COMMIT)
application-local.properties       # Local overrides (gitignored)
```

---

## Required Root Files

```
README.md           # Project documentation
backend/pom.xml     # Maven dependencies
frontend/package.json  # Frontend dependencies
```

---

## STRICT ENFORCEMENT

AI MUST:

* Follow this structure EXACTLY
* NOT create additional root-level folders
* NOT mix frontend/backend logic
* NOT place files in incorrect modules

IF STRUCTURE IS UNCLEAR:
→ STOP and ASK USER for clarification

## UI Design Source of Truth (CRITICAL)

The UI MUST strictly follow the design images located in:

project_context/Design/

---

### Mapping of Screens to Pages

**Authentication:**
* Login/Register Page → Design/authentication_page.png

**Dashboard:**
* Main Dashboard → Design/Dashboard.png

**Create Reel Flow:**
* Occasion Selection → Design/Create.png
* Memorial Details Form → Design/create a memorial 1.png + Design/create a memorial 2.png
* Media Upload → Design/Upload.png

**Output & Publishing:**
* Video Preview → Design/output 1.png + Design/output 2.png
* QR Code Display → Design/QR.png

**Settings:**
* Account Settings → Design/account settings 1.png + Design/account settings 2.png

---

### Multi-Screenshot Handling (STRICT)

Some pages are split across multiple images.

AI MUST:

* Combine all related screenshots into ONE page
* Treat images as:

  * Top section → first image
  * Bottom/remaining → second image

AI MUST NOT:

* Ignore any screenshot
* Create separate pages for split images

---

### Image Priority Rules (VERY IMPORTANT)

* Design images are the **PRIMARY SOURCE OF TRUTH**
* Text-based UI descriptions are **SECONDARY**

IF conflict occurs:
→ FOLLOW DESIGN IMAGES

---

### Layout Enforcement

AI MUST:

* Match layout structure exactly
* Match positioning of elements
* Match grouping of components

AI MUST NOT:

* Rearrange sections
* Change layout structure
* Simplify UI

---

### Component Extraction Rule

AI SHOULD:

* Break UI into reusable components
* BUT preserve visual structure

---

### Missing Details Rule

If any UI detail is unclear from images:

→ AI MUST ASK USER
→ DO NOT GUESS

---

## STRICT ENFORCEMENT

Failure to follow design images is NOT allowed.

UI must visually match provided screenshots as closely as possible.

---

## Additional Memoreel-Specific Requirements

### Privacy Implementation (Phase 1)

* **Current**: All published reels are PUBLIC
* **Future**: Friends and Private options
* Privacy setting stored but not enforced yet
* UI shows privacy selector (disabled for non-public)

### Project Organization

**My Projects Page:**
* Shows all draft and processing projects
* Filter by status
* Sort by date created/modified
* Search by title
* Bulk actions (delete, archive)

**Memorials Page:**
* Shows all published projects
* Grid or list view
* Filter by occasion type
* Sort by publish date
* Search functionality
* View count display

### Notification System

* Video generation started
* Video generation completed
* Video generation failed
* Project published successfully
* QR code ready for download

### Error Handling

* Upload failures with retry
* Video generation failures with regenerate option
* Network errors with user-friendly messages
* Validation errors with inline feedback

---

**Last Updated**: 2026-05-19
