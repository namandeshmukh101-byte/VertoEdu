You are a Senior Full Stack Software Architect and Software Engineer.

Before writing any code:

1. Read VertoEdu_PRD_v1.0.md.
2. Follow the PRD exactly.
3. Do not redesign the architecture.
4. Continue from the existing project created in Prompts 1–3.

====================================================================
OBJECTIVE
====================================================================

Build the Teacher Module.

Implement ONLY teacher-related functionality.

Do NOT implement:

- Parent Module
- OCR
- AI Assistant
- Timetable Management
- Notice Management
- Student Dashboard
- UI redesign

====================================================================
FEATURES
====================================================================

Build the Teacher Dashboard.

The dashboard should display:

- Welcome section
- Assigned Classes
- Assigned Subjects
- Quick Actions
- Today's Attendance Status
- Recent Activity placeholder

Navigation:

Dashboard

Attendance

Results

Profile

Logout

====================================================================
ATTENDANCE MODULE
====================================================================

Teachers may only access classes assigned to them.

Implement:

GET Assigned Classes

GET Students of Assigned Class

POST Attendance

PUT Update Attendance

Attendance Fields:

- Student
- Date
- Status (Present / Absent)
- Remarks (optional)

Prevent duplicate attendance for the same student on the same day.

====================================================================
RESULT MODULE
====================================================================

Teachers may upload marks.

Supported examinations:

- PT-1
- PT-2
- Half-Yearly
- Final

Fields:

Student

Subject

Exam

Marks Obtained

Maximum Marks

Remarks (optional)

Teachers can edit marks before administrator publication.

====================================================================
DATABASE
====================================================================

Create entities:

AttendanceRecord

Exam

ExamResult

Relationships:

Teacher

↓

Assigned Subject

↓

Student

↓

Attendance

and

Teacher

↓

Subject

↓

Exam Result

====================================================================
SECURITY
====================================================================

Every endpoint requires:

Authenticated User

Role = TEACHER

Teachers must never access:

Other teachers' classes.

Other teachers' attendance.

Other teachers' results.

====================================================================
VALIDATION
====================================================================

Attendance:

One attendance record per student per day.

Marks:

Marks cannot exceed maximum marks.

Marks cannot be negative.

Student must belong to assigned class.

Teacher must be assigned to subject.

====================================================================
FRONTEND
====================================================================

Create:

Teacher Dashboard

Attendance Page

Marks Entry Page

Profile Page

Use:

React

React Router

Axios

shadcn/ui

React Hook Form

Zod

Create reusable components.

====================================================================
BACKEND
====================================================================

Implement:

AttendanceController

AttendanceService

AttendanceRepository

ExamController

ExamService

ExamRepository

ResultController

ResultService

ResultRepository

Use DTOs.

Controllers must remain thin.

Business logic belongs inside Services.

====================================================================
ERROR HANDLING
====================================================================

Return consistent JSON:

timestamp

status

message

path

Use centralized exception handling.

====================================================================
AUDIT
====================================================================

Automatically maintain:

createdAt

updatedAt

====================================================================
TESTING
====================================================================

Verify:

Teacher Login

↓

Assigned Classes

↓

Attendance Save

↓

Attendance Update

↓

Marks Upload

↓

Marks Update

↓

Unauthorized Access Blocked

====================================================================
OUTPUT
====================================================================

Provide:

1. Folder Tree

2. Entities

3. DTOs

4. Controllers

5. Services

6. Repositories

7. React Pages

8. Components

9. APIs

10. Validation Rules

11. Example Requests

12. Example Responses

====================================================================
VERIFICATION
====================================================================

Verify:

✓ React builds

✓ Spring Boot builds

✓ MySQL updated

✓ Attendance works

✓ Marks upload works

✓ Teacher security works

✓ Validation works

✓ Ready for Prompt 5

Stop after completing the Teacher Module.
