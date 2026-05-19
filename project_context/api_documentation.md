# API Documentation for Memoreel App

---

## API Overview

* **Base URL**: https://api.memoreel.app/v1
* **Protocol**: REST
* **Authentication**: JWT Bearer Token
* **Content-Type**: application/json (except file uploads)
* **Rate Limit**: 100 requests per minute per user

---

## Authentication

### Header

```http
Authorization: Bearer <token>
```

---

## Response Format (GLOBAL)

### Success

```json
{
  "success": true,
  "data": {},
  "message": "Success"
}
```

### Error

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Error message"
  }
}
```

---

#  AUTH

## POST /auth/register

### Body

```json
{
  "email": "string (required, valid email)",
  "password": "string (required, min 6 chars)"
}
```

### Response

```json
{
  "success": true,
  "data": {
    "token": "jwt_token",
    "user": {
      "id": "user_id",
      "email": "user@example.com"
    }
  }
}
```

---

## POST /auth/login

### Body

```json
{
  "email": "string (required)",
  "password": "string (required)"
}
```

---

## POST /auth/google

### Body

```json
{
  "token": "google_oauth_token (required)"
}
```

---

#  USER

## PUT /user/profile

 Requires Authentication

### Body

```json
{
  "email": "string (optional)",
  "avatarUrl": "string (optional)"
}
```

---

## PUT /user/password

 Requires Authentication

### Body

```json
{
  "oldPassword": "string (required)",
  "newPassword": "string (required, min 6 chars)"
}
```

---

#  PROJECTS

## POST /projects

 Requires Authentication

### Body

```json
{
  "title": "string (required, min 3 chars, max 100 chars)",
  "type": "string (required, must be valid occasion type)",
  "metadata": {
    "personName": "string (required for memorial)",
    "birthDate": "date (optional)",
    "deathDate": "date (required for memorial)",
    "description": "string (required for memorial)",
    "profileImage": "string (optional, URL)",
    "relationship": "string (optional)",
    // Other occasion-specific fields
  }
}
```

### Response

```json
{
  "success": true,
  "data": {
    "id": "project_id",
    "title": "In Memory of John Doe",
    "type": "memorial",
    "status": "draft",
    "createdAt": "2026-05-19T10:00:00Z"
  }
}
```

---

## GET /projects

 Requires Authentication

### Query Parameters

```
page (optional, default: 1)
limit (optional, default: 10, max: 50)
status (optional, filter: draft|processing|completed|published|failed)
type (optional, filter by occasion type)
sort (optional, values: createdAt|updatedAt|title, default: createdAt)
order (optional, values: asc|desc, default: desc)
```

### Response

```json
{
  "success": true,
  "data": {
    "projects": [
      {
        "id": "project_id",
        "title": "My Birthday",
        "type": "birthday",
        "status": "completed",
        "thumbnail": "url",
        "createdAt": "2026-05-19T10:00:00Z",
        "updatedAt": "2026-05-19T11:00:00Z"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 10,
      "total": 25,
      "totalPages": 3
    }
  }
}
```

---

## GET /projects/:id

 Requires Authentication

### Rules

* User MUST own the project

### Response

```json
{
  "success": true,
  "data": {
    "id": "project_id",
    "title": "In Memory of John Doe",
    "type": "memorial",
    "status": "completed",
    "privacy": "public",
    "metadata": {
      "personName": "John Doe",
      "birthDate": "1950-01-01",
      "deathDate": "2026-05-01",
      "description": "A loving father and friend..."
    },
    "media": [
      {
        "id": "media_id",
        "type": "image",
        "url": "s3_url",
        "orderIndex": 1
      }
    ],
    "video": {
      "url": "video_url",
      "duration": 180,
      "createdAt": "2026-05-19T11:00:00Z"
    },
    "createdAt": "2026-05-19T10:00:00Z",
    "updatedAt": "2026-05-19T11:00:00Z"
  }
}
```

---

## PUT /projects/:id

 Requires Authentication

### Body

```json
{
  "title": "string (optional)",
  "metadata": {
    // Occasion-specific fields to update
  }
}
```

### Rules

* Can only update draft projects
* Cannot update during processing

---

## DELETE /projects/:id

 Requires Authentication

### Rules

* Can only delete draft or failed projects
* Cannot delete published projects

---

## PUT /projects/:id/privacy

 Requires Authentication

### Body

```json
{
  "privacy": "public | friends | private (required)"
}
```

### Response

```json
{
  "success": true,
  "data": {
    "id": "project_id",
    "privacy": "public"
  }
}
```

---

#  MEDIA

## POST /media/upload

 Requires Authentication

### Content-Type

```http
multipart/form-data
```

### Rules

* Supported formats: JPG, PNG, MP4, MOV
* Max size: Images 5MB, Videos 50MB
* MIME type validation required
* Virus scanning recommended

### Fields

* file (required, binary)
* projectId (required, string)
* orderIndex (optional, integer, auto-assigned if not provided)

### Response

```json
{
  "success": true,
  "data": {
    "id": "media_id",
    "projectId": "project_id",
    "type": "image",
    "url": "s3_url",
    "thumbnail": "thumbnail_url",
    "orderIndex": 1,
    "size": 2048576,
    "uploadedAt": "2026-05-19T10:30:00Z"
  }
}
```

### Error Response

```json
{
  "success": false,
  "error": {
    "code": "MEDIA_001",
    "message": "File size exceeds limit",
    "details": {
      "maxSize": "5MB",
      "actualSize": "7MB"
    }
  }
}
```

---

## GET /media/:projectId

 Requires Authentication

### Response

```json
{
  "success": true,
  "data": {
    "media": [
      {
        "id": "media_id",
        "type": "image",
        "url": "s3_url",
        "thumbnail": "thumbnail_url",
        "orderIndex": 1,
        "size": 2048576
      }
    ]
  }
}
```

---

## PUT /media/:id/reorder

 Requires Authentication

### Body

```json
{
  "orderIndex": "integer (required)"
}
```

### Rules

* Can only reorder media in draft projects
* orderIndex must be unique within project

---

## DELETE /media/:id

 Requires Authentication

### Rules

* Must belong to user's project
* Cannot delete during video processing
* Deletes file from S3

### Response

```json
{
  "success": true,
  "message": "Media deleted successfully"
}
```

---

#  VIDEO

## POST /video/generate

 Requires Authentication

### Body

```json
{
  "projectId": "string (required)",
  "templateId": "string (optional, auto-selected if not provided)"
}
```

### Rules

* Project must be in "draft" or "failed" state
* Project must have at least 1 media item
* Only one generation job per project at a time

### State Transition

* draft → processing
* failed → processing

### Response

```json
{
  "success": true,
  "data": {
    "jobId": "video_job_id",
    "projectId": "project_id",
    "status": "queued",
    "estimatedTime": 180
  }
}
```

---

## GET /video/status/:jobId

 Requires Authentication

### Response

```json
{
  "success": true,
  "data": {
    "jobId": "video_job_id",
    "projectId": "project_id",
    "status": "processing",
    "progress": 45,
    "startedAt": "2026-05-19T10:00:00Z",
    "estimatedCompletion": "2026-05-19T10:03:00Z"
  }
}
```

---

## GET /video/:projectId

 Requires Authentication

### Response

```json
{
  "success": true,
  "data": {
    "videoUrl": "s3_video_url",
    "duration": 180,
    "resolution": "1080p",
    "size": 52428800,
    "createdAt": "2026-05-19T10:03:00Z"
  }
}
```

---

## POST /video/regenerate

 Requires Authentication

### Body

```json
{
  "projectId": "string (required)",
  "reason": "string (optional)"
}
```

### Rules

* Project must be in "completed" or "failed" state
* Previous video will be replaced

---

#  PUBLISH

## POST /projects/:id/publish

 Requires Authentication

### Rules

* Allowed ONLY if status = completed
* Project must have generated video
* User must own the project
* Action is permanent (cannot unpublish)

### Body

```json
{
  "privacy": "public | friends | private (optional, default: public)"
}
```

### Response

```json
{
  "success": true,
  "data": {
    "projectId": "project_id",
    "publicUrl": "https://memoreel.app/m/abc123xyz",
    "slug": "abc123xyz",
    "qrCodeUrl": "https://memoreel.app/qr/abc123xyz.png",
    "publishedAt": "2026-05-19T12:00:00Z"
  }
}
```

### State Transition

* completed → published

### Error Response

```json
{
  "success": false,
  "error": {
    "code": "PROJ_002",
    "message": "Cannot publish incomplete project",
    "details": {
      "currentStatus": "processing",
      "requiredStatus": "completed"
    }
  }
}
```

---

# 🌐 PUBLIC

## GET /public/:slug

 Public Endpoint (No Authentication Required)

### Rules

* slug MUST be unique
* slug generated at publish
* Accessible without login
* Privacy setting enforced

### Response

```json
{
  "success": true,
  "data": {
    "id": "project_id",
    "title": "In Memory of John Doe",
    "type": "memorial",
    "author": {
      "name": "Jane Doe",
      "avatar": "avatar_url"
    },
    "metadata": {
      "personName": "John Doe",
      "birthDate": "1950-01-01",
      "deathDate": "2026-05-01",
      "description": "A loving father..."
    },
    "video": {
      "url": "video_url",
      "duration": 180,
      "thumbnail": "thumbnail_url"
    },
    "media": [
      {
        "thumbnail": "thumbnail_url",
        "orderIndex": 1
      }
    ],
    "publishedAt": "2026-05-19T12:00:00Z",
    "views": 150
  }
}
```

### Error Response (Private Project)

```json
{
  "success": false,
  "error": {
    "code": "AUTH_003",
    "message": "This memorial is private"
  }
}
```

---

#  QR CODE

## GET /qr/:projectId

 Requires Authentication

### Rules

* Project must be published
* User must own the project

### Response

```json
{
  "success": true,
  "data": {
    "qrCodeUrl": "https://memoreel.app/qr/abc123xyz.png",
    "publicUrl": "https://memoreel.app/m/abc123xyz",
    "downloadUrl": "https://memoreel.app/qr/abc123xyz/download",
    "size": "512x512",
    "format": "PNG"
  }
}
```

---

## GET /qr/:slug/download

 Public Endpoint

### Description

Download high-resolution QR code for printing

### Response

* Content-Type: image/png
* Binary PNG file (512x512px)
* High error correction level

---

## POST /qr/regenerate/:projectId

 Requires Authentication

### Rules

* Project must be published
* Generates new QR code (same URL)

### Response

```json
{
  "success": true,
  "data": {
    "qrCodeUrl": "https://memoreel.app/qr/abc123xyz.png",
    "regeneratedAt": "2026-05-19T13:00:00Z"
  }
}
```

---

#  STATE FLOW (STRICT)

```text
draft → processing → completed → published
processing failure → failed
```

---

#  PAGINATION

```http
GET /projects?page=1&limit=10
```

---

#  VALIDATION RULES

* All inputs MUST be validated
* Reject invalid formats
* Reject missing required fields

---

#  AUTHORIZATION RULES

* Users can ONLY access their own resources
* Public endpoints are read-only

---

#  ERROR CODES

| Code      | Description                      | HTTP Status |
| --------- | -------------------------------- | ----------- |
| AUTH_001  | Invalid credentials              | 401         |
| AUTH_002  | Unauthorized access              | 403         |
| AUTH_003  | Private content                  | 403         |
| VAL_001   | Validation error                 | 400         |
| VAL_002   | Invalid file format              | 400         |
| VAL_003   | File size exceeds limit          | 400         |
| PROJ_001  | Project not found                | 404         |
| PROJ_002  | Cannot publish incomplete project| 400         |
| PROJ_003  | Project already published        | 400         |
| MEDIA_001 | Upload failed                    | 500         |
| MEDIA_002 | Media not found                  | 404         |
| MEDIA_003 | Cannot modify during processing  | 400         |
| VIDEO_001 | Processing failed                | 500         |
| VIDEO_002 | Generation already in progress   | 400         |
| VIDEO_003 | No media uploaded                | 400         |
| QR_001    | QR generation failed             | 500         |
| QR_002    | Project not published            | 400         |
| RATE_001  | Rate limit exceeded              | 429         |

---

#  RATE LIMIT

* 100 requests per minute

---

#  DATA MODELS

## Project

```ts
interface Project {
  id: string;
  title: string;
  type: string;
  status: "draft" | "processing" | "completed" | "published";
  privacy: "public" | "private";
}
```

---

## Media

```ts
interface Media {
  id: string;
  projectId: string;
  type: "image" | "video";
  url: string;
  orderIndex: number;
}
```

---

## VideoJob

```ts
interface VideoJob {
  id: string;
  projectId: string;
  status: "queued" | "processing" | "completed" | "failed";
}
```

---

# 📊 WEBHOOKS (Future Feature)

## POST /webhooks/video-completed

Webhook called when video generation completes

### Payload

```json
{
  "event": "video.completed",
  "projectId": "project_id",
  "videoUrl": "s3_url",
  "timestamp": "2026-05-19T10:03:00Z"
}
```

---

#  STATISTICS

## GET /stats/user

 Requires Authentication

### Response

```json
{
  "success": true,
  "data": {
    "totalProjects": 15,
    "completedProjects": 10,
    "publishedProjects": 8,
    "totalViews": 1250,
    "storageUsed": 2048576000,
    "storageLimit": 5368709120
  }
}
```

---

## GET /stats/project/:id

 Requires Authentication

### Response

```json
{
  "success": true,
  "data": {
    "views": 150,
    "qrScans": 25,
    "shares": 10,
    "lastViewed": "2026-05-19T14:00:00Z"
  }
}
```

---

#  AI CONSTRAINTS

* DO NOT invent endpoints
* DO NOT change request/response format
* DO NOT skip validation
* FOLLOW architecture.md strictly
* Memorial endpoints must follow memorial-specific rules

IF ANYTHING IS UNCLEAR → STOP AND ASK

---

**Last Updated**: 2026-05-19
