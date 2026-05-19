# Context Index - Quick Reference Guide for Memoreel

This file maps topics and keywords to relevant context files for fast lookup.

## How to Use This Index
When working on a task, search for relevant keywords below to find which context files contain the information you need.

---

## 📋 Quick Links
- [Project Overview](./project_overview.md) - High-level project information about Memoreel
- [Architecture](./architecture.md) - System design, user flows, and structure
- [Coding Standards](./coding_standards.md) - Code style and best practices
- [API Documentation](./api_documentation.md) - API endpoints and specifications
- [Business Logic](./business_logic.md) - Domain rules, workflows, and validations
- [Dependencies](./dependencies.md) - Tech stack, packages, and installation

---

## 🔍 Keyword to Context Mapping

### Memoreel Core Concepts
**Related Files**: 
- `project_overview.md` - Overview and terminology
- `architecture.md` - System architecture
- `business_logic.md` - Core business rules

**Keywords**: memoreel, memory reel, reel creation, occasion, memorial, celebration, video sharing, QR code

---

### Authentication & Authorization
**Related Files**: 
- `architecture.md` - Authentication architecture
- `api_documentation.md` - Auth endpoints
- `business_logic.md` - Auth rules and permissions
- `coding_standards.md` - Security best practices

**Keywords**: auth, login, register, JWT, OAuth, Google login, token, session, permissions, user ownership, access control

---

### Projects & Reels
**Related Files**:
- `architecture.md` - Project system architecture
- `business_logic.md` - Project lifecycle and rules
- `api_documentation.md` - Project endpoints
- `coding_standards.md` - Project-related code standards

**Keywords**: project, reel, draft, processing, completed, published, failed, status, state machine, My Projects, project creation

---

### Memorials
**Related Files**:
- `project_overview.md` - Memorial overview
- `architecture.md` - Memorial system design
- `business_logic.md` - Memorial-specific rules
- `api_documentation.md` - Memorial endpoints

**Keywords**: memorial, tribute, remembrance, deceased, death date, birth date, person name, memorial description, condolence, memorial page

---

### Occasions & Templates
**Related Files**:
- `architecture.md` - Occasion system and templates
- `business_logic.md` - Occasion-specific validation
- `api_documentation.md` - Occasion types

**Keywords**: occasion, template, birthday, anniversary, wedding, graduation, celebration, occasion selection, occasion-specific forms

---

### Media Upload & Management
**Related Files**:
- `architecture.md` - Media upload system
- `business_logic.md` - Media rules and validation
- `api_documentation.md` - Media endpoints
- `coding_standards.md` - File handling standards

**Keywords**: media, upload, image, video, photo, drag-and-drop, file validation, orderIndex, media sequence, JPG, PNG, MP4, MOV, file size, thumbnail

---

### Video Generation & Processing
**Related Files**:
- `architecture.md` - Video generation pipeline
- `business_logic.md` - Video processing workflow
- `api_documentation.md` - Video endpoints
- `dependencies.md` - FFmpeg and video tools

**Keywords**: video generation, FFmpeg, video processing, render, transitions, background music, text overlay, video job, queue, Spring Batch, Quartz, async processing

---

### QR Code System
**Related Files**:
- `architecture.md` - QR code system
- `business_logic.md` - QR code rules
- `api_documentation.md` - QR endpoints
- `dependencies.md` - QR code libraries

**Keywords**: QR code, QR generation, scannable, download QR, print QR, tombstone, memorial card, physical sharing, QR scan

---

### Publishing & Sharing
**Related Files**:
- `architecture.md` - Publish system
- `business_logic.md` - Publish workflow
- `api_documentation.md` - Publish endpoints

**Keywords**: publish, public link, slug, share, public URL, privacy, public memorial, published projects, Memorials section

---

### Privacy & Permissions
**Related Files**:
- `architecture.md` - Privacy system
- `business_logic.md` - Privacy rules
- `api_documentation.md` - Privacy endpoints

