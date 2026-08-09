You are a Senior Full Stack Software Architect and Software Engineer.

Before writing any code:

1. Read VertoEdu_PRD_v1.0.md.
2. Follow the PRD exactly.
3. Do not redesign the architecture.
4. Do not replace any technology.
5. Build production-quality software.

====================================================================
CURRENT OBJECTIVE
====================================================================

Implement the Authentication & Security module only.

This prompt must NOT implement:

- Attendance
- Examination
- Student Management
- Teacher Management
- Parent Management
- OCR
- AI Assistant
- Notice Management
- Timetable

Only authentication and authorization.

====================================================================
TECH STACK (LOCKED)
====================================================================

Frontend
- React + Vite
- JavaScript
- React Router
- Axios
- shadcn/ui
- React Hook Form
- Zod

Backend
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

Database
- MySQL

Authentication
- Google OAuth

====================================================================
DATABASE
====================================================================

Create only the authentication-related tables.

roles

Fields:
- id
- name
- description
- createdAt
- updatedAt

Seed:

ADMIN

TEACHER

PARENT

----------------------------

users

Fields:

- id
- googleId
- fullName
- email
- profileImage
- roleId
- active
- createdAt
- updatedAt

Use proper foreign-key relationships.

Do not create any other tables.

====================================================================
BACKEND
====================================================================

Configure Spring Security.

Configure Google OAuth login.

Create:

- SecurityConfig
- OAuth2 Success Handler
- OAuth2 Failure Handler
- User Entity
- Role Entity
- User Repository
- Role Repository
- User Service
- Role Service
- Authentication Controller
- Global Exception Handler

Implement:

User Login

↓

Google Authentication

↓

Check User Exists

↓

If Not

Create User

↓

Assign Default Role

↓

Create Session

↓

Redirect

Do not implement business APIs.

====================================================================
DEFAULT ROLE
====================================================================

When a user logs in for the first time:

Create the account automatically.

Assign:

PARENT

as the default role.

The role must be easy to change later by an administrator.

====================================================================
FRONTEND
====================================================================

Create:

Login Page

Include:

- VertoEdu logo placeholder
- Tagline
- Continue with Google button
- Loading state
- Error state

Do not create email/password login.

Create:

Authentication Context

Protected Route component

Logout functionality

Unauthorized page

Session persistence

====================================================================
ROUTING
====================================================================

After successful login:

ADMIN

↓

/admin

-----------------------

TEACHER

↓

/teacher

-----------------------

PARENT

↓

/parent

Automatically redirect according to role.

====================================================================
SECURITY
====================================================================

Never trust frontend roles.

Every protected API must verify authentication.

Every protected API must verify authorization.

Load every secret from environment variables.

Never hardcode credentials.

====================================================================
ENVIRONMENT VARIABLES
====================================================================

Backend

application.properties

DB_URL=

DB_USERNAME=

DB_PASSWORD=

GOOGLE_CLIENT_ID=

GOOGLE_CLIENT_SECRET=

JWT_SECRET=

Frontend

.env

VITE_API_BASE_URL=

====================================================================
DO NOT DO
====================================================================

Do NOT create:

Attendance

Results

Students

Teachers

Parents

OCR

AI

Dashboards

Timetable

Notice Module

Those belong to future prompts.

====================================================================
OUTPUT FORMAT
====================================================================

Provide:

1. Folder Tree

2. Files Created

3. Dependencies Added

4. Database Tables

5. Backend Components

6. Frontend Components

7. Commands Executed

8. Manual Configuration Steps

====================================================================
VERIFICATION
====================================================================

Verify:

✓ React starts

✓ Spring Boot starts

✓ Maven build succeeds

✓ Google OAuth configuration is ready

✓ User table created

✓ Role table created

✓ Roles seeded

✓ Login succeeds

✓ Logout succeeds

✓ Session persists

✓ Protected routes work

✓ Unauthorized users blocked

Stop after authentication is fully complete.

Do not continue into Prompt 3.
