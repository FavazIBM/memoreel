# AI Code Generation Rules

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

## 5. Clarification Requirement (MANDATORY)
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