**Keywords**: privacy, public, private, friends, visibility, access control, authorization, ownership verification

---

### API & Endpoints
**Related Files**:
- `api_documentation.md` - Complete API reference
- `architecture.md` - API architecture pattern
- `coding_standards.md` - API coding standards

**Keywords**: REST, endpoints, routes, HTTP, request, response, API, error codes, validation, pagination

---

### Database & Data Models
**Related Files**:
- `architecture.md` - Database schema
- `dependencies.md` - Database tools (Spring Data JPA, PostgreSQL)
- `business_logic.md` - Data validation rules
- `api_documentation.md` - Data models

**Keywords**: database, PostgreSQL, Spring Data JPA, Hibernate, ORM, schema, Flyway migration, query, User, Project, Media, Video, QRCode, metadata

---

### Frontend & UI
**Related Files**:
- `architecture.md` - Frontend architecture and UI design
- `dependencies.md` - Frontend libraries (React, Tailwind)
- `coding_standards.md` - UI coding standards

**Keywords**: React, components, UI, frontend, styling, Tailwind CSS, dashboard, wizard, step indicator, drag-and-drop, preview

---

### Backend & Server
**Related Files**:
- `architecture.md` - Backend architecture
- `dependencies.md` - Backend frameworks (Java, Spring Boot)
- `api_documentation.md` - Server endpoints
- `coding_standards.md` - Backend coding standards

**Keywords**: Java, Spring Boot, Spring MVC, server, backend, REST controllers, services, repositories, modular architecture, Maven

---

### Queue & Async Processing
**Related Files**:
- `architecture.md` - Queue system
- `dependencies.md` - Queue tools (Spring Batch, Quartz, Redis)
- `business_logic.md` - Async workflows

**Keywords**: queue, Spring Batch, Quartz Scheduler, Redis, async, job processing, video job, background processing, retry logic, @Async

---

### Storage & Cloud
**Related Files**:
- `architecture.md` - Storage structure
- `dependencies.md` - Cloud services (AWS S3)
- `business_logic.md` - Storage rules

**Keywords**: storage, S3, AWS, cloud, file storage, media storage, video storage, CDN, bucket

---

### Testing
**Related Files**:
- `coding_standards.md` - Testing standards
- `dependencies.md` - Testing tools (Jest, Supertest)
- `architecture.md` - Testing strategy

**Keywords**: test, Jest, unit test, integration test, E2E, coverage, TDD, testing library, Supertest

---

### Security
**Related Files**:
- `coding_standards.md` - Security best practices
- `business_logic.md` - Security rules
- `api_documentation.md` - API security
- `architecture.md` - Security architecture
- `dependencies.md` - Security packages

**Keywords**: security, encryption, HTTPS, XSS, CSRF, SQL injection, validation, sanitization, bcrypt, JWT, helmet, rate limiting

---

### Deployment & DevOps
**Related Files**:
- `architecture.md` - Deployment architecture
- `dependencies.md` - DevOps tools (Docker, GitHub Actions)
- `coding_standards.md` - CI/CD practices

**Keywords**: Docker, Docker Compose, CI/CD, deployment, GitHub Actions, PM2, container, production

---

### Business Rules & Workflows
**Related Files**:
- `business_logic.md` - Complete business rules
- `api_documentation.md` - Business endpoints
- `project_overview.md` - Business goals

**Keywords**: workflow, validation, rules, permissions, state machine, calculations, business logic, edge cases

---

### Code Quality
**Related Files**:
- `coding_standards.md` - Complete standards
- `dependencies.md` - Quality tools (ESLint, Prettier)

**Keywords**: ESLint, Prettier, linting, formatting, code review, best practices, TypeScript, type safety

---

### Performance
**Related Files**:
- `architecture.md` - Performance architecture
- `coding_standards.md` - Performance guidelines
- `dependencies.md` - Performance tools

**Keywords**: optimization, caching, Redis, performance, scalability, load balancing, bundle size, lazy loading

