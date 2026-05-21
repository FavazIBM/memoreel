# Memoreel Quick Start Guide

## ✅ System Check

You have Java 21 installed - Perfect! ✓

## 🚀 Quick Setup (5 minutes)

### 1. Install Required Services

**PostgreSQL:**
```bash
# Download and install from: https://www.postgresql.org/download/windows/
# Or use Docker:
docker run -d --name memoreel-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:15
```

**Redis:**
```bash
# Using Docker (recommended for Windows):
docker run -d --name memoreel-redis -p 6379:6379 redis:7-alpine
```

**FFmpeg:**
```bash
# Download from: https://ffmpeg.org/download.html#build-windows
# Add to PATH or note the installation directory
```

### 2. Create Database

```bash
# Using psql (if installed locally)
psql -U postgres
CREATE DATABASE memoreel;
\q

# Or using Docker:
docker exec -it memoreel-postgres psql -U postgres -c "CREATE DATABASE memoreel;"
```

### 3. Configure Environment

```bash
# Copy the example file
copy .env.example .env

# Edit .env with your settings (minimum required):
# DATABASE_URL=postgresql://localhost:5432/memoreel
# DATABASE_USERNAME=postgres
# DATABASE_PASSWORD=postgres
# JWT_SECRET=change_this_to_a_random_secret_key
# FFMPEG_PATH=C:/path/to/ffmpeg.exe
```

### 4. Install Backend Dependencies

```bash
cd backend
mvn clean install
```

This will:
- Download all dependencies
- Run database migrations automatically
- Build the application

### 5. Install Frontend Dependencies

```bash
cd ..\frontend
npm install
```

### 6. Start the Application

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```

Wait for: `Started MemoreelApplication in X seconds`

**Terminal 2 - Frontend:**
```bash
cd frontend
npm run dev
```

### 7. Open Application

Navigate to: **http://localhost:3000**

## 🎉 You're Ready!

The application is now running:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api/v1
- **Health Check**: http://localhost:8080/api/v1/actuator/health

## 📝 Next Steps

1. **Register an account** at http://localhost:3000/register
2. **Create your first project** - Click "Create New Reel"
3. **Upload media** - Add photos or videos
4. **Generate video** - Let the system create your reel

## ⚠️ Common Issues

### Backend won't start?
```bash
# Check if PostgreSQL is running
docker ps | findstr postgres

# Check if Redis is running
docker ps | findstr redis

# Check Java version
java --version  # Should be 17 or higher ✓
```

### Frontend won't start?
```bash
# Check Node version
node --version  # Should be 20 or higher

# Clear cache and reinstall
cd frontend
rmdir /s /q node_modules
del package-lock.json
npm install
```

### Database connection error?
Check your `.env` file:
```env
DATABASE_URL=postgresql://localhost:5432/memoreel
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
```

## 🔧 Development Commands

**Backend:**
```bash
mvn spring-boot:run          # Start server
mvn test                     # Run tests
mvn clean package            # Build JAR
```

**Frontend:**
```bash
npm run dev                  # Start dev server
npm run build                # Production build
npm run lint                 # Check code quality
npm test                     # Run tests
```

## 📚 Full Documentation

For detailed setup and troubleshooting, see:
- [`SETUP.md`](SETUP.md) - Complete setup guide
- [`README.md`](README.md) - Project overview
- [`project_context/`](project_context/) - Technical documentation

## 🆘 Need Help?

1. Check [`SETUP.md`](SETUP.md) troubleshooting section
2. Review error logs in console
3. Verify all services are running
4. Check environment variables in `.env`

---

**Your Java Version**: 21.0.10 ✓ (Fully Compatible)

**Last Updated**: 2026-05-21