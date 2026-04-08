# System Architecture & Product Specification 
---

## 1. Product Overview

A web platform for creating emotionally rich memory videos for occasions and memorials.

Core capabilities:

* Upload media (images/videos/text)
* Generate videos via templates
* Share via public links and QR codes
* Memorial system with QR for physical usage

---

## 2. Core User Flow (STRICT – MUST NOT CHANGE)

### Wizard Flow

1. Select Occasion
2. Enter Title
3. Upload Media
4. Preview & Generate

---

### Wizard UI Requirements

* Step indicator (1–4)
* Active + completed states
* Back / Next navigation
* State persistence

---

## 3. Authentication System

### Features

* Email/password login
* Google OAuth login

### Backend Rules

* Password hashing (bcrypt)
* JWT authentication
* Secure sessions

---

## 4. Authorization Rules (STRICT)

* Users can ONLY access their own projects
* Public endpoints are read-only
* Unauthorized access MUST return 403

---

## 5. Dashboard System

### Sidebar

* Dashboard
* My Projects
* Memorials
* Templates
* Settings

---

### Dashboard Sections

* Welcome banner (gradient)
* Stats:

  * totalProjects
  * completedProjects
  * publishedProjects

---

### Project Card

* thumbnail
* title
* occasion badge
* status badge
* actions: edit, generate, publish

---

## 6. Project System

### Fields

* id
* userId
* title
* type
* status
* privacy

---

### State Machine

draft → processing → completed → published
failure → failed

RULE:

* ONLY completed → publish

---

## 7. Occasion System

### Categories

#### Celebration

birthday, anniversary, wedding, graduation, baby_shower, housewarming, retirement

#### Social

farewell, trip_memory, friendship, reunion, milestone, achievement

#### Emotional

memorial, condolence, remembrance_day

#### Custom

custom

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

* Stored separately
* Linked to project
* Injected into video pipeline

---

## 12. Video Generation Pipeline (STRICT)

1. Validate input
2. Select template (NO randomness)
3. Normalize media
4. Sort by orderIndex
5. Apply transitions
6. Apply text overlays
7. Add background music
8. Render

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

* Queue: Redis + BullMQ
* Job type: video_generation
* Retries: 3
* Backoff: exponential

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

### Required

* personName
* deathDate
* description

### Optional

* birthDate
* profileImage

---

### Additional

* privacy (public/private)

---

## 18. QR Code System

* QR linked to public URL
* Downloadable
* Permanent

---

## 19. Public Link Rules

* slug must be:

  * unique
  * URL-safe
* generated at publish

---

## 20. Settings System

* Profile update
* Password change
* Avatar upload
* Preferences (toggles)
* Plan system

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

* Frontend: React
* Backend: Node.js
* Queue: Redis + BullMQ
* Storage: AWS S3
* Processing: FFmpeg
* Database: PostgreSQL

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

## Backend Structure (Node.js Modular Architecture)

```
backend/
  src/
    modules/
      auth/
        auth.controller.ts
        auth.service.ts
        auth.model.ts
        auth.routes.ts

      project/
        project.controller.ts
        project.service.ts
        project.model.ts
        project.routes.ts

      media/
        media.controller.ts
        media.service.ts
        media.model.ts
        media.routes.ts

      video/
        video.controller.ts
        video.service.ts
        video.model.ts
        video.routes.ts

      qr/
        qr.controller.ts
        qr.service.ts
        qr.model.ts
        qr.routes.ts

    queue/
      jobs/
        video.job.ts
      processors/
        video.processor.ts

    config/           # DB, env, app config
    middlewares/      # Auth, error handling
    validators/       # Request validation schemas
    utils/            # Helper utilities
```

### Rules

* Each module MUST be self-contained
* NO global controllers/services/models outside modules
* Business logic MUST be inside services
* Controllers MUST only handle request/response

---

## Shared Structure

```
shared/
  types/              # Shared types between frontend & backend
```

---

## Environment Configuration

```
.env                # Secrets (DO NOT COMMIT)
.env.example        # Example config
```

---

## Required Root Files

```
README.md           # Project documentation
package.json        # Dependencies
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
