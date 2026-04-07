# Bob Context System - Workflow Documentation

## 🎯 Overview
This directory contains context files that enable Bob (your AI assistant) to work with full knowledge of your project. Think of it as Bob's "knowledge base" that gets automatically loaded when you give tasks.

## 📁 Context Files

### Core Context Files
| File | Purpose | When to Update |
|------|---------|----------------|
| `project_overview.md` | High-level project info, goals, features | When project scope changes |
| `architecture.md` | System design, tech stack, patterns | When architecture evolves |
| `coding_standards.md` | Code style, conventions, best practices | When standards are established/changed |
| `api_documentation.md` | API endpoints, data models, specs | When APIs are added/modified |
| `business_logic.md` | Domain rules, workflows, validations | When business rules change |
| `dependencies.md` | Tech stack, packages, tools | When dependencies are added/updated |
| `context_index.md` | Keyword-to-file mapping for quick lookup | When new topics are added |

## 🚀 How It Works

### The Magic Behind Context-Aware Processing

When you give Bob a task like **"Add user authentication"**, here's what happens automatically:

```
1. Bob extracts keywords: ["user", "authentication", "add"]
   
2. Bob searches project_context/ for these keywords
   
3. Bob finds relevant files:
   ✓ business_logic.md (auth rules & workflows)
   ✓ architecture.md (JWT authentication pattern)
   ✓ api_documentation.md (auth endpoint structure)
   ✓ coding_standards.md (security best practices)
   
4. Bob reads all 4 files together (efficient batch read)
   
5. Bob constructs an enhanced internal prompt:
   "Add user authentication
    - Use JWT tokens (from architecture.md)
    - Follow auth workflow: register → verify → login (from business_logic.md)
    - Create /auth/login and /auth/register endpoints (from api_documentation.md)
    - Hash passwords with bcrypt, validate inputs (from coding_standards.md)"
   
6. Bob implements the feature with full context!
```

### Without Context System
```
You: "Add user authentication"
Bob: "I'll add basic authentication. What pattern should I use?"
You: "Use JWT"
Bob: "What endpoints do you need?"
You: "Login and register"
Bob: "What's the workflow?"
... (many back-and-forth questions)
```

### With Context System
```
You: "Add user authentication"
Bob: "Based on your architecture (JWT pattern) and business rules,
     I'll implement authentication with:
     - /auth/register endpoint with email verification
     - /auth/login endpoint returning JWT tokens
     - Password hashing with bcrypt
     - Token refresh mechanism
     
     Proceeding with implementation..."
```

## 📝 Getting Started

### Step 1: Fill Out Context Files
Start with the most important files for your project:

1. **project_overview.md** - Describe your project
2. **architecture.md** - Document your tech stack and patterns
3. **coding_standards.md** - Define your code style

### Step 2: Add Details Gradually
You don't need to fill everything at once! Add information as you:
- Make architectural decisions
- Establish coding patterns
- Create APIs
- Define business rules

### Step 3: Keep It Updated
Update context files when:
- Adding new features
- Changing architecture
- Establishing new patterns
- Discovering better practices

## 💡 Best Practices

### DO ✅
- **Be Specific**: "Use JWT with 1-hour expiry" vs "Use tokens"
- **Include Examples**: Show code snippets and patterns
- **Use Keywords**: Add relevant keywords at the bottom of files
- **Cross-Reference**: Link related files together
- **Keep Current**: Update when things change

### DON'T ❌
- **Don't Be Vague**: "Use good practices" doesn't help
- **Don't Duplicate**: Reference other files instead of copying
- **Don't Forget Keywords**: They help Bob find the right context
- **Don't Let It Get Stale**: Outdated context is worse than no context

## 🔍 Finding the Right Context File

Use `context_index.md` as your quick reference guide. It maps topics to files:

**Example Lookups:**
- Need auth info? → Check `business_logic.md`, `architecture.md`, `api_documentation.md`
- Adding API endpoint? → Check `api_documentation.md`, `architecture.md`
- Code style question? → Check `coding_standards.md`
- Tech stack question? → Check `dependencies.md`

## 🎨 Customization

