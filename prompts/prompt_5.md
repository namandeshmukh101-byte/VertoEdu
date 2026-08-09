You are a Senior Full Stack Software Architect and Software Engineer.

Before writing any code:

1. Read VertoEdu_PRD_v1.0.md.
2. Continue from Prompts 1–4.
3. Do not redesign the existing architecture.
4. Do not modify completed modules unless required to integrate this prompt.

====================================================================
OBJECTIVE
====================================================================

Build the Parent Portal.

This module allows parents to securely monitor their child's academic
progress without modifying school records.

Implement ONLY parent-related functionality.

Do NOT implement:

- OCR
- AI Assistant
- UI redesign
- Timetable Management
- Notice Module
- Administration Module changes

====================================================================
PARENT DASHBOARD
====================================================================

Create a Parent Dashboard.

Display:

- Welcome section
- Student Profile Summary
- Today's Attendance
- Attendance Percentage
- Recent Exam Results
- Upcoming Exams (placeholder)
- Recent School Notices (placeholder)
- Quick Actions

Navigation:

Dashboard

Attendance

Results

Profile

Logout

====================================================================
STUDENT PROFILE
====================================================================

Parents can only view students linked to their own account.

Display:

- Student Name
- Class
- Section
- Roll Number
- Academic Year
- Profile Photo (placeholder)
- Parent Information

Parents cannot edit academic information.

====================================================================
ATTENDANCE MODULE
====================================================================

Create Attendance page.

Display:

- Daily Attendance History
- Monthly Attendance Summary
- Attendance Percentage

Provide filters:

- Month
- Academic Year

Read-only.

====================================================================
RESULT MODULE
====================================================================

Create Results page.

Display:

- Examination Name
- Subject
- Marks Obtained
- Maximum Marks
- Percentage
- Grade
- Teacher Remarks

Allow filtering by:

- Examination
- Academic Year

Read-only.

====================================================================
PROFILE MODULE
====================================================================

Allow parents to update only:

- Contact Number
- Alternate Contact
- Address

Do NOT allow editing:

Student Name

Class

Roll Number

Academic Data

====================================================================
BACKEND
====================================================================

Implement:

ParentController

ParentService

ParentRepository

AttendanceQueryService

ResultQueryService

Create APIs for:

Student Profile

Attendance History

Attendance Summary

Exam Results

Parent Profile Update

====================================================================
DATABASE
====================================================================

Implement relationships:

Parent

↓

Student

↓

Attendance

↓

Exam Results

Use existing entities created in previous prompts.

Do NOT redesign schema.

====================================================================
SECURITY
====================================================================

Every endpoint requires:

Authenticated User

AND

Role = PARENT

Parents must never access:

Other students

Other parents

Teacher APIs

Admin APIs

====================================================================
VALIDATION
====================================================================

Allow editing only permitted profile fields.

Reject attempts to modify academic records.

Return proper HTTP status codes.

====================================================================
FRONTEND
====================================================================

Build using:

React

React Router

Axios

shadcn/ui

React Hook Form

Zod

Use reusable components.

Pages:

Parent Dashboard

Attendance Page

Results Page

Profile Page

Unauthorized Page

Loading States

Empty States

====================================================================
ERROR HANDLING
====================================================================

Implement centralized exception handling.

Return consistent JSON:

timestamp

status

message

path

====================================================================
TESTING
====================================================================

Verify:

Parent Login

↓

Dashboard Loads

↓

Student Profile Loads

↓

Attendance History Loads

↓

Results Load

↓

Profile Update Works

↓

Unauthorized Access Blocked

====================================================================
OUTPUT
====================================================================

Provide:

1. Folder Tree

2. React Pages Created

3. Components Created

4. Controllers

5. Services

6. Repositories

7. APIs

8. DTOs

9. Validation Rules

10. Example Requests

11. Example Responses

====================================================================
VERIFICATION
====================================================================

Verify:

✓ React builds successfully

✓ Spring Boot builds successfully

✓ Parent Dashboard works

✓ Attendance page works

✓ Results page works

✓ Profile update works

✓ Authorization works

✓ Ready for Prompt 6

Stop after completing the Parent Module.
