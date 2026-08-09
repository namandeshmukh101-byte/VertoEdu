# VertoEdu — Project Structure

> A complete guide to every folder and file in the VertoEdu project.

---

## Root Directory

```
VertoEdu/
├── frontend/       → React + Vite client application
├── backend/        → Spring Boot server application
├── docs/           → Project documentation
├── prompts/        → AI prompt scripts for incremental development
├── README.md       → Project overview and getting started guide
└── .gitignore      → Git ignore rules for the entire project
```

---

## Frontend (`frontend/`)

The frontend is a React + Vite single-page application using JavaScript only (no TypeScript).

| Path                      | Purpose                                                       |
|---------------------------|---------------------------------------------------------------|
| `public/`                 | Static files served directly (favicon, icons)                 |
| `src/`                    | Application source code                                       |
| `src/assets/`             | Static assets imported by components (images, SVGs)           |
| `src/components/`         | Reusable React components                                     |
| `src/components/ui/`      | shadcn/ui base components (Button, Card, etc.)                |
| `src/hooks/`              | Custom React hooks for shared logic                           |
| `src/layouts/`            | Page layout wrappers (AdminLayout, TeacherLayout, etc.)       |
| `src/lib/`                | Utility libraries (e.g., `cn` helper for Tailwind class merging) |
| `src/pages/`              | Page-level components mapped to routes                        |
| `src/services/`           | API service layer (Axios instance and API calls)              |
| `src/utils/`              | Shared helper/utility functions                               |
| `src/App.jsx`             | Root component with React Router setup                        |
| `src/main.jsx`            | Application entry point (ReactDOM render)                     |
| `src/index.css`           | Global CSS with Tailwind directives and theme variables       |
| `.env.example`            | Environment variable template (copy to `.env`)                |
| `components.json`         | shadcn/ui configuration                                       |
| `vite.config.js`          | Vite build configuration with path aliases and proxy          |
| `package.json`            | npm dependencies and scripts                                  |
| `index.html`              | HTML entry point with meta tags                               |

---

## Backend (`backend/`)

The backend is a Spring Boot application using Maven, Spring Security, Spring Data JPA, and MySQL.

| Path                                          | Purpose                                                   |
|-----------------------------------------------|-----------------------------------------------------------|
| `src/main/java/com/vertoedu/`                 | Root Java package                                         |
| `src/main/java/com/vertoedu/config/`          | Configuration classes (Security, CORS, etc.)              |
| `src/main/java/com/vertoedu/controller/`      | REST API endpoint controllers                             |
| `src/main/java/com/vertoedu/dto/`             | Data Transfer Objects for API requests/responses          |
| `src/main/java/com/vertoedu/entity/`          | JPA entity classes mapped to database tables              |
| `src/main/java/com/vertoedu/exception/`       | Custom exceptions and global exception handler            |
| `src/main/java/com/vertoedu/repository/`      | Spring Data JPA repository interfaces                     |
| `src/main/java/com/vertoedu/security/`        | Security filters, OAuth handlers, JWT utilities           |
| `src/main/java/com/vertoedu/service/`         | Business logic services                                   |
| `src/main/java/com/vertoedu/util/`            | Shared helper/utility classes                             |
| `src/main/resources/application.properties`   | Application configuration (database, server, etc.)        |
| `src/main/resources/application.properties.example` | Environment variable template                        |
| `src/test/java/com/vertoedu/`                 | Unit and integration tests                                |
| `pom.xml`                                     | Maven project configuration and dependencies              |

---

## Documentation (`docs/`)

| File                     | Purpose                                                    |
|--------------------------|------------------------------------------------------------|
| `PRD.md`                 | Product Requirements Document — single source of truth     |
| `Design.md`              | Design System — colors, typography, component guidelines   |
| `PROJECT_STRUCTURE.md`   | This file — explains every folder                          |
| `SETUP.md`               | How future prompts build on this foundation                 |

---

## Prompts (`prompts/`)

Contains the incremental AI prompt scripts used to build the project:

| File           | Purpose                                      |
|----------------|----------------------------------------------|
| `prompt_1.md`  | Project foundation and scaffold              |
| `prompt_2.md`  | Authentication (Google OAuth, Spring Security)|
| `prompt_3.md`  | Administration module (CRUD for school data)  |
| `prompt_4.md`  | Teacher features                             |
| `prompt_5.md`  | Parent features                              |
| `prompt_6.md`  | Attendance and OCR                           |
| `prompt_7.md`  | AI features and OpenAI integration           |
| `prompt_8.md`  | Polish and deployment                        |
