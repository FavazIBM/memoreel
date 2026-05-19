# Dependencies & Tech Stack - Memoreel

## Technology Stack Overview

### Frontend
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| React | 18.x | UI Framework | https://react.dev |
| TypeScript | 5.x | Type Safety | https://www.typescriptlang.org |
| Vite | 5.x | Build Tool | https://vitejs.dev |
| React Router | 6.x | Routing | https://reactrouter.com |
| Tailwind CSS | 3.x | Styling | https://tailwindcss.com |
| React Query | 5.x | Data Fetching | https://tanstack.com/query |
| Zustand | 4.x | State Management | https://zustand-demo.pmnd.rs |
| Axios | 1.x | HTTP Client | https://axios-http.com |

### Backend
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| Java | 17+ (LTS) | Programming Language | https://www.oracle.com/java |
| Spring Boot | 3.2.x | Application Framework | https://spring.io/projects/spring-boot |
| Spring Data JPA | 3.2.x | Data Access | https://spring.io/projects/spring-data-jpa |
| Spring Security | 6.2.x | Security Framework | https://spring.io/projects/spring-security |
| PostgreSQL | 15.x | Primary Database | https://www.postgresql.org |
| Redis | 7.x | Caching & Sessions | https://redis.io |
| Spring Data Redis | 3.2.x | Redis Integration | https://spring.io/projects/spring-data-redis |

### Video Processing
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| FFmpeg | 6.x | Video Processing | https://ffmpeg.org |
| FFmpeg Java | 0.8.0 | FFmpeg Java Wrapper | https://github.com/bramp/ffmpeg-cli-wrapper |

### Storage & Cloud
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| AWS S3 | Latest | File Storage | https://aws.amazon.com/s3 |
| AWS SDK for Java | 2.21.x | AWS Integration | https://aws.amazon.com/sdk-for-java |

### Job Queue & Async Processing
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| Spring Batch | 5.1.x | Batch Processing | https://spring.io/projects/spring-batch |
| Spring Integration | 6.2.x | Message Processing | https://spring.io/projects/spring-integration |
| Quartz Scheduler | 2.3.x | Job Scheduling | http://www.quartz-scheduler.org |

### Authentication & Security
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| Spring Security OAuth2 | 6.2.x | OAuth2 Support | https://spring.io/projects/spring-security-oauth |
| JWT (jjwt) | 0.12.x | JWT Token | https://github.com/jwtk/jjwt |
| BCrypt | Built-in | Password Hashing | Spring Security |
| Google OAuth2 Client | Latest | Google Login | https://developers.google.com/identity |

### QR Code Generation
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| ZXing | 3.5.x | QR Code Generation | https://github.com/zxing/zxing |

### Build & Dependency Management
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| Maven | 3.9.x | Build Tool | https://maven.apache.org |
| Lombok | 1.18.x | Boilerplate Reduction | https://projectlombok.org |

### Testing
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| JUnit 5 | 5.10.x | Unit Testing | https://junit.org/junit5 |
| Mockito | 5.x | Mocking Framework | https://site.mockito.org |
| Spring Boot Test | 3.2.x | Integration Testing | https://spring.io/guides/gs/testing-web |
| TestContainers | 1.19.x | Container Testing | https://testcontainers.com |
| REST Assured | 5.4.x | API Testing | https://rest-assured.io |

### DevOps & Infrastructure
| Technology | Version | Purpose | Documentation |
|------------|---------|---------|---------------|
| Docker | 24.x | Containerization | https://www.docker.com |
| Docker Compose | 2.x | Multi-container | https://docs.docker.com/compose |
| GitHub Actions | Latest | CI/CD | https://github.com/features/actions |

## Package Dependencies

### Frontend Dependencies (package.json)
```json
{
  "name": "memoreel-frontend",
  "version": "1.0.0",
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.20.0",
    "axios": "^1.6.0",
    "@tanstack/react-query": "^5.12.0",
    "zustand": "^4.4.0",
    "react-dropzone": "^14.2.0",
    "react-player": "^2.13.0",
    "react-hot-toast": "^2.4.0",
    "lucide-react": "^0.294.0",
    "date-fns": "^2.30.0",
    "clsx": "^2.0.0",
    "tailwind-merge": "^2.1.0"
  },
  "devDependencies": {
    "@types/react": "^18.2.0",
    "@types/react-dom": "^18.2.0",
    "typescript": "^5.3.0",
    "vite": "^5.0.0",
    "@vitejs/plugin-react": "^4.2.0",
    "tailwindcss": "^3.3.0",
    "postcss": "^8.4.0",
    "autoprefixer": "^10.4.0",
    "eslint": "^8.55.0",
    "eslint-plugin-react": "^7.33.0",
    "@typescript-eslint/eslint-plugin": "^6.14.0",
    "@typescript-eslint/parser": "^6.14.0",
    "prettier": "^3.1.0",
    "prettier-plugin-tailwindcss": "^0.5.0"
  }
}
```