### Adding New Context Files
If you need additional context files:

1. Create the file in `project_context/`
2. Add it to `context_index.md`
3. Include relevant keywords at the bottom
4. Update this README

**Example New Files:**
- `security_guidelines.md` - Security-specific rules
- `deployment_process.md` - Deployment procedures
- `troubleshooting.md` - Common issues and solutions
- `team_conventions.md` - Team-specific practices

### Custom Context Categories
Organize by:
- **Feature**: `auth_context.md`, `payment_context.md`
- **Layer**: `frontend_context.md`, `backend_context.md`
- **Domain**: `user_management.md`, `order_processing.md`

## 🔧 Maintenance

### Weekly
- [ ] Review recent changes and update relevant context files
- [ ] Check if new patterns have emerged

### Monthly
- [ ] Review all context files for accuracy
- [ ] Remove outdated information
- [ ] Add new sections as needed

### When Onboarding
- [ ] Have new team members read context files
- [ ] Update with their questions/feedback
- [ ] Ensure context reflects current state

## 📊 Measuring Success

You'll know the context system is working when:
- ✅ Bob asks fewer clarifying questions
- ✅ Bob's first implementation is closer to what you want
- ✅ Bob follows your patterns automatically
- ✅ Less back-and-forth in conversations
- ✅ Faster task completion

## 🆘 Troubleshooting

### Bob isn't using context
**Check:**
- Are keywords in your prompt matching context file keywords?
- Are context files filled out with relevant information?
- Is the information in the right context file?

**Solution:** Add more keywords to context files or be more specific in prompts

### Bob uses wrong context
**Check:**
- Are keywords too generic?
- Is there conflicting information in multiple files?

**Solution:** Make keywords more specific, remove duplicates

### Context is outdated
**Check:**
- When was the last update?
- Have patterns changed since then?

**Solution:** Set a reminder to review context files regularly

## 🎓 Examples

### Example 1: Well-Documented Project
```
project_context/
├── project_overview.md      ✅ Filled with project details
├── architecture.md           ✅ Complete tech stack documented
├── coding_standards.md       ✅ Team conventions defined
├── api_documentation.md      ✅ All endpoints documented
├── business_logic.md         ✅ Domain rules captured
└── dependencies.md           ✅ Package list maintained

Result: Bob works efficiently with minimal questions
```

### Example 2: Minimal Setup (Still Useful!)
```
project_context/
├── project_overview.md      ✅ Basic project description
├── architecture.md           ✅ Main tech stack listed
└── coding_standards.md       ✅ Key conventions noted

Result: Bob has enough context to be helpful
```

## 🚦 Quick Start Checklist

- [ ] Read this README
- [ ] Fill out `project_overview.md` with basic info
- [ ] Document your tech stack in `architecture.md`
- [ ] Add your code style preferences to `coding_standards.md`
- [ ] Try giving Bob a task and see how it uses context
- [ ] Gradually add more details to other files
- [ ] Update context as your project evolves

## 📚 Additional Resources

### Template Files
All context files include templates with examples. Just fill in the brackets `[like this]` with your information.

### Keywords
Each context file has a "Keywords" section at the bottom. These help Bob find the right context quickly.

### Cross-References
Files reference each other (e.g., "See architecture.md for details"). This helps Bob navigate complex topics.

## 🎉 Benefits

With a well-maintained context system, you get:
- 🚀 **Faster Development**: Less time explaining, more time building
- 🎯 **Better Accuracy**: Bob understands your patterns and follows them
- 📚 **Living Documentation**: Context files serve as project documentation
- 🤝 **Team Alignment**: Everyone (including Bob) follows the same patterns
- 🔄 **Consistency**: All code follows established conventions
- 💡 **Smart Suggestions**: Bob can suggest improvements based on context

## 🤔 Questions?

If you're unsure about:
- What to put in a context file → Check the template in that file
- Which file to update → Check `context_index.md`
- How to organize information → Look at the examples in templates

---

**Remember**: The context system is designed to save you time. Start simple, add details gradually, and keep it updated. Even basic context is better than no context!

**Last Updated**: [Date]