# Business Logic & Domain Knowledge

## Domain Overview
[High-level description of the business domain]

## Core Business Concepts

### [Concept 1]
**Definition**: [What it is]
**Purpose**: [Why it exists]
**Rules**: 
- [Rule 1]
- [Rule 2]
- [Rule 3]

### [Concept 2]
**Definition**: [What it is]
**Purpose**: [Why it exists]
**Rules**:
- [Rule 1]
- [Rule 2]

## Business Rules

### User Management
- Users must verify email before accessing features
- Passwords must meet complexity requirements
- Account lockout after [X] failed login attempts
- Session timeout after [X] minutes of inactivity

### [Feature/Module Name]
- [Business rule 1]
- [Business rule 2]
- [Business rule 3]

## Workflows

### User Registration Flow
1. User submits registration form
2. System validates input data
3. System checks if email already exists
4. System creates user account (inactive status)
5. System sends verification email
6. User clicks verification link
7. System activates account
8. User can now log in

### [Another Workflow]
1. [Step 1]
2. [Step 2]
3. [Step 3]

## Validation Rules

### Email Validation
- Must be valid email format
- Must not already exist in system
- Must be from allowed domains (if applicable)

### Password Validation
- Minimum [X] characters
- Must contain uppercase letter
- Must contain lowercase letter
- Must contain number
- Must contain special character

### [Other Validations]
- [Validation rule 1]
- [Validation rule 2]

## Calculations & Formulas

### [Calculation Name]
```
Formula: [Mathematical formula or logic]
Example: 
  Input: [values]
  Output: [result]
```

### Pricing Logic
```
Base Price: $X
Discount: Y%
Tax: Z%
Final Price = (Base Price - Discount) * (1 + Tax)
```

## State Machines

### Order Status Flow
```
[Draft] → [Pending] → [Processing] → [Shipped] → [Delivered]
                ↓
            [Cancelled]
```

**Transitions**:
- Draft → Pending: User submits order
- Pending → Processing: Payment confirmed
- Processing → Shipped: Order dispatched
- Shipped → Delivered: Customer receives order
- Any → Cancelled: User/Admin cancels order

### [Another State Machine]
[Describe states and transitions]

## Business Constraints

### Time-based Constraints
- Orders can only be placed during business hours
- Refunds must be requested within [X] days
- Subscriptions renew on [specific date/interval]

### Quantity Constraints
- Minimum order quantity: [X]
- Maximum items per order: [Y]
- Stock availability checks required

### Financial Constraints
- Minimum transaction amount: $[X]
- Maximum transaction amount: $[Y]
- Currency: [USD, EUR, etc.]

## Permissions & Access Control

### User Roles
| Role | Permissions | Description |
|------|-------------|-------------|
| Admin | Full access | System administrator |
| Manager | Read/Write most resources | Department manager |
| User | Limited access | Regular user |
| Guest | Read-only public data | Unauthenticated user |

### Feature Access Matrix
| Feature | Admin | Manager | User | Guest |
|---------|-------|---------|------|-------|
| View Dashboard | ✓ | ✓ | ✓ | ✗ |
| Create Orders | ✓ | ✓ | ✓ | ✗ |
| Manage Users | ✓ | ✗ | ✗ | ✗ |
| View Reports | ✓ | ✓ | ✗ | ✗ |

## Data Retention & Lifecycle

### Data Retention Policies
- User data: Retained for [X] years after account deletion
- Transaction logs: Retained for [Y] years
- Audit logs: Retained for [Z] years
- Temporary data: Deleted after [N] days

### Data Archival
- Active data: [Definition and storage]
- Archived data: [Definition and storage]
- Deletion policy: [When and how data is permanently deleted]

## Integration Rules

### Third-party Services
- [Service Name]: [When and how it's used]
- [Service Name]: [When and how it's used]

### Data Synchronization
- [What data syncs]
- [Sync frequency]
- [Conflict resolution strategy]

## Edge Cases & Special Scenarios

### Scenario 1: [Description]
**Condition**: [When this happens]
**Expected Behavior**: [What should happen]
**Implementation**: [How it's handled]

### Scenario 2: [Description]
**Condition**: [When this happens]
**Expected Behavior**: [What should happen]
**Implementation**: [How it's handled]

## Business Metrics & KPIs

### Key Metrics
- [Metric 1]: [Definition and target]
- [Metric 2]: [Definition and target]
- [Metric 3]: [Definition and target]

### Success Criteria
- [Criterion 1]
- [Criterion 2]
- [Criterion 3]

## Compliance & Regulations

### Data Privacy
- GDPR compliance requirements
- User consent management
- Right to be forgotten implementation

### Industry Standards
- [Standard 1]: [How it's implemented]
- [Standard 2]: [How it's implemented]

## Glossary

| Term | Definition |
|------|------------|
| [Term 1] | [Definition] |
| [Term 2] | [Definition] |
| [Term 3] | [Definition] |

## Common Scenarios & Examples

### Example 1: [Scenario Name]
**Context**: [Background]
**Input**: [What comes in]
**Process**: [What happens]
**Output**: [What comes out]

### Example 2: [Scenario Name]
**Context**: [Background]
**Input**: [What comes in]
**Process**: [What happens]
**Output**: [What comes out]

## Decision Trees

### [Decision Name]
```
Is user authenticated?
├─ Yes → Check permissions
│         ├─ Has permission → Allow access
│         └─ No permission → Deny access (403)
└─ No → Redirect to login
```

## Notifications & Alerts

### User Notifications
- [Event]: [Notification sent]
- [Event]: [Notification sent]

### System Alerts
- [Condition]: [Alert triggered]
- [Condition]: [Alert triggered]

---
**Keywords**: business logic, domain, rules, workflows, validation
**Last Updated**: [Date]