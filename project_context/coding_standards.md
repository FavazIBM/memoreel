# Coding Standards & Best Practices - Memoreel

## General Principles
- Write clean, readable, and maintainable code
- Follow DRY (Don't Repeat Yourself) principle
- Keep functions small and focused (Single Responsibility)
- Write self-documenting code with clear naming
- Prioritize code clarity over cleverness
- Follow the architecture patterns defined in architecture.md
- Respect the state machine for project status transitions

## Naming Conventions

### Variables
- **Style**: camelCase
- **Boolean prefix**: is, has, should, can
- **Examples**:
  ```javascript
  isPublished, hasMedia, shouldRegenerate, canPublish
  projectStatus, memorialData, qrCodeUrl
  ```

### Functions/Methods
- **Style**: camelCase
- **Verb-based names**: get, set, create, update, delete, fetch, validate, generate, publish
- **Examples**:
  ```javascript
  getUserProjects(), createMemorial(), validateMedia()
  generateVideo(), publishProject(), downloadQRCode()
  ```

### Classes/Interfaces
- **Style**: PascalCase
- **Examples**:
  ```typescript
  ProjectService, MediaController, VideoProcessor
  IProjectRepository, IAuthService, IQRGenerator
  MemorialMetadata, OccasionType
  ```

### Constants
- **Style**: UPPER_SNAKE_CASE
- **Examples**:
  ```javascript
  MAX_FILE_SIZE, VIDEO_PROCESSING_TIMEOUT
  SUPPORTED_IMAGE_FORMATS, SUPPORTED_VIDEO_FORMATS
  QR_CODE_SIZE, DEFAULT_PRIVACY_SETTING
  ```

### Files
- **Style**: kebab-case for modules, PascalCase for components
- **Examples**:
  ```
  Backend: project.service.ts, media.controller.ts, video.processor.ts
  Frontend: ProjectCard.tsx, MediaUpload.tsx, QRCodeDisplay.tsx
  ```

### Memoreel-Specific Naming
- **Projects**: Use "project" not "reel" in code (reel is user-facing term)
- **Memorials**: Use "memorial" for memorial-type projects
- **Status**: Use exact state names: draft, processing, completed, published, failed
- **Occasions**: Use lowercase with underscores: birthday, memorial, wedding

## Code Formatting

### Indentation
- **Spaces**: 2 spaces for JavaScript/TypeScript
- **No tabs**: Use spaces for consistency
- Configure editor to convert tabs to spaces

### Line Length
- **Maximum**: 100 characters
- Break long lines appropriately
- Use prettier for automatic formatting

### Braces
```javascript
// Preferred style
if (condition) {
  // code
} else {
  // code
}

// Function style
function example() {
  // code
}
```

## Comments & Documentation

### When to Comment
- Complex algorithms or business logic
- Non-obvious decisions or workarounds
- Public APIs and interfaces
- TODO/FIXME items with context

### Comment Style
```javascript
// Single-line comment for brief explanations

/**
 * Multi-line JSDoc comment for functions
 * @param {string} userId - The user identifier
 * @returns {Promise<User>} The user object
 */
function getUser(userId) {
  // Implementation
}
```

### Avoid
- Obvious comments that restate the code
- Commented-out code (use version control)
- Outdated or misleading comments

## Error Handling

### Principles
- Always handle errors explicitly
- Use try-catch for async operations
- Provide meaningful error messages
- Log errors with context
- Use error codes from api_documentation.md
- Never expose internal errors to users

### Example
```javascript
try {
  const result = await generateVideo(projectId);
  return result;
} catch (error) {
  logger.error('Video generation failed', {
    projectId,
    error: error.message,
    stack: error.stack
  });
  
  throw new AppError(
    'VIDEO_001',
    'Failed to generate video. Please try again.',
    500,
    { projectId }
  );
}
```

### Memoreel-Specific Error Handling

**Project State Validation:**
```javascript
if (project.status !== 'completed') {
  throw new AppError(
    'PROJ_002',
    'Cannot publish incomplete project',
    400,
    { currentStatus: project.status }
  );
}
```

**Media Validation:**
```javascript
if (file.size > MAX_FILE_SIZE) {
  throw new AppError(
    'VAL_003',
    'File size exceeds limit',
    400,
    { maxSize: MAX_FILE_SIZE, actualSize: file.size }
  );
}
```

## Testing Standards

### Test Coverage
- **Minimum coverage**: [e.g., 80%]
- **Critical paths**: [100% coverage]

### Test Naming
```javascript
describe('UserService', () => {
  describe('createUser', () => {
    it('should create user with valid data', () => {});
    it('should throw error when email is invalid', () => {});
  });
});
```

### Test Structure
- **Arrange**: Set up test data
- **Act**: Execute the function
- **Assert**: Verify the result

## Code Review Guidelines

### Before Submitting
- [ ] Code follows style guide
- [ ] Tests are written and passing
- [ ] Documentation is updated
- [ ] No console.logs or debug code
- [ ] No commented-out code

### Review Checklist
- [ ] Logic is correct and efficient
- [ ] Error handling is appropriate
- [ ] Security considerations addressed
- [ ] Performance implications considered
- [ ] Code is maintainable

## Security Best Practices

### Input Validation
- Validate all user inputs on both client and server
- Sanitize file names and paths
- Use parameterized queries for database operations
- Validate MIME types for uploaded files
### Java/Spring Boot (Backend)

#### General Java Standards
- Use Java 17+ features (records, sealed classes, pattern matching)
- Follow Oracle Java Code Conventions
- Use meaningful variable and method names
- Keep methods small (max 20-30 lines)
- One class per file
- Use proper access modifiers (private by default)

#### Spring Boot Conventions
```java
// Controller naming
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    // Use constructor injection
    private final ProjectService projectService;
    
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
}

// Service naming
@Service
public class ProjectService {
    // Business logic here
}

// Repository naming
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom queries
}
```

#### Entity Definitions
```java
@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OccasionType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;
    
    @Enumerated(EnumType.STRING)
    private Privacy privacy;
    
    @Column(columnDefinition = "jsonb")
    private String metadata;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

#### Enum Definitions
```java
public enum ProjectStatus {
    DRAFT,
    PROCESSING,
    COMPLETED,
    PUBLISHED,
    FAILED
}

public enum OccasionType {
    BIRTHDAY,
    ANNIVERSARY,
    MEMORIAL,
    WEDDING,
    GRADUATION,
    CUSTOM
}

public enum Privacy {
    PUBLIC,
    FRIENDS,
    PRIVATE
}
```

#### DTO Pattern
```java
@Data
@Builder
public class ProjectDTO {
    private Long id;
    private String title;
    private OccasionType type;
    private ProjectStatus status;
    private Privacy privacy;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
}
```

#### Exception Handling
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProjectNotFound(
            ProjectNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .code("PROJ_001")
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

// Custom exceptions
public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long id) {
        super("Project not found with id: " + id);
    }
}
```

#### Validation
```java
@Data
public class CreateProjectRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @NotNull(message = "Occasion type is required")
    private OccasionType type;
    
    private Map<String, Object> metadata;
}
```

- Check file sizes before processing

### Authentication & Authorization
- Never store passwords in plain text (use bcrypt)
- Use JWT with secure secret keys
- Implement token expiration and refresh
- Verify user ownership for all project operations
- Check project status before allowing operations

### Sensitive Data
- Never commit secrets to version control
- Use environment variables for all secrets
- Encrypt sensitive memorial data if needed
- Secure S3 bucket access with proper IAM roles
- Use HTTPS for all API communications

### Memoreel-Specific Security

**Project Access Control:**
```javascript
// Always verify ownership
if (project.userId !== req.user.id) {
  throw new AppError('AUTH_002', 'Unauthorized access', 403);
}
```

**Privacy Enforcement:**
```javascript
// Check privacy settings for public access
if (project.privacy === 'private' && !req.user) {
  throw new AppError('AUTH_003', 'This memorial is private', 403);
}
```

**File Upload Security:**
```javascript
// Validate file type and size
const allowedTypes = ['image/jpeg', 'image/png', 'video/mp4'];
if (!allowedTypes.includes(file.mimetype)) {
  throw new AppError('VAL_002', 'Invalid file format', 400);
}
```

## Performance Guidelines

### Optimization
- Avoid premature optimization
- Profile before optimizing
- Cache expensive operations
- Use appropriate data structures

### Database
- Use indexes appropriately
- Avoid N+1 queries
- Batch operations when possible
- Use pagination for large datasets

## Version Control

### Commit Messages
```
type(scope): subject

body (optional)

footer (optional)
```

**Types**: feat, fix, docs, style, refactor, test, chore

**Example**:
```
feat(auth): add JWT token refresh mechanism

Implemented automatic token refresh to improve user experience
and reduce login frequency.

Closes #123
```

### Branch Naming
- **Feature**: `feature/user-authentication`
- **Bug Fix**: `fix/login-error`
- **Hotfix**: `hotfix/critical-security-patch`

## Language-Specific Standards

### JavaScript/TypeScript
- Use `const` by default, `let` when needed, avoid `var`
- Prefer arrow functions for callbacks
- Use async/await over promise chains
- Enable strict mode in TypeScript
- Use TypeScript interfaces for data models
- Avoid `any` type - use proper types

### TypeScript Type Definitions

**Project Types:**
```typescript
type ProjectStatus = 'draft' | 'processing' | 'completed' | 'published' | 'failed';
type OccasionType = 'birthday' | 'anniversary' | 'memorial' | 'wedding' | 'custom';
type Privacy = 'public' | 'friends' | 'private';

interface Project {
  id: string;
  userId: string;
  title: string;
  type: OccasionType;
  status: ProjectStatus;
  privacy: Privacy;
  metadata: Record<string, any>;
  createdAt: Date;
  updatedAt: Date;
}
```

**Memorial Types:**
```typescript
interface MemorialMetadata {
  personName: string;
  birthDate?: Date;
  deathDate: Date;
  description: string;
  profileImage?: string;
  relationship?: string;
}
```

### React/Frontend Standards
- Use functional components with hooks
- Keep components small and focused
- Use custom hooks for reusable logic
- Implement proper error boundaries
- Use React Query for API calls
- Implement loading and error states

**Component Example:**
```typescript
const ProjectCard: React.FC<{ project: Project }> = ({ project }) => {
  const { mutate: publishProject } = usePublishProject();
  
  const handlePublish = () => {
    if (project.status !== 'completed') {
      toast.error('Cannot publish incomplete project');
      return;
    }
    publishProject(project.id);
  };
  
  return (
    <Card>
      <CardTitle>{project.title}</CardTitle>
      <StatusBadge status={project.status} />
      {project.status === 'completed' && (
        <Button onClick={handlePublish}>Publish</Button>
      )}
    </Card>
  );
};
```

## Tools & Linters

### Required Tools
- **Linter**: ESLint with TypeScript support
- **Formatter**: Prettier
- **Pre-commit hooks**: Husky + lint-staged
- **Type Checker**: TypeScript compiler

### ESLint Configuration
```json
{
  "extends": [
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:react/recommended",
    "prettier"
  ],
  "rules": {
    "no-console": "warn",
    "@typescript-eslint/no-explicit-any": "error",
    "@typescript-eslint/explicit-function-return-type": "warn"
  }
}
```

### Prettier Configuration
```json
{
  "semi": true,
  "trailingComma": "es5",
  "singleQuote": true,
  "printWidth": 100,
  "tabWidth": 2
}
```

## Memoreel-Specific Best Practices

### State Management
- Use proper state machine for project status
- Never skip status transitions
- Validate state before operations

### Video Processing
- Always use queue for video generation
- Implement proper retry logic
- Log all processing steps
- Handle FFmpeg errors gracefully

### QR Code Generation
- Generate QR codes asynchronously
- Store QR codes in S3
- Implement retry logic for failures
- Use high error correction level

### Media Handling
- Validate files before upload
- Generate thumbnails for images
- Store orderIndex with media
- Implement proper cleanup on delete

### Memorial Handling
- Validate required memorial fields
- Use respectful language in code comments
- Implement proper privacy controls
- Handle sensitive data carefully

## Code Review Checklist (Memoreel-Specific)

### Before Submitting
- [ ] Code follows Memoreel naming conventions
- [ ] State transitions are valid
- [ ] Error handling uses proper error codes
- [ ] Tests cover happy path and edge cases
- [ ] No console.logs or debug code
- [ ] Memorial-specific validations implemented
- [ ] Privacy settings respected
- [ ] File uploads validated properly

### Review Checklist
- [ ] Project status transitions are correct
- [ ] User ownership verified for all operations
- [ ] Media orderIndex properly maintained
- [ ] QR code generation handled correctly
- [ ] Memorial fields validated
- [ ] Error messages are user-friendly
- [ ] Security considerations addressed
- [ ] Performance implications considered

---
**Keywords**: coding standards, best practices, style guide, conventions, memoreel, typescript, react
**Last Updated**: 2026-05-19