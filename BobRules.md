# Bob Rules Configuration File
# Define custom rules and preferences for Bob, your AI coding assistant

## Context-Aware Processing
**MANDATORY - Follow EVERY TIME, NO EXCEPTIONS:**

This workflow must be executed for EVERY task to create context-rich super prompts:

### Step 1: Extract Keywords
- Identify key terms from the user's task/prompt
- Extract relevant technical concepts, features, and domain terms

### Step 2: Search project_context/
- Search the project_context/ directory for files matching extracted keywords
- Look for relevant information across all context files

### Step 3: Read Context Files
- Read all matching context files to gather project-specific information
- Priority files to check:
  - project_overview.md - Project purpose and scope
  - architecture.md - System design and patterns
  - business_logic.md - Business rules and workflows
  - api_documentation.md - API specifications
  - coding_standards.md - Code style and conventions
  - dependencies.md - Tech stack and tools
  - context_index.md - Quick reference guide
  - Design/ - UI/UX design references

### Step 4: Combine Context with Original Prompt
- Merge the original task with all relevant project context
- Ensure no conflicting information

### Step 5: Generate SUPER PROMPT
- Create an enriched prompt that combines:
  - Original user task
  - Relevant project context
  - Technical specifications
  - Business requirements
  - Coding standards
- This SUPER PROMPT contains complete understanding of what needs to be done

### Step 6: Execute with Full Context
- Use the SUPER PROMPT to complete the task
- Implementation will be aligned with project standards and architecture
- No need for back-and-forth clarifications on project-specific details

**Result:** Every task is executed with complete project knowledge, ensuring consistency, adherence to standards, and intelligent decision-making.

## 1. General Principles
- Generate clean, readable, and modular code.
- Follow the project's folder structure and coding conventions strictly.
- Ensure all code is complete, runnable, and properly formatted.

## 2. Code Quality
- Use meaningful and consistent naming conventions.
- Avoid code duplication; reuse existing logic whenever possible.
- Keep functions small and focused on a single responsibility.

## 3. Error Handling & Validation
- Always include proper error handling.
- Validate all inputs and handle edge cases.
- Do not leave unhandled exceptions.

## 4. No Hallucination Policy
- Do NOT invent or assume APIs, libraries, functions, or data.
- Do NOT generate code based on unclear or incomplete requirements.

## 5. Clarification Requirements
- If requirements are unclear or incomplete:
  - DO NOT guess or proceed with assumptions.
  - ASK the user for clarification before generating code.

## 6. Assumptions Handling
- Only proceed with assumptions if explicitly instructed.
- Clearly state all assumptions in comments.

## 7. Security Rules
- Never hardcode secrets (API keys, tokens, passwords).
- Follow secure coding practices.
- Validate and sanitize all external inputs.

## 8. Best Practices
- Follow best practices of the chosen language and framework.
- Avoid deprecated or outdated approaches.
- Prefer standard and well-supported libraries.

## 9. Maintainability
- Write code that is easy to test and maintain.
- Add comments only where necessary (explain "why", not "what").

## 10. Output Requirements
- Provide complete working code (no partial snippets unless requested).
- Ensure consistency with existing project code.
- If something cannot be completed due to missing information, ask for clarification instead of guessing.