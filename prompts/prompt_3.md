You are a Senior Full Stack Software Architect and Software Engineer.

Read the attached VertoEdu_PRD_v1.0.md before writing any code.

This prompt builds the core database foundation and Administration Module.

Do NOT implement Teacher features, Parent features, OCR, AI, Attendance, Results or Timetable yet.

====================================================================
OBJECTIVE
====================================================================

Build the complete Administration Foundation.

This prompt establishes:

- Database schema
- JPA entities
- Repository layer
- Service layer
- Administration REST APIs
- Validation
- Exception handling

No frontend pages beyond minimal API testing.

====================================================================
DATABASE
====================================================================

Implement the following entities using Spring Data JPA.

1. School

2. AcademicYear

3. SchoolClass

4. Section

5. Subject

6. Teacher

7. Parent

8. Student

Relationships must follow the PRD exactly.

Use:

- Primary Keys
- Foreign Keys
- Proper JPA Relationships
- Cascade only where appropriate

Do NOT duplicate data.

====================================================================
CRUD MODULES
====================================================================

Create REST APIs for:

Academic Years

School Classes

Sections

Subjects

Teachers

Parents

Students

Each module must support:

GET

POST

PUT

DELETE

Validation required.

====================================================================
ADMIN SECURITY
====================================================================

Every endpoint must require:

Authenticated User

AND

Role = ADMIN

Return:

401

if unauthenticated.

403

if authenticated but unauthorized.

====================================================================
VALIDATION
====================================================================

Use Bean Validation.

Examples:

Name required.

Email unique.

Academic Year unique.

Subject code unique.

No duplicate class names inside same academic year.

Reject invalid requests with meaningful messages.

====================================================================
SERVICE LAYER
====================================================================

Business logic must exist only inside Services.

Controllers should remain thin.

Repositories should contain only data access.

====================================================================
GLOBAL EXCEPTION HANDLING
====================================================================

Implement centralized exception handling.

Return consistent JSON:

timestamp

status

message

path

====================================================================
AUDIT
====================================================================

Automatically populate:

createdAt

updatedAt

for every entity.

====================================================================
DATABASE INITIALIZATION
====================================================================

Seed:

One School

Three Roles

ADMIN

TEACHER

PARENT

One Academic Year

Administrator account

====================================================================
API OUTPUT
====================================================================

Return clean DTOs.

Never expose entity internals unnecessarily.

====================================================================
DO NOT IMPLEMENT
====================================================================

Attendance

Results

OCR

AI

Timetable

Notice Module

Teacher Dashboard

Parent Dashboard

Student Dashboard

====================================================================
TESTING
====================================================================

Verify every API using:

Postman or REST Client.

Show example:

Request

↓

Response

for every endpoint.

====================================================================
OUTPUT
====================================================================

Provide:

1. Folder Tree

2. Entities Created

3. DTOs Created

4. Repositories Created

5. Services Created

6. Controllers Created

7. APIs Created

8. Database Tables

9. Validation Rules

10. Testing Results

====================================================================
VERIFICATION
====================================================================

Verify:

✔ Spring Boot starts.

✔ MySQL connects.

✔ Tables generated.

✔ Foreign keys created.

✔ CRUD APIs working.

✔ Validation working.

✔ Security working.

✔ Admin-only APIs protected.

Stop after completing the Administration Foundation.

Do not continue into Teacher Module.