---

### Error Handling
**Related Files**:
- `coding_standards.md` - Error handling standards
- `api_documentation.md` - Error responses and codes
- `business_logic.md` - Error scenarios

**Keywords**: error, exception, try-catch, logging, monitoring, debugging, error codes, AppError

---

## 📊 Context File Purposes

### project_overview.md
**Use When**: 
- Starting work on Memoreel
- Understanding project goals and features
- Onboarding new team members
- Need high-level context about memory reel sharing
- Understanding user journey

**Contains**:
- Memoreel description and purpose
- Key features (reel creation, memorials, QR codes)
- Target users and use cases
- Business goals
- Core terminology (reel, memorial, occasion, etc.)
- User journey from signup to sharing

---

### architecture.md
**Use When**:
- Designing new features
- Understanding system structure
- Making architectural decisions
- Integrating components
- Understanding user flows and wizard steps
- Implementing UI based on design images

**Contains**:
- Complete system architecture
- User flow (7-step wizard)
- Authentication system
- Dashboard and navigation
- Project state machine
- Occasion system with templates
- Media upload system
- Video generation pipeline
- Memorial system details
- QR code generation
- Public link system
- UI design specifications
- Storage structure

---

### coding_standards.md
**Use When**:
- Writing new code
- Reviewing code
- Setting up linters
- Establishing conventions
- Implementing Memoreel-specific features

**Contains**:
- Naming conventions (Memoreel-specific)
- Code formatting rules
- Best practices for React and Spring Boot
- Error handling patterns
- Security guidelines
- TypeScript type definitions
- Memorial-specific code standards
- State management patterns
- Video processing standards
- QR code generation standards

---

### api_documentation.md
**Use When**:
- Creating new endpoints
- Integrating with API
- Understanding data models
- Debugging API issues
- Implementing memorial or QR features

**Contains**:
- All API endpoints (auth, projects, media, video, publish, QR)
- Request/response formats
- Authentication methods
- Error codes (AUTH_*, PROJ_*, MEDIA_*, VIDEO_*, QR_*)
- Data models (Project, Media, Video, Memorial)
- Memorial-specific endpoints
- QR code endpoints
- Public viewer endpoints
- Validation rules

---

### business_logic.md
**Use When**:
- Implementing features
- Understanding workflows
- Validating data
- Making business decisions
- Handling edge cases
- Implementing memorial logic

**Contains**:
- Complete business rules
- Project lifecycle and state machine
- Memorial-specific rules
- QR code business logic
- Workflows (reel creation, memorial creation, regeneration)
- Validation rules (project, media, memorial, occasion-specific)
- Privacy rules
- Edge cases and error scenarios
- Notifications
- Key metrics

---

### dependencies.md
**Use When**:
- Adding new packages
- Updating dependencies
- Troubleshooting issues
- Understanding tech stack
- Setting up development environment
- Installing FFmpeg, Redis, PostgreSQL

**Contains**:
- Complete tech stack (React, Java, Spring Boot, FFmpeg, etc.)
- Package versions and purposes
- Installation instructions
- FFmpeg setup for video processing
- Redis setup for queue
- PostgreSQL setup
- AWS S3 configuration
- QR code library (qrcode package)
- Testing tools
- Build tools
- Troubleshooting guides

---

## 🎯 Common Task Scenarios

### Scenario: Creating a New Reel/Project
**Check These Files**:
1. `business_logic.md` - Project creation workflow
2. `architecture.md` - Wizard flow and occasion system
3. `api_documentation.md` - POST /projects endpoint
4. `coding_standards.md` - Project-related code standards

---

### Scenario: Implementing Memorial Features
**Check These Files**:
1. `business_logic.md` - Memorial-specific rules and validation
2. `architecture.md` - Memorial system design
3. `api_documentation.md` - Memorial endpoints and metadata
4. `coding_standards.md` - Memorial code standards

---

### Scenario: Adding Media Upload
**Check These Files**:
1. `architecture.md` - Media upload system and validation
2. `business_logic.md` - Media rules and orderIndex
3. `api_documentation.md` - POST /media/upload endpoint
4. `dependencies.md` - Multer, AWS S3 SDK
5. `coding_standards.md` - File handling standards

---

### Scenario: Implementing Video Generation
**Check These Files**:
1. `architecture.md` - Video generation pipeline
2. `business_logic.md` - Video generation workflow
3. `api_documentation.md` - POST /video/generate endpoint
4. `dependencies.md` - FFmpeg, Spring Batch setup
5. `coding_standards.md` - Video processing standards

---

### Scenario: Creating QR Code System
**Check These Files**:
1. `architecture.md` - QR code system design
2. `business_logic.md` - QR code rules and use cases
3. `api_documentation.md` - QR endpoints
4. `dependencies.md` - qrcode package
5. `coding_standards.md` - QR generation standards

---

### Scenario: Publishing a Project
**Check These Files**:
1. `business_logic.md` - Publish workflow and validation
2. `architecture.md` - Publish system and public links
3. `api_documentation.md` - POST /projects/:id/publish
4. `coding_standards.md` - State transition validation

---

### Scenario: Setting Up Authentication
**Check These Files**:
1. `business_logic.md` - Auth rules and workflows
2. `architecture.md` - Auth architecture (JWT, Google OAuth)
3. `api_documentation.md` - Auth endpoints
4. `dependencies.md` - Auth libraries (JWT, bcrypt, passport)
5. `coding_standards.md` - Security best practices

---

### Scenario: Creating Dashboard UI
**Check These Files**:
1. `architecture.md` - Dashboard system and UI design
2. `project_overview.md` - Dashboard features
3. `coding_standards.md` - React component standards
4. `dependencies.md` - Frontend libraries

---

### Scenario: Implementing Occasion Selection
**Check These Files**:
1. `architecture.md` - Occasion system and categories
2. `business_logic.md` - Occasion-specific validation
3. `api_documentation.md` - Occasion types
4. Design images in `Design/Create.png`

---

### Scenario: Fixing a Bug
**Check These Files**:
1. `coding_standards.md` - Error handling patterns
2. `business_logic.md` - Expected behavior and edge cases
3. `api_documentation.md` - Expected responses
4. `architecture.md` - System behavior

---

### Scenario: Setting Up Development Environment
**Check These Files**:
1. `dependencies.md` - Installation instructions
2. `architecture.md` - System requirements
3. `project_overview.md` - Project overview

---

## 🔄 Maintenance

### Updating Context Files
When you update any context file, remember to:
1. Update the "Last Updated" date in that file
2. Update this index if new keywords or topics are added
3. Ensure cross-references are still accurate
4. Update related files if changes affect multiple areas

### Adding New Context Files
If you add a new context file:
1. Add it to the Quick Links section
2. Add relevant keyword mappings
3. Add a purpose section
4. Update relevant scenarios
5. Update the date

---

## 💡 Tips for Effective Context Usage

1. **Start Broad**: Begin with `project_overview.md` for Memoreel context
2. **Get Specific**: Move to specialized files for implementation details
3. **Cross-Reference**: Use multiple files for comprehensive understanding
4. **Search Keywords**: Use Ctrl+F to find specific topics
5. **Keep Updated**: Regularly update context files as Memoreel evolves
6. **Follow Design**: Always refer to Design images for UI implementation
7. **Check State Machine**: Verify project status transitions in `business_logic.md`
8. **Validate**: Always check validation rules before implementing features

---

## 🎨 Design Reference

All UI implementations MUST follow the design images in:
`project_context/Design/`

- authentication_page.png
- Dashboard.png
- Create.png
- create a memorial 1.png & 2.png
- Upload.png
- output 1.png & 2.png
- QR.png
- account settings 1.png & 2.png

---

**Last Updated**: 2026-05-19