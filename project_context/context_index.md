# Context Index - Quick Reference Guide

This file maps topics and keywords to relevant context files for fast lookup.

## How to Use This Index
When working on a task, search for relevant keywords below to find which context files contain the information you need.

---

## 📋 Quick Links
- [Project Overview](./project_overview.md) - High-level project information
- [Architecture](./architecture.md) - System design and structure
- [Coding Standards](./coding_standards.md) - Code style and best practices
- [API Documentation](./api_documentation.md) - API endpoints and specifications
- [Business Logic](./business_logic.md) - Domain rules and workflows
- [Dependencies](./dependencies.md) - Tech stack and packages

---

## 🔍 Keyword to Context Mapping

### Authentication & Authorization
**Related Files**: 
- `architecture.md` - Authentication architecture
- `api_documentation.md` - Auth endpoints
- `business_logic.md` - Auth rules and permissions
- `coding_standards.md` - Security best practices

**Keywords**: auth, login, JWT, OAuth, token, session, permissions, roles, access control

---

### API & Endpoints
**Related Files**:
- `api_documentation.md` - Complete API reference
- `architecture.md` - API architecture pattern
- `coding_standards.md` - API coding standards

**Keywords**: REST, GraphQL, endpoints, routes, HTTP, request, response, API

---

### Database & Data
**Related Files**:
- `architecture.md` - Database architecture
- `dependencies.md` - Database tools and ORM
- `business_logic.md` - Data validation rules
- `api_documentation.md` - Data models

**Keywords**: database, SQL, PostgreSQL, MongoDB, Prisma, ORM, schema, migration, query

---

### Frontend & UI
**Related Files**:
- `architecture.md` - Frontend architecture
- `dependencies.md` - Frontend libraries
- `coding_standards.md` - UI coding standards

**Keywords**: React, Vue, Angular, components, UI, frontend, styling, CSS, Tailwind

---

### Backend & Server
**Related Files**:
- `architecture.md` - Backend architecture
- `dependencies.md` - Backend frameworks
- `api_documentation.md` - Server endpoints
- `coding_standards.md` - Backend coding standards

**Keywords**: Node.js, Express, server, backend, middleware, controllers, services

---

### Testing
**Related Files**:
- `coding_standards.md` - Testing standards
- `dependencies.md` - Testing tools
- `architecture.md` - Testing strategy

**Keywords**: test, Jest, unit test, integration test, E2E, coverage, TDD

---

### Security
**Related Files**:
- `coding_standards.md` - Security best practices
- `business_logic.md` - Security rules
- `api_documentation.md` - API security
- `architecture.md` - Security architecture

**Keywords**: security, encryption, HTTPS, XSS, CSRF, SQL injection, validation, sanitization

---

### Deployment & DevOps
**Related Files**:
- `architecture.md` - Deployment architecture
- `dependencies.md` - DevOps tools
- `coding_standards.md` - CI/CD practices

**Keywords**: Docker, Kubernetes, CI/CD, deployment, AWS, Azure, GCP, container

---

### Business Rules & Logic
**Related Files**:
- `business_logic.md` - Complete business rules
- `api_documentation.md` - Business endpoints
- `project_overview.md` - Business goals

**Keywords**: workflow, validation, rules, permissions, state machine, calculations

---

### Code Quality
**Related Files**:
- `coding_standards.md` - Complete standards
- `dependencies.md` - Quality tools

**Keywords**: ESLint, Prettier, linting, formatting, code review, best practices

---

### Performance
**Related Files**:
- `architecture.md` - Performance architecture
- `coding_standards.md` - Performance guidelines
- `dependencies.md` - Performance tools

**Keywords**: optimization, caching, Redis, performance, scalability, load balancing

---

### Error Handling
**Related Files**:
- `coding_standards.md` - Error handling standards
- `api_documentation.md` - Error responses
- `business_logic.md` - Error scenarios

**Keywords**: error, exception, try-catch, logging, monitoring, debugging

---

## 📊 Context File Purposes

### project_overview.md
**Use When**: 
- Starting a new feature
- Understanding project goals
- Onboarding new team members
- Need high-level context

**Contains**:
- Project description and goals
- Key features
- Team information
- Terminology

---

### architecture.md
**Use When**:
- Designing new features
- Understanding system structure
- Making architectural decisions
- Integrating components

**Contains**:
- System architecture pattern
- Component structure
- Data flow
- Infrastructure details
- Design patterns

---

### coding_standards.md
**Use When**:
- Writing new code
- Reviewing code
- Setting up linters
- Establishing conventions

**Contains**:
- Naming conventions
- Code formatting rules
- Best practices
- Testing standards
- Security guidelines

---

### api_documentation.md
**Use When**:
- Creating new endpoints
- Integrating with API
- Understanding data models
- Debugging API issues

**Contains**:
- All API endpoints
- Request/response formats
- Authentication methods
- Error codes
- Data models

---

### business_logic.md
**Use When**:
- Implementing features
- Understanding workflows
- Validating data
- Making business decisions

**Contains**:
- Business rules
- Workflows
- Validation rules
- State machines
- Permissions

---

### dependencies.md
**Use When**:
- Adding new packages
- Updating dependencies
- Troubleshooting issues
- Understanding tech stack

**Contains**:
- Complete tech stack
- Package versions
- Installation instructions
- Dependency management
- Troubleshooting

---

## 🎯 Common Task Scenarios

### Scenario: Adding User Authentication
**Check These Files**:
1. `business_logic.md` - Auth rules and workflows
2. `architecture.md` - Auth architecture pattern
3. `api_documentation.md` - Auth endpoints
4. `dependencies.md` - Auth libraries (JWT, bcrypt)
5. `coding_standards.md` - Security best practices

---

### Scenario: Creating a New API Endpoint
**Check These Files**:
1. `api_documentation.md` - Endpoint patterns and structure
2. `architecture.md` - API architecture
3. `coding_standards.md` - API coding standards
4. `business_logic.md` - Business rules for the endpoint

---

### Scenario: Setting Up Database
**Check These Files**:
1. `architecture.md` - Database architecture
2. `dependencies.md` - Database tools and ORM
3. `business_logic.md` - Data validation rules
4. `coding_standards.md` - Database best practices

---

### Scenario: Implementing a Workflow
**Check These Files**:
1. `business_logic.md` - Workflow definitions
2. `api_documentation.md` - Related endpoints
3. `architecture.md` - System flow
4. `coding_standards.md` - Implementation standards

---

### Scenario: Fixing a Bug
**Check These Files**:
1. `coding_standards.md` - Error handling patterns
2. `business_logic.md` - Expected behavior
3. `api_documentation.md` - Expected responses
4. `architecture.md` - System behavior

---

## 🔄 Maintenance

### Updating Context Files
When you update any context file, remember to:
1. Update the "Last Updated" date in that file
2. Update this index if new keywords or topics are added
3. Ensure cross-references are still accurate

### Adding New Context Files
If you add a new context file:
1. Add it to the Quick Links section
2. Add relevant keyword mappings
3. Add a purpose section
4. Update relevant scenarios

---

## 💡 Tips for Effective Context Usage

1. **Start Broad**: Begin with `project_overview.md` for context
2. **Get Specific**: Move to specialized files for details
3. **Cross-Reference**: Use multiple files for comprehensive understanding
4. **Search Keywords**: Use Ctrl+F to find specific topics
5. **Keep Updated**: Regularly update context files as project evolves

---

**Last Updated**: [Date]