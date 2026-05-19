# API Documentation for Memoreel App

---

## API Overview

* **Base URL**: https://api.memoreel.app/v1
* **Protocol**: REST
* **Authentication**: JWT Bearer Token

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
  "title": "string (required, min 3 chars)",
  "type": "string (required, must be valid occasion type)"
}
```

### Response

```json
{
  "success": true,
  "data": {
    "id": "project_id",
    "title": "My Birthday",
    "status": "draft"
  }
}
```

---

## GET /projects

 Requires Authentication

### Query

```
page (optional)
limit (optional)
```

---

## GET /projects/:id

 Requires Authentication

### Rules

* User MUST own the project

---

## PUT /projects/:id/privacy

 Requires Authentication

### Body

```json
{
  "privacy": "public | private (required)"
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

* Supported: JPG, PNG, MP4, MOV
* Max size: 50MB

### Fields

* file (required)
* projectId (required)

---

## DELETE /media/:id

 Requires Authentication

### Rules

* Must belong to user

---

#  VIDEO

## POST /video/generate

 Requires Authentication

### Body

```json
{
  "projectId": "string (required)"
}
```

### State Transition

* draft → processing

---

## GET /video/:projectId

 Requires Authentication

---

#  PUBLISH

## POST /projects/:id/publish

 Requires Authentication

### Rules

* Allowed ONLY if status = completed

### Response

```json
{
  "success": true,
  "data": {
    "publicUrl": "https://memoreel.app/public/abc123",
    "qrCodeUrl": "https://..."
  }
}
```

### State Transition

* completed → published

---

# 🌐 PUBLIC

## GET /public/:slug

 Public Endpoint

### Rules

* slug MUST be unique
* slug generated at publish

---

#  QR CODE

## GET /qr/:projectId

 Requires Authentication

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

| Code      | Description         |
| --------- | ------------------- |
| AUTH_001  | Invalid credentials |
| AUTH_002  | Unauthorized        |
| VAL_001   | Validation error    |
| PROJ_001  | Project not found   |
| MEDIA_001 | Upload failed       |
| VIDEO_001 | Processing failed   |

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

#  AI CONSTRAINTS

* DO NOT invent endpoints
* DO NOT change request/response format
* DO NOT skip validation
* FOLLOW architecture.md strictly

IF ANYTHING IS UNCLEAR → STOP AND ASK
