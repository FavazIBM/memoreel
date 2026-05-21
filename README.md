# Memoreel - Memory Reel Sharing Application

Memoreel is a web-based platform that enables users to create and share emotionally rich memory video reels for special occasions and memorials. The application automates video creation by combining user-uploaded media with predefined templates, background music, and transitions.

## 🎯 Key Features

- **User Account Management**: Secure registration and authentication with email/password and Google OAuth
- **Template-Based Video Creation**: Predefined templates for various occasions (birthdays, anniversaries, weddings, memorials)
- **Guided Creation Wizard**: Step-by-step process for selecting occasions, entering details, and uploading media
- **Automated Video Generation**: Backend video editor automatically creates reels using FFmpeg
- **Memorial System**: Special project type for tribute and remembrance with QR code generation
- **QR Code Sharing**: Generate scannable QR codes that link to published memorials
- **Project Management**: Organize incomplete projects and published reels
- **Public Sharing**: Share published reels via unique URLs and QR codes

## 🏗️ Architecture

### Frontend
- **Framework**: React 18 + TypeScript
- **Build Tool**: Vite 5
- **Styling**: Tailwind CSS 3
- **State Management**: Zustand 4
- **Data Fetching**: React Query 5
- **Routing**: React Router 6

### Backend
- **Framework**: Spring Boot 3.2 + Java 17
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Storage**: AWS S3
- **Video Processing**: FFmpeg 6
- **Queue**: Spring Batch + Quartz Scheduler
- **Authentication**: Spring Security + JWT

## 📁 Project Structure

```
memoreel/
├── frontend/          # React TypeScript application
├── backend/           # Spring Boot application
├── shared/            # Shared types and utilities
├── .env.example       # Environment variables template
└── README.md          # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java**: 17 or higher
- **Maven**: 3.9 or higher
- **Node.js**: 20 or higher
- **npm**: 10 or higher
- **PostgreSQL**: 15 or higher
- **Redis**: 7 or higher
- **FFmpeg**: 6 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/memoreel.git
   cd memoreel
   ```

2. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Install backend dependencies**
   ```bash
   cd backend
   mvn clean install
   ```

4. **Install frontend dependencies**
   ```bash
   cd ../frontend
   npm install
   ```

5. **Start services**
   ```bash
   # Start PostgreSQL (should be running as service)
   # Start Redis
   redis-server
   
   # Start backend (from backend directory)
   mvn spring-boot:run
   
   # Start frontend (from frontend directory)
   npm run dev
   ```

## 🔧 Development

### Backend Development
```bash
cd backend
mvn spring-boot:run
```

### Frontend Development
```bash
cd frontend
npm run dev
```

### Running Tests
```bash
# Backend tests
cd backend
mvn test

# Frontend tests
cd frontend
npm test
```

## 📚 Documentation

- [Architecture Documentation](project_context/architecture.md)
- [API Documentation](project_context/api_documentation.md)
- [Business Logic](project_context/business_logic.md)
- [Coding Standards](project_context/coding_standards.md)
- [Dependencies](project_context/dependencies.md)

## 🔐 Environment Variables

See `.env.example` for required environment variables:

- Database configuration (PostgreSQL)
- Redis configuration
- AWS S3 credentials
- JWT secret keys
- Google OAuth credentials
- FFmpeg path

## 🎨 User Journey

1. **Sign Up/Login**: User creates an account or logs in
2. **Select Occasion**: Choose from predefined occasion templates
3. **Enter Details**: Answer basic questions about the occasion
4. **Upload Media**: Upload photos and videos with drag-and-drop
5. **Generate Video**: Backend automatically creates the reel
6. **Preview**: Watch the generated video
7. **Recreate (Optional)**: Regenerate if changes are needed
8. **Publish**: Make the reel public and generate QR code
9. **Share**: Share via URL or QR code

## 🏷️ Version

**Current Version**: v1.0.0 (Initial Release)

## 📄 License

[Add your license here]

## 👥 Team

- **Project Lead**: [To be assigned]
- **Developers**: [To be assigned]

## 🤝 Contributing

[Add contribution guidelines]

---

**Keywords**: memoreel, memory reel, video sharing, memorial, occasion, celebration, QR code, automated video

**Last Updated**: 2026-05-21