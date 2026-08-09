You are a Senior Full Stack Software Architect and Software Engineer.

Before writing any code:

1. Read VertoEdu_PRD_v1.0.md.
2. Continue from Prompts 1–6.
3. Preserve all previously implemented functionality.
4. Do not redesign the architecture.
5. This prompt integrates the application and modernizes the user experience without changing business logic.

====================================================================
OBJECTIVE
====================================================================

Build the production-ready dashboards for all user roles and modernize
the complete UI/UX.

This prompt integrates:

- Authentication
- Administration
- Teacher Module
- Parent Module
- OCR
- AI

into one polished application.

Do NOT implement deployment in this prompt.

====================================================================
ROLE-BASED DASHBOARDS
====================================================================

Create three dashboards.

------------------------------------------------

ADMIN DASHBOARD

Display:

• School Overview

• Total Students

• Total Teachers

• Total Parents

• Pending OCR Documents

• Pending AI Reviews

• Recent Admissions

• Quick Actions

• System Status

Navigation:

Dashboard

Students

Teachers

Parents

Attendance

Results

OCR

AI Review

Settings

Logout

------------------------------------------------

TEACHER DASHBOARD

Display:

• Welcome Message

• Assigned Classes

• Today's Attendance

• Pending Result Entry

• Recent Activity

• Quick Actions

Navigation:

Dashboard

Attendance

Results

Profile

Logout

------------------------------------------------

PARENT DASHBOARD

Display:

• Student Summary

• Attendance Percentage

• Latest Results

• School Notices

• Upcoming Exams

• Profile

Navigation:

Dashboard

Attendance

Results

Profile

Logout

====================================================================
UI/UX MODERNIZATION
====================================================================

Apply a consistent design system across the application.

Use shadcn/ui components wherever possible.

Create reusable components for:

Cards

Buttons

Dialogs

Forms

Tables

Badges

Dropdowns

Navigation

Sidebars

Breadcrumbs

Loading Indicators

Empty States

Error States

Success Notifications

Use:

React

shadcn/ui

Lucide Icons

Responsive Layouts

====================================================================
DESIGN SYSTEM
====================================================================

Create and consistently apply:

Primary Color

Secondary Color

Typography

Spacing Scale

Border Radius

Shadow System

Card Style

Table Style

Form Style

Badge Style

Button Variants

Maintain one consistent visual language throughout the application.

====================================================================
RESPONSIVENESS
====================================================================

Support:

Desktop

Tablet

Mobile

Ensure dashboards remain usable on all screen sizes.

====================================================================
ACCESSIBILITY
====================================================================

Ensure:

Keyboard Navigation

Visible Focus States

Accessible Form Labels

ARIA where appropriate

Sufficient Color Contrast

====================================================================
SYSTEM INTEGRATION
====================================================================

Verify that all modules work together:

Authentication

↓

Administration

↓

Teacher Module

↓

Parent Module

↓

OCR

↓

AI Review

No previously implemented functionality should break.

====================================================================
PERFORMANCE
====================================================================

Optimize:

Lazy loading where appropriate

API requests

Reusable components

Loading skeletons

Avoid unnecessary re-renders.

====================================================================
TESTING
====================================================================

Verify:

Administrator workflow

Teacher workflow

Parent workflow

OCR workflow

AI Review workflow

Navigation

Responsive layouts

Authorization

====================================================================
OUTPUT
====================================================================

Provide:

1. Folder Tree

2. React Pages

3. Components

4. Shared UI Components

5. Dashboard Pages

6. Design System Summary

7. APIs Used

8. Performance Improvements

9. Accessibility Improvements

10. Responsive Features

11. Testing Results

====================================================================
VERIFICATION
====================================================================

Verify:

✓ React builds successfully

✓ Spring Boot builds successfully

✓ Admin Dashboard works

✓ Teacher Dashboard works

✓ Parent Dashboard works

✓ Responsive layouts verified

✓ Authentication still works

✓ OCR workflow still works

✓ AI workflow still works

✓ No existing functionality broken

Stop after completing the UI/UX modernization and dashboard integration.

Deployment will be completed in the next prompt.
