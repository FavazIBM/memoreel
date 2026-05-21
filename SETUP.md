# Memoreel Setup Guide

This guide will help you set up and run the Memoreel application locally.

## Prerequisites

Before you begin, ensure you have the following installed:

### Required Software
- **Java**: Version 17 or higher
  ```bash
  java --version
  ```
- **Maven**: Version 3.9 or higher
  ```bash
  mvn --version
  ```
- **Node.js**: Version 20 or higher
  ```bash
  node --version
  ```
- **npm**: Version 10 or higher
  ```bash
  npm --version
  ```
- **PostgreSQL**: Version 15 or higher
- **Redis**: Version 7 or higher
- **FFmpeg**: Version 6 or higher
  ```bash
  ffmpeg -version
  ```

## Installation Steps

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/memoreel.git
cd memoreel
```

### 2. Set Up Environment Variables

Copy the example environment file and configure it:

```bash
cp .env.example .env
```

Edit `.env` and fill in your actual values:

```env
# Database
DATABASE_URL=postgresql://localhost:5432/memoreel
DATABASE_USERNAME=postgres
DATABASE_PASSWORD="password"

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# AWS S3 (required for media storage)
AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
AWS_S3_BUCKET_NAME=memoreel-media

# JWT
JWT_SECRET=your_super_secret_jwt_key_change_this

# Google OAuth (optional)
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# FFmpeg
FFMPEG_PATH=/usr/local/bin/ffmpeg
FFPROBE_PATH=/usr/local/bin/ffprobe
```

### 3. Set Up PostgreSQL Database

Create the database:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE memoreel;

# Exit psql
\q
```

### 4. Set Up Redis

Start Redis server:

```bash
# macOS
brew services start redis

# Ubuntu/Debian
sudo systemctl start redis

# Windows (using Docker)
docker run -d -p 6379:6379 redis:7-alpine
```

### 5. Install Backend Dependencies

```bash
cd backend
mvn clean install
```

This will:
- Download all Maven dependencies
- Run database migrations via Flyway
- Build the application

### 6. Install Frontend Dependencies

```bash
cd ../frontend
npm install
```

## Running the Application

### Development Mode

#### Start Backend Server

```bash
cd backend
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

#### Start Frontend Development Server

In a new terminal:

```bash
cd frontend
npm run dev
```

The frontend will start on `http://localhost:3000`

### Production Build

#### Build Backend

```bash
cd backend
mvn clean package
java -jar target/memoreel-backend-1.0.0.jar
```

#### Build Frontend

```bash
cd frontend
npm run build
```

The production build will be in the `frontend/dist` directory.

## Verification

### Check Backend Health

```bash
curl http://localhost:8080/api/v1/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

### Check Frontend

Open your browser and navigate to:
```
http://localhost:3000
```

You should see the Memoreel login page.

## Database Migrations

Flyway migrations run automatically on application startup. To manually check migration status:

```bash
cd backend
mvn flyway:info
```

## Troubleshooting

### FFmpeg Not Found

**Error**: `FFmpeg not found`

**Solution**:
```bash
# macOS
brew install ffmpeg

# Ubuntu/Debian
sudo apt-get install ffmpeg

# Verify installation
ffmpeg -version
```

### Redis Connection Failed

**Error**: `Could not connect to Redis`

**Solution**:
```bash
# Check if Redis is running
redis-cli ping
# Should return: PONG

# If not running, start it
brew services start redis  # macOS
sudo systemctl start redis # Linux
```

### PostgreSQL Connection Failed

**Error**: `Connection to database failed`

**Solution**:
1. Verify PostgreSQL is running:
   ```bash
   pg_isready
   ```

2. Check your DATABASE_URL in `.env`:
   ```env
   DATABASE_URL=postgresql://localhost:5432/memoreel
   ```

3. Ensure the database exists:
   ```bash
   psql -U postgres -l | grep memoreel
   ```

### Port Already in Use

**Error**: `Port 8080 already in use`

**Solution**:
```bash
# Find process using port
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Kill the process or change port in application.properties
```

### AWS S3 Credentials Invalid

**Error**: `AWS credentials are invalid`

**Solution**:
1. Verify your AWS credentials in `.env`
2. Ensure your IAM user has S3 permissions
3. Test credentials:
   ```bash
   aws s3 ls s3://your-bucket-name
   ```

## Testing

### Run Backend Tests

```bash
cd backend
mvn test
```

### Run Frontend Tests

```bash
cd frontend
npm test
```

## Development Tools

### Recommended VS Code Extensions

- Java Extension Pack
- Spring Boot Extension Pack
- ESLint
- Prettier
- Tailwind CSS IntelliSense
- TypeScript Vue Plugin (Volar)

### Database GUI Tools

- pgAdmin (PostgreSQL)
- Redis Commander (Redis)
- DBeaver (Universal)

## Next Steps

1. **Create your first account**: Navigate to `/register`
2. **Create a project**: Click "Create New Reel"
3. **Upload media**: Add photos or videos
4. **Generate video**: Let the system create your reel
5. **Publish**: Share your memorial or celebration

## Additional Resources

- [Architecture Documentation](project_context/architecture.md)
- [API Documentation](project_context/api_documentation.md)
- [Business Logic](project_context/business_logic.md)
- [Coding Standards](project_context/coding_standards.md)

## Support

For issues or questions:
- Check existing documentation
- Review troubleshooting section
- Contact the development team

---

**Last Updated**: 2026-05-21