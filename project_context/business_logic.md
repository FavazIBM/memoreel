# Business Logic & Domain Knowledge

---

## 1. Domain Overview

The system is a **memory video generation platform** where users create videos for occasions or memorials using uploaded media and predefined templates.

The system automates:

* Media organization
* Template-based video generation
* Public sharing via links and QR codes

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

**Definition**: Special project type for tribute.

**Purpose**: Create remembrance content.

**Rules**:

* Must include personName, deathDate
* Uses calm visual style
* Can be linked to QR

---

### QR Code

**Definition**: Scannable link to public project.

**Purpose**: Physical sharing (e.g., tombstone)

**Rules**:

* Generated only after publish
* Must be permanent
* Must map to public URL

---

## 3. Business Rules

### Project Rules

* Projects start in "draft"
* Cannot publish before video generation
* Only completed projects can be published

---

### Media Rules

* Media must be uploaded before generation
* orderIndex must be unique per project
* Media cannot be modified during processing

---

### Video Rules

* Generation must be asynchronous
* Only one generation job per project at a time
* Failed jobs must update project status

---

### Publish Rules

* Publish allowed ONLY if status = completed
* Publish generates:

  * public link
  * QR code

---

## 4. Workflows

### Project Creation Flow

1. User selects occasion
2. User enters title
3. Project created (status = draft)

---

### Media Upload Flow

1. User uploads media
2. System validates file
3. System stores file (S3)
4. System saves metadata with orderIndex

---

### Video Generation Flow

1. User clicks generate
2. System creates VideoJob
3. Status → processing
4. Queue processes job
5. FFmpeg generates video
6. Status → completed OR failed

---

### Publish Flow

1. User clicks publish
2. System checks status = completed
3. System generates slug
4. System generates QR
5. Status → published

---

## 5. Validation Rules

### Project

* title: required, min 3 chars
* type: must be valid occasion

---

### Media

* file type: JPG, PNG, MP4, MOV
* size limit enforced

---

### Memorial

* personName required
* deathDate required

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

## 10. Notifications

* Video processing started
* Video completed
* Video failed

---

## 11. Key Metrics

* Total projects created
* Video generation success rate
* Average processing time

---

## 12. AI Constraints (CRITICAL)

* DO NOT invent business rules
* DO NOT change workflows
* DO NOT skip validation
* FOLLOW architecture.md + api_documentation.md strictly

IF ANYTHING IS UNCLEAR:
→ STOP AND ASK USER