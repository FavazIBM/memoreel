# Project Overview

## Project Name
**Memoreel** - Memory Reel Sharing Application

## Description
Memoreel is a web-based platform that enables users to create and share emotionally rich memory video reels for special occasions and memorials. The application automates video creation by combining user-uploaded media (photos and videos) with predefined templates, background music, and transitions to generate professional-quality memory reels.

## Key Features
- **User Account Management**: Secure registration and authentication with email/password and Google OAuth
- **Template-Based Video Creation**: Predefined templates for various occasions (birthdays, anniversaries, weddings, memorials, etc.)
- **Guided Creation Wizard**: Step-by-step process for selecting occasions, entering details, and uploading media
- **Automated Video Generation**: Backend video editor automatically creates reels from uploaded media using FFmpeg
- **Preview & Regeneration**: Users can preview generated videos and recreate them if needed
- **Memorial System**: Special project type for tribute and remembrance with QR code generation
- **QR Code Sharing**: Generate scannable QR codes that link to published memorials
- **Project Management**: Organize incomplete projects (My Projects) and published reels (Memorials)
- **Privacy Controls**: Public, friends, and private visibility settings (currently public only, others coming soon)
- **Media Upload**: Support for images (JPG, PNG) and videos (MP4, MOV) with drag-and-drop interface
- **Public Sharing**: Share published reels via unique URLs and QR codes

## Target Users
- **Primary Users**: Individuals wanting to create memory videos for special occasions
- **Use Cases**:
  - Celebrating birthdays, anniversaries, weddings, graduations
  - Creating memorial tributes for loved ones
  - Commemorating friendships, trips, and milestones
  - Sharing memories at events (via QR codes on physical items)

## Business Goals
- Simplify the process of creating professional memory videos
- Provide an emotional and meaningful way to preserve and share memories
- Enable physical-digital integration through QR codes for memorials
- Build a platform for occasion-based video content creation
- Automate video editing to make it accessible to non-technical users

## Project Status
- **Current Phase**: Planning/Initial Development
- **Version**: v1.0.0 (Initial Release)
- **Last Updated**: 2026-05-19

## Team & Roles
- **Project Lead**: [To be assigned]
- **Developers**: [To be assigned]
- **Stakeholders**: [To be assigned]

## Key Terminology
| Term | Definition |
|------|------------|
| **Reel** | A short memory video created from user-uploaded media |
| **Memorial** | A special type of reel created as a tribute to a deceased person |
| **Occasion** | The event type for which a reel is created (birthday, wedding, memorial, etc.) |
| **Template** | Predefined video styling with transitions, music, and text overlays |
| **Project** | A container for a reel in any state (draft, processing, completed, published) |
| **My Projects** | User's incomplete or draft projects |
| **Memorials** | Published reels accessible via public links |
| **QR Code** | Scannable code that links to a published memorial |
| **Media** | User-uploaded images and videos used in reel creation |
| **Privacy** | Visibility setting for published reels (public, friends, private) |

## User Journey
1. **Sign Up/Login**: User creates an account or logs in
2. **Select Occasion**: Choose from predefined occasion templates
3. **Enter Details**: Answer basic questions about the occasion (title, date, person name for memorials)
4. **Upload Media**: Upload photos and videos with drag-and-drop interface
5. **Generate Video**: Backend automatically creates the reel
6. **Preview**: Watch the generated video
7. **Recreate (Optional)**: Regenerate if changes are needed
8. **Publish**: Make the reel public and generate QR code
9. **Share**: Share via URL or QR code

## Core Workflows

### Celebration Reel Creation
1. Select celebration occasion (birthday, anniversary, wedding, etc.)
2. Enter title and occasion details
3. Upload celebration photos/videos
4. Generate video with vibrant template
5. Preview and publish
6. Share with friends and family

### Memorial Creation
1. Select memorial occasion
2. Enter deceased person's details (name, dates, description)
3. Upload memorial photos/videos
4. Generate video with calm, respectful template
5. Preview and publish
6. Generate QR code for physical placement (tombstone, memorial card)
7. Share memorial link

## Technical Highlights
- **Frontend**: React 18 + TypeScript single-page application
- **Backend**: Java 17 + Spring Boot 3.2 with modular architecture
- **Video Processing**: FFmpeg for automated video generation
- **Queue System**: Spring Batch + Quartz Scheduler for asynchronous video processing
- **Storage**: AWS S3 for media and video files
- **Database**: PostgreSQL 15 with Spring Data JPA
- **Cache**: Redis 7 for session management and caching
- **Authentication**: Spring Security with JWT (jjwt) and BCrypt password hashing
- **Build Tools**: Maven (Backend), Vite (Frontend)

## Related Documentation
- Architecture: See `architecture.md`
- API Docs: See `api_documentation.md`
- Business Logic: See `business_logic.md`
- Coding Standards: See `coding_standards.md`
- Dependencies: See `dependencies.md`

---
**Keywords**: memoreel, memory reel, video sharing, memorial, occasion, celebration, QR code, automated video
**Last Updated**: 2026-05-19