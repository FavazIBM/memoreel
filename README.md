# Memoreel - Memory Reel Sharing Application

Memoreel is a web-based platform that enables users to create and share emotionally rich memory video reels for special occasions and memorials. The application automates video creation by combining user-uploaded media with predefined templates, background music, and transitions.

## 🎯 Key Features

- **User Account Management**: Secure authentication with email/password and Google OAuth
- **Template-Based Video Creation**: Predefined templates for various occasions
- **Guided Creation Wizard**: Step-by-step process for creating memory reels
- **Automated Video Generation**: Backend video editor using FFmpeg
- **Memorial System**: Special project type for tributes with QR code generation
- **QR Code Sharing**: Generate scannable QR codes for published memorials
- **Project Management**: Organize incomplete projects and published reels
- **Privacy Controls**: Public, friends, and private visibility settings
- **Media Upload**: Support for images (JPG, PNG) and videos (MP4, MOV)
- **Public Sharing**: Share published reels via unique URLs

## 🏗️ Tech Stack

### Backend
- **Java 17** with **Spring Boot 3.2**
- **PostgreSQL 15** - Primary database
- **Redis 7** - Caching and session management
- **Spring Security** with JWT authentication
- **Spring Batch + Quartz** - Asynchronous job processing
- **FFmpeg** - Video processing
- **AWS S3** - Media storage
- **Maven** - Build tool

### Frontend
- **React 18** with **TypeScript 5**
- **Vite 5** - Build tool
- **Tailwind CSS 3** - Styling
- **React Router 6** - Routing
- **React Query 5** - Data fetching
- **Zustand 4** - State management
- **Axios** - HTTP client

## 📁 Project Structure

```
memoreel/
├── backend/                 # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/memoreel/
│   │   │   │   ├── auth/           # Authentication module
│   │   │   │   ├── project/        # Project management
│   │   │   │   ├── media/          # Media handling
│   │   │   │   ├── video/          # Video processing
│   │   │   │   ├── qr/             # QR code generation
│   │   │   │   ├── config/         # Configuration
│   │   │   │   ├── exception/      # Exception handling
│   │   │   │   └── util/           # Utilities
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── db/migration/   # Flyway migrations
│   │   └── test/
│   └── pom.xml
├── frontend/                # React frontend
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── pages/           # Page components
│   │   ├── layouts/         # Layout components
│   │   ├── services/        # API services
│   │   ├── store/           # State management
│   │   ├── hooks/           # Custom hooks
│   │   ├── utils/           # Utility functions
│   │   ├── types/           # TypeScript types
│   │   └── config/          # Configuration
│   ├── package.json
│   └── vite.config.ts
├── docker-compose.yml       # Local development setup
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
- **Node.js 20+** - [Download](https://nodejs.org/)
- **PostgreSQL 15+** - [Download](https://www.postgresql.org/download/)
- **Redis 7+** - [Download](https://redis.io/download/)
- **FFmpeg 6+** - [Download](https://ffmpeg.org/download.html)
- **Docker & Docker Compose** (optional) - [Download](https://www.docker.com/products/docker-desktop/)

### Installation

#### 1. Clone the repository

```bash
git clone https://github.com/your-org/memoreel.git
cd memoreel
```

#### 2. Setup with Docker (Recommended)

```bash
# Start PostgreSQL and Redis
docker-compose up -d

# Backend will connect to these services automatically
```

#### 3. Setup Backend

```bash
cd backend

# Copy environment file
cp .env.example .env

# Edit .env with your configuration
# Update database credentials, AWS S3 keys, JWT secret, etc.

# Install dependencies and build
mvn clean install

# Run the application
mvn spring-boot:run

# Backend will run on http://localhost:8080
```

#### 4. Setup Frontend

```bash
cd frontend

# Install dependencies
npm install

# Copy environment file
cp .env.example .env

# Edit .env with backend API URL

# Start development server
npm run dev

# Frontend will run on http://localhost:5173
```

### Manual Database Setup (Without Docker)

```bash
# Create PostgreSQL database
createdb memoreel

# Create Redis instance (if not using Docker)
redis-server

# Update backend/.env with your database credentials
DATABASE_URL=postgresql://localhost:5432/memoreel
DATABASE_USERNAME=your_username
DATABASE_PASSWORD=your_password
```

## 🧪 Running Tests

### Backend Tests

```bash
cd backend

# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run with coverage
mvn test jacoco:report
```

### Frontend Tests

```bash
cd frontend

# Run tests
npm test

# Run with coverage
npm run test:coverage
```

## 📦 Building for Production

### Backend

```bash
cd backend
mvn clean package

# JAR file will be in target/memoreel-backend-1.0.0.jar
java -jar target/memoreel-backend-1.0.0.jar
```

### Frontend

```bash
cd frontend
npm run build

# Build output will be in dist/
```

## 🔧 Configuration

### Backend Environment Variables

See `backend/.env.example` for all available configuration options:

- Database connection (PostgreSQL)
- Redis connection
- AWS S3 credentials
- JWT secret key
- Google OAuth credentials
- FFmpeg path
- Server port

### Frontend Environment Variables

See `frontend/.env.example` for configuration:

- API base URL
- Google OAuth client ID

## 📚 API Documentation

API documentation is available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html` (when backend is running)
- API Docs: See `project_context/api_documentation.md`

## 🗂️ Database Migrations

Database migrations are managed by Flyway and run automatically on application startup.

Migration files are located in: `backend/src/main/resources/db/migration/`

To create a new migration:
1. Create a new file: `V{version}__{description}.sql`
2. Add your SQL statements
3. Restart the application

## 🎨 Design System

UI designs are available in `project_context/Design/` directory.

## 📖 Documentation

- **Project Overview**: `project_context/project_overview.md`
- **Architecture**: `project_context/architecture.md`
- **API Documentation**: `project_context/api_documentation.md`
- **Business Logic**: `project_context/business_logic.md`
- **Coding Standards**: `project_context/coding_standards.md`
- **Dependencies**: `project_context/dependencies.md`

## 🤝 Contributing

1. Follow the coding standards in `project_context/coding_standards.md`
2. Write tests for new features
3. Update documentation as needed
4. Submit pull requests for review

## 📝 License

[Your License Here]

## 👥 Team

- Project Lead: [To be assigned]
- Developers: [To be assigned]

## 🐛 Known Issues

See GitHub Issues for current bugs and feature requests.

## 📞 Support

For support, email [support@memoreel.com] or open an issue on GitHub.

---

**Version**: 1.0.0  
**Last Updated**: 2026-05-20