### Backend Dependencies (pom.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.memoreel</groupId>
    <artifactId>memoreel-backend</artifactId>
    <version>1.0.0</version>
    <name>Memoreel Backend</name>
    <description>Memory Reel Sharing Application Backend</description>
    
    <properties>
        <java.version>17</java.version>
        <lombok.version>1.18.30</lombok.version>
        <jjwt.version>0.12.3</jjwt.version>
        <zxing.version>3.5.2</zxing.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-batch</artifactId>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Security & JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- OAuth2 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-client</artifactId>
        </dependency>
        
        <!-- AWS S3 -->
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>s3</artifactId>
            <version>2.21.0</version>
        </dependency>
        
        <!-- QR Code Generation -->
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
            <version>${zxing.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
            <version>${zxing.version}</version>
        </dependency>
        
        <!-- FFmpeg Wrapper -->
        <dependency>
            <groupId>net.bramp.ffmpeg</groupId>
            <artifactId>ffmpeg</artifactId>
            <version>0.8.0</version>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- Quartz Scheduler -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-quartz</artifactId>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <version>1.19.3</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## Critical Dependencies

### Must-Have Libraries
| Package | Purpose | Why Critical | Alternative |
|---------|---------|--------------|-------------|
| FFmpeg | Video processing | Core functionality for video generation | None (industry standard) |
| Spring Batch | Job processing | Async video processing | Quartz only |
| Spring Data JPA | Database ORM | Type-safe database access | Hibernate directly, MyBatis |
| AWS S3 | File storage | Scalable media storage | Google Cloud Storage, Azure Blob |
| React Query | Data fetching | Efficient API state management | SWR, Apollo Client |
| bcrypt | Password hashing | Security requirement | argon2 |
| jsonwebtoken | Authentication | JWT token management | passport-jwt only |

### Security-Critical Packages
- **Spring Security**: Complete security framework - authentication, authorization, CSRF protection
- **BCrypt (Spring Security)**: Password hashing - built into Spring Security
- **jjwt**: JWT token generation and validation - required for API security
- **Spring Security OAuth2**: OAuth2 integration - Google login support
- **Spring Validation**: Input validation - prevents injection attacks

## Dependency Management

### Update Policy
- **Major versions**: Review breaking changes, test thoroughly before updating
- **Minor versions**: Update quarterly, test key features
- **Patch versions**: Update immediately for security fixes
- **Security audits**: Run weekly with `mvn dependency-check:check`

### Dependency Audit
```bash
# Frontend - Check for vulnerabilities
npm audit

# Backend - Check for vulnerabilities
mvn dependency-check:check

# Backend - Check for outdated dependencies
mvn versions:display-dependency-updates

# Backend - Update dependencies
mvn versions:use-latest-versions
```

### Lock Files
- **Frontend (npm)**: `package-lock.json` (commit to version control)
- **Backend (Maven)**: Dependencies locked in `pom.xml`

**Important**: Always commit lock files and pom.xml to ensure consistent installations

## Version Constraints

### Semantic Versioning
- `^1.2.3`: Compatible with 1.x.x (>=1.2.3 <2.0.0)
- `~1.2.3`: Compatible with 1.2.x (>=1.2.3 <1.3.0)
- `1.2.3`: Exact version only
- `*` or `latest`: Any version (NEVER use in production)

### Recommended Constraints for Memoreel
- **Frontend dependencies**: Use `^` for flexibility
- **Backend dependencies**: Use Maven version ranges sparingly
- **Critical dependencies** (Spring Boot, Spring Security): Use exact versions
- **Security packages**: Keep updated regularly

## Environment-Specific Dependencies

### Development Only
- **Frontend**: Testing frameworks (Jest), Linters (ESLint, Prettier), Build tools (Vite)
- **Backend**: Testing frameworks (JUnit, Mockito), Spring Boot DevTools, H2 Database (for testing)

### Production Only
- **Frontend**: Runtime dependencies, Core libraries (React)
- **Backend**: Spring Boot starters, PostgreSQL driver, Redis, AWS SDK, FFmpeg wrapper

## Third-Party Services

### External APIs
| Service | Purpose | API Key Required | Documentation |
|---------|---------|------------------|---------------|
| Google OAuth | Authentication | Yes | https://console.cloud.google.com |
| AWS S3 | File Storage | Yes | https://aws.amazon.com/s3 |
| AWS CloudFront | CDN (optional) | Yes | https://aws.amazon.com/cloudfront |

### SaaS Tools
| Tool | Purpose | Pricing | Integration |
|------|---------|---------|-------------|
| Sentry | Error Tracking | Free tier available | SDK integration |
| LogRocket | Session Replay | Paid | Frontend SDK |
| Datadog | Monitoring | Paid | Agent-based |

## Database Migrations

### Migration Tool
- **Tool**: Flyway (integrated with Spring Boot)
- **Location**: `/backend/src/main/resources/db/migration`
- **Naming**: `V{version}__{description}.sql` (e.g., `V1__create_users_table.sql`)

### Migration Strategy
1. Create migration SQL file in `db/migration` folder
2. Follow naming convention: `V{version}__{description}.sql`
3. Test in development environment
4. Flyway automatically applies on application startup
5. Apply to staging environment
6. Verify data integrity
7. Apply to production
8. Monitor for issues

### Alternative: Liquibase
Can also use Liquibase instead of Flyway:
```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

## Build Tools & Bundlers

### Frontend Build Configuration
- **Tool**: Vite
- **Config File**: `vite.config.ts`
- **Build Command**: `npm run build`
- **Dev Command**: `npm run dev`

### Frontend Output
- **Directory**: `/dist`
- **Format**: ESM
- **Optimization**: Minification, tree-shaking, code splitting
- **Assets**: Hashed filenames for caching

### Backend Build Configuration
- **Tool**: Maven
- **Config File**: `pom.xml`
- **Build Command**: `mvn clean package`
- **Dev Command**: `mvn spring-boot:run`
- **Test Command**: `mvn test`

### Backend Output
- **Directory**: `/target`
- **Format**: JAR file (executable)
- **Artifact**: `memoreel-backend-1.0.0.jar`

## Testing Dependencies

### Testing Stack
| Tool | Purpose | Version |
|------|---------|---------|
| JUnit 5 | Test Runner (Backend) | 5.10.x |
| Mockito | Mocking (Backend) | 5.x |
| Spring Boot Test | Integration Testing | 3.2.x |
| TestContainers | Container Testing | 1.19.x |
| REST Assured | API Testing | 5.4.x |
| Jest | Test Runner (Frontend) | 29.x |
| React Testing Library | Component Testing | 14.x |
| @testing-library/jest-dom | DOM Matchers | 6.x |

### Test Commands
```bash
# Backend unit tests
cd backend && mvn test

# Backend integration tests
cd backend && mvn verify

# Backend with coverage
cd backend && mvn test jacoco:report

# Frontend unit tests
cd frontend && npm test

# Frontend with coverage
cd frontend && npm run test:coverage

# E2E tests (future)
npm run test:e2e
```

## Code Quality Tools

### Linting & Formatting

**Frontend:**
- **ESLint**: JavaScript/TypeScript linting
- **Prettier**: Code formatting
- **Commands**: `npm run lint`, `npm run format`

**Backend:**
- **Checkstyle**: Java code style checking
- **SpotBugs**: Bug detection
- **SonarLint**: Code quality (IDE plugin)
- **Google Java Format**: Code formatting

### Maven Plugins for Code Quality
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.1</version>
</plugin>

<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.8.2.0</version>
</plugin>

<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
</plugin>
```

### Pre-commit Hooks
- **Frontend**: Husky + lint-staged
- **Backend**: Maven pre-commit plugin or Git hooks

## FFmpeg Installation

### Development
```bash
# macOS
brew install ffmpeg

# Ubuntu/Debian
sudo apt-get install ffmpeg

# Windows
# Download from https://ffmpeg.org/download.html
# Add to PATH
```

### Production (Docker)
```dockerfile
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache ffmpeg
COPY target/memoreel-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Redis Installation

### Development
```bash
# macOS
brew install redis
brew services start redis

# Ubuntu/Debian
sudo apt-get install redis-server
sudo systemctl start redis

# Windows
# Use Docker or WSL
docker run -d -p 6379:6379 redis:7-alpine
```

### Production
- Use managed Redis service (AWS ElastiCache, Redis Cloud)
- Or deploy Redis in Docker container

## PostgreSQL Installation

### Development
```bash
# macOS
brew install postgresql@15
brew services start postgresql@15

# Ubuntu/Debian
sudo apt-get install postgresql-15
sudo systemctl start postgresql

# Windows
# Download installer from postgresql.org
```

### Production
- Use managed PostgreSQL (AWS RDS, Supabase, Neon)
- Or deploy PostgreSQL in Docker container

## Deprecated Dependencies

### To Be Removed
| Package | Reason | Replacement | Timeline |
|---------|--------|-------------|----------|
| None yet | N/A | N/A | N/A |

## Dependency Conflicts

### Known Issues
| Package A | Package B | Issue | Resolution |
|-----------|-----------|-------|------------|
| None yet | N/A | N/A | N/A |

## Performance Considerations

### Bundle Size
- **Target frontend bundle size**: <500KB (gzipped)
- **Code splitting**: Route-based splitting
- **Lazy loading**: Heavy components (video player, QR generator)
- **Tree shaking**: Enabled via Vite

### Heavy Dependencies
| Package | Size | Why Needed | Optimization |
|---------|------|------------|--------------|
| FFmpeg | N/A (binary) | Video processing | Server-side only |
| React Player | ~100KB | Video playback | Lazy loaded |
| AWS SDK | ~200KB | S3 operations | Import only needed clients |

## License Compliance

### License Types Used
- **MIT**: React, most npm packages
- **Apache 2.0**: TypeScript, some AWS packages
- **BSD**: Some utility libraries
- **ISC**: Some npm packages

### License Restrictions
- All dependencies use permissive licenses
- No GPL or restrictive licenses
- Commercial use allowed

## Installation Instructions

### Prerequisites
```bash
# Java version
java --version  # Should be >= 17

# Maven version
mvn --version  # Should be >= 3.9.x

# Node.js version (for frontend)
node --version  # Should be >= 20.x
npm --version   # Should be >= 10.x

# Install FFmpeg
# See FFmpeg Installation section above

# Install Redis
# See Redis Installation section above

# Install PostgreSQL
# See PostgreSQL Installation section above
```

### Initial Setup
```bash
# Clone repository
git clone https://github.com/your-org/memoreel.git
cd memoreel

# Install backend dependencies
cd backend
npm install

# Setup environment variables
cp .env.example .env
# Edit .env with your configuration

# Run database migrations
# Flyway migrations run automatically on startup

# Build backend
mvn clean install

# Install frontend dependencies
cd ../frontend
npm install

# Setup environment variables
cp .env.example .env
# Edit .env with your configuration
```

### Development Setup
```bash
# Start Redis
redis-server

# Start PostgreSQL
# (should be running as service)

# Start backend (from backend directory)
mvn spring-boot:run

# Or run the JAR
java -jar target/memoreel-backend-1.0.0.jar

# Start frontend (from frontend directory)
npm run dev
```

## Troubleshooting

### Common Issues

**Issue**: FFmpeg not found
**Solution**: 
```bash
# Verify FFmpeg installation
ffmpeg -version

# Add to PATH if needed (Windows)
# Or reinstall using package manager
```

**Issue**: Maven build fails
**Solution**:
```bash
cd backend
mvn clean install
```

**Issue**: Redis connection failed
**Solution**:
```bash
# Check Redis is running
redis-cli ping
# Should return PONG

# Check Redis connection in .env
REDIS_URL=redis://localhost:6379
```

**Issue**: PostgreSQL connection failed
**Solution**:
```bash
# Check PostgreSQL is running
pg_isready

# Verify DATABASE_URL in .env
DATABASE_URL="postgresql://user:password@localhost:5432/memoreel"
```

**Issue**: Port already in use
**Solution**:
```bash
# Find process using port
lsof -i :3000  # macOS/Linux
netstat -ano | findstr :3000  # Windows

# Kill process or change port in .env
```

### Dependency Resolution
```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and lock file
rm -rf node_modules package-lock.json

# Reinstall
npm install

# If issues persist, try
npm ci  # Clean install from lock file
```

### Docker Issues
```bash
# Rebuild containers
docker-compose down
docker-compose build --no-cache
docker-compose up

# View logs
docker-compose logs -f

# Access container shell
docker-compose exec backend sh
```

---
**Keywords**: dependencies, packages, tech stack, libraries, tools, memoreel, ffmpeg, redis, postgresql
**Last Updated**: 2026-05-19