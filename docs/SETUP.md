# VertoEdu — Setup Guide

> How future prompts build on this foundation.

---

## Current State (After Prompt 1)

Prompt 1 establishes the complete project scaffold:

- **Frontend**: React + Vite application with all dependencies installed, Tailwind CSS configured, shadcn/ui initialized, React Router set up, Axios configured, and a landing page placeholder.
- **Backend**: Spring Boot application with Maven, Spring Security (permit-all for now), Spring Data JPA, MySQL driver, Lombok, and Validation. All package directories created.
- **Documentation**: README, project structure guide, environment templates.
- **No business logic**: Authentication, dashboards, CRUD, OCR, and AI are intentionally deferred to later prompts.

---

## Incremental Build Plan

Each prompt builds on the previous one. The foundation established here supports all future additions.

### Prompt 2 — Authentication

- Implements Google OAuth via Spring Security
- Adds JWT/session management
- Creates User and Role entities
- Builds login/logout UI, protected routes, and role-based redirects
- **Depends on**: Prompt 1 (security package, config, frontend routing)

### Prompt 3 — Administration Foundation

- Implements CRUD for School, Academic Year, Classes, Sections, Subjects, Teachers, Parents, Students
- Creates all JPA entities, DTOs, repositories, services, controllers
- Adds global exception handler and validation
- **Depends on**: Prompt 2 (authentication and role-based access)

### Prompt 4 — Teacher Features

- Teacher dashboard, class management, student views
- **Depends on**: Prompt 3 (entities and admin CRUD)

### Prompt 5 — Parent Features

- Parent dashboard, student progress, communication
- **Depends on**: Prompt 3 (entities and admin CRUD)

### Prompt 6 — Attendance & OCR

- Attendance tracking with OCR-based document processing
- **Depends on**: Prompts 3–5 (entities, teacher/parent views)

### Prompt 7 — AI Features

- OpenAI API integration for result analysis and intelligent features
- **Depends on**: Prompts 3–6 (complete data layer)

### Prompt 8 — Polish & Deployment

- UI polish, performance optimization, deployment configuration
- **Depends on**: All previous prompts

---

## Key Architecture Decisions

1. **Frontend and backend are completely separate** — they communicate via REST API only.
2. **Environment variables** are used for all secrets — nothing is hardcoded.
3. **shadcn/ui** provides consistent, accessible UI components.
4. **Spring Data JPA** with Hibernate handles database operations — no raw SQL.
5. **Maven** manages backend dependencies and builds.
6. **Vite** provides fast frontend development with HMR.

---

## How to Add New Features

1. **Backend**: Add entity → repository → service → controller → DTO in the respective packages.
2. **Frontend**: Add page → component → service call → route in the respective directories.
3. **Always follow** the PRD, Design System, and coding standards established in Prompt 1.
