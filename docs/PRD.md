# VertoEdu
## Where AI Meets Education

---

# PRODUCT REQUIREMENTS DOCUMENT (PRD)

**Project Name:** VertoEdu

**Tagline:** Where AI Meets Education

**Version:** 1.0

**Document Status:** Final Draft

**Prepared For:** Hackathon 2026

**Prepared By:** Team VertoEdu

---

# Version History

| Version | Date | Description |
|----------|------|-------------|
| 0.1 | Initial Concept | Project Idea Finalized |
| 0.2 | Feature Planning | AI Features Added |
| 0.5 | Architecture | Technical Architecture Designed |
| 0.8 | Database Design | Database Structure Finalized |
| 1.0 | Final Draft | PRD Completed |

---

# Table of Contents

1. Executive Summary
2. Problem Statement
3. Goals & Success Metrics
4. Target Users
5. MVP Feature Scope
6. Core User Flows
7. Database Design
8. AI Intelligence & Automation
9. Technical Architecture
10. Non-Functional Requirements
11. Post-MVP Roadmap
12. Technology Stack
13. Appendix
14. Glossary

---

# 1. Executive Summary

## Product Vision

VertoEdu is an AI-powered School Operations Platform designed to modernize and automate school administration by integrating Artificial Intelligence into everyday academic and administrative workflows. The platform enables administrators, teachers, and parents to collaborate through a secure and centralized digital ecosystem while significantly reducing manual paperwork and repetitive administrative tasks.

Traditional school management systems primarily function as digital record books. They require administrators and teachers to manually perform repetitive operations such as student registration, attendance management, examination result processing, notice publication, timetable preparation, and document verification.

VertoEdu transforms this experience by combining automation with human supervision. Instead of replacing school staff, the platform assists them using AI-powered workflows that reduce workload, improve accuracy, and accelerate decision-making.

One of the key innovations of VertoEdu is its OCR-assisted admission process. School administrators can upload admission forms and student documents, after which the OCR engine extracts structured information. AI analyzes the extracted data, presents it for verification, and only inserts it into the database after administrator approval. This human-in-the-loop workflow minimizes data-entry effort while maintaining complete administrative control.

The platform also introduces an AI School Assistant capable of answering questions related to school operations, attendance policies, examination procedures, timetable information, and platform usage. Rather than modifying sensitive records, the assistant functions as an intelligent guidance system that improves accessibility and user experience.

VertoEdu follows a modular architecture that separates authentication, academic management, communication, scheduling, examinations, attendance, document management, and AI services into independent yet integrated components. This architecture ensures scalability, maintainability, and future extensibility.

The system supports three primary user roles:

- Administrator
- Teacher
- Parent

Each role receives a dedicated dashboard with role-specific permissions, ensuring security while simplifying daily workflows.

Built using React, Spring Boot, MySQL, Google OAuth, OCR technology, and OpenAI-powered AI assistance, VertoEdu demonstrates how artificial intelligence can be responsibly integrated into educational administration without replacing human decision-making.

---

# 2. Problem Statement

Educational institutions continue to rely heavily on manual administrative processes despite rapid advancements in digital technology.

Many schools still perform tasks such as student registration, attendance recording, examination result preparation, notice distribution, timetable management, and document verification manually or through fragmented software systems. These disconnected workflows consume significant administrative time, increase the likelihood of human error, and reduce communication efficiency among administrators, teachers, and parents.

During the student admission process, administrative staff often spend hours manually entering information from printed application forms into digital records. This repetitive work is time-consuming, error-prone, and difficult to verify. Likewise, attendance and examination records require repetitive data entry by teachers, while parents often experience delays in receiving academic updates.

Existing school management software frequently lacks intelligent automation. Most systems function as passive databases rather than active assistants capable of reducing workload.

Furthermore, communication between schools and parents is often scattered across messaging applications, paper notices, and multiple digital platforms, resulting in inconsistent information flow and reduced transparency.

These operational challenges become increasingly significant as schools grow in size, making administrative efficiency essential for maintaining educational quality.

VertoEdu addresses these problems by introducing AI-assisted automation while ensuring that all critical decisions remain under human supervision. Instead of replacing school staff, the platform accelerates routine administrative processes through OCR-assisted document processing, intelligent data extraction, AI-assisted guidance, centralized academic management, and secure role-based access.

By consolidating multiple administrative workflows into a single integrated platform, VertoEdu aims to reduce repetitive work, improve data accuracy, strengthen communication among stakeholders, and create a modern digital ecosystem for educational institutions.

---

# 3. Goals & Success Metrics

## Primary Goal

Develop a secure, AI-powered School Operations Platform that automates repetitive administrative tasks while simplifying communication and academic management for administrators, teachers, and parents.

The MVP focuses on demonstrating that intelligent automation combined with human approval can significantly reduce administrative workload without compromising security or data accuracy.

---

## Success Metrics

| Metric | Target for MVP Validation |
|----------|---------------------------|
| Administrator onboarding completion | > 90% |
| Teacher account activation | > 80% |
| Parent account activation | > 70% |
| OCR document processing accuracy | > 90% |
| OCR approval time reduction | > 60% compared to manual entry |
| Attendance submission success rate | > 95% |
| Examination result publication accuracy | 100% |
| AI School Assistant response success | > 90% |
| Dashboard response time | Less than 2 seconds |
| Overall system uptime | > 99% during demonstration |

---

## Secondary Goals

- Eliminate repetitive manual data entry wherever possible.
- Reduce document processing time using OCR-assisted workflows.
- Improve communication between schools and parents.
- Simplify attendance and examination management.
- Provide centralized access to academic records.
- Demonstrate responsible AI usage through human-in-the-loop verification.
- Build a scalable architecture suitable for future expansion into a complete School ERP system.

---

## Product Objectives

The VertoEdu MVP is designed to demonstrate the following capabilities:

- AI-assisted school administration.
- Secure Google OAuth authentication.
- Role-based dashboard experiences.
- OCR-powered admission workflow.
- Attendance management.
- Examination result management.
- Notice publishing system.
- Timetable management.
- AI-powered School Assistant.
- Responsive web application accessible across desktop and mobile devices.

---

## Long-Term Vision

Beyond the hackathon MVP, VertoEdu aims to evolve into a comprehensive AI-enabled School ERP platform capable of serving educational institutions of varying sizes through modular, secure, and intelligent digital workflows.

The platform is designed with scalability in mind so that future features such as predictive analytics, student performance insights, mobile applications, learning management integration, and advanced AI decision support can be introduced without major architectural redesign.
# 4. Target Users

VertoEdu is designed for educational institutions that require a secure, intelligent, and centralized platform for managing day-to-day academic and administrative operations. The platform primarily serves three categories of users, each with clearly defined responsibilities, permissions, and workflows.

The system follows a Role-Based Access Control (RBAC) model to ensure that every user can access only the features relevant to their responsibilities.

---

## 4.1 Persona A — School Administrator

### Description

The Administrator is the primary owner and controller of the VertoEdu platform within a school.

Administrators manage teachers, parents, classes, subjects, academic years, student admissions, timetables, notices, examination schedules, and AI-assisted document verification.

They possess the highest level of system permissions and are responsible for maintaining data integrity throughout the platform.

---

### Responsibilities

- Configure school information
- Manage teacher accounts
- Create parent accounts
- Register students
- Manage classes and sections
- Assign teachers to subjects
- Configure academic years
- Publish notices
- Generate reports
- Review OCR extracted data
- Approve AI-generated student records
- Monitor overall school operations

---

### Pain Points

Current administrative systems require repetitive manual data entry.

Admission forms must often be entered manually into digital records.

Managing attendance, results, notices, and timetables across multiple disconnected software systems consumes valuable administrative time.

Administrators also face challenges maintaining accurate records while handling increasing student enrollment.

---

### How VertoEdu Helps

VertoEdu automates repetitive workflows while keeping administrators in complete control.

OCR-assisted admissions significantly reduce manual typing.

Centralized dashboards eliminate fragmented software usage.

Role-based permissions improve security.

AI assists with information extraction without directly modifying sensitive records.

---

## 4.2 Persona B — Teacher

### Description

Teachers primarily interact with academic workflows.

Their dashboard is intentionally simplified to reduce unnecessary complexity while allowing quick completion of daily teaching activities.

Teachers only access classes and subjects officially assigned to them by administrators.

---

### Responsibilities

- View assigned classes
- View assigned subjects
- Mark daily attendance
- Upload examination marks
- View timetable
- Read school notices
- Update limited profile information

---

### Pain Points

Teachers spend considerable time manually recording attendance and preparing examination records.

They often need to switch between multiple systems for notices, attendance, and result submission.

---

### How VertoEdu Helps

The Teacher Dashboard consolidates all teaching-related workflows into a single interface.

Attendance recording becomes faster.

Result submission is simplified.

Teachers only view information relevant to their assignments, reducing unnecessary navigation.

---

## 4.3 Persona C — Parent

### Description

Parents are observers within the VertoEdu ecosystem.

They are intentionally given read-only permissions to maintain academic data integrity.

Parents never modify attendance, examination records, or official school documents.

---

### Responsibilities

- View student attendance
- View examination results
- Read school notices
- View timetable
- Download approved documents (future enhancement)

---

### Pain Points

Parents often receive delayed information regarding attendance, examination performance, and school announcements.

Communication is frequently scattered across paper notices and messaging applications.

---

### How VertoEdu Helps

Parents receive centralized access to academic information through a secure dashboard.

Attendance, examination results, and notices become immediately accessible after publication.

Communication becomes more transparent and organized.

---

## User Permission Matrix

| Feature | Admin | Teacher | Parent |
|----------|-------|----------|---------|
| Google Login | ✅ | ✅ | ✅ |
| Dashboard | ✅ | ✅ | ✅ |
| Manage Teachers | ✅ | ❌ | ❌ |
| Manage Parents | ✅ | ❌ | ❌ |
| Manage Students | ✅ | ❌ | ❌ |
| Manage Subjects | ✅ | ❌ | ❌ |
| Manage Timetable | ✅ | View Only | View Only |
| Attendance | View | Create & Update | View Only |
| Examination Results | View | Create & Update | View Only |
| OCR Upload | ✅ | ❌ | ❌ |
| OCR Approval | ✅ | ❌ | ❌ |
| AI School Assistant | ✅ | ✅ | ✅ |
| Notices | Create | View | View |
| Reports | ✅ | Limited | ❌ |

---

# 5. MVP Feature Scope

The MVP focuses on delivering a complete, working AI-powered School Operations Platform while keeping the implementation realistic within the hackathon timeline.

Only essential features required to demonstrate the platform's value are included in the MVP.

---

## 5.1 Authentication

Authentication is implemented using Google OAuth.

Users authenticate through their Google accounts without creating separate usernames or passwords.

Features include:

- Continue with Google
- Secure OAuth authentication
- Role-based authorization
- Automatic session management
- Protected routes
- Automatic logout on unauthorized access

Password management is delegated to Google's authentication infrastructure, eliminating the need for password storage, password reset functionality, or custom authentication logic.

---

## 5.2 Administrator Dashboard

The Administrator Dashboard serves as the control center of the platform.

Features include:

- Dashboard overview
- School statistics
- Student management
- Teacher management
- Parent management
- Academic year management
- Class management
- Section management
- Subject management
- Subject allocation
- Timetable management
- Notice management
- OCR document upload
- OCR approval interface
- Examination management
- Attendance reports
- AI School Assistant
- Activity logs

---

## 5.3 Teacher Dashboard

Teachers receive a simplified interface focused on academic operations.

Features include:

- Assigned classes
- Assigned subjects
- Daily attendance
- Examination marks entry
- Timetable
- Notices
- AI School Assistant

Teachers cannot create new classes, students, or subjects.

---

## 5.4 Parent Dashboard

Parents receive read-only access.

Features include:

- Student attendance
- Examination results
- Timetable
- Notices
- AI School Assistant

Parents cannot modify academic records.

---

## 5.5 Student Admission Module

The admission workflow combines OCR and AI assistance.

Workflow:

Administrator uploads student documents.

↓

OCR extracts text.

↓

AI converts extracted text into structured information.

↓

Administrator reviews extracted data.

↓

Administrator approves.

↓

Student record is inserted into the database.

This human approval step ensures AI never writes sensitive data directly into the database.

---

## 5.6 Attendance Management

Teachers record attendance for assigned classes.

Attendance is stored daily.

Attendance percentage is calculated dynamically.

Features include:

- Daily attendance
- Present
- Absent
- Attendance history
- Monthly reports
- Attendance analytics (future)

---

## 5.7 Examination Management

Teachers upload examination marks.

Administrators publish results.

Parents view published results.

Supported examinations include:

- PT-1
- PT-2
- Half-Yearly
- Final Examination

Future examination types can be added without changing database architecture.

---

## 5.8 Timetable Management

Administrators prepare class timetables.

Teachers and parents receive read-only access.

Timetables include:

- Subject
- Teacher
- Classroom
- Day
- Period

---

## 5.9 Notice Management

Administrators publish school notices.

Notice targeting supports:

- Entire school
- Individual class
- Individual section

Teachers and parents receive notices immediately.

---

## 5.10 AI School Assistant

The AI School Assistant provides intelligent guidance across the platform.

Capabilities include:

- Attendance queries
- Examination information
- School policies
- Timetable questions
- Platform usage guidance
- Admission process guidance

The AI assistant does not modify database records or perform administrative actions.

Its role is informational rather than operational.

---

## 5.11 OCR Pipeline

Supported documents include:

- Admission Form
- Birth Certificate
- Aadhaar Card
- Transfer Certificate
- Previous Marksheet

OCR extracts text.

AI structures the information.

Administrator verifies.

Database updates occur only after approval.

---

## 5.12 Future Features (Excluded from MVP)

The following features are intentionally excluded from the MVP:

- Student Dashboard
- Mobile Application
- Face Recognition Attendance
- WhatsApp Integration
- SMS Notifications
- Fee Management
- Library Management
- Hostel Management
- Transport Management
- Payroll
- Learning Management System
- AI Performance Prediction
- AI Attendance Prediction
- Multi-school Support
- Cloud File Storage

These features are reserved for future releases to keep the MVP focused and achievable.

# 6. Core User Flows

The platform is designed around six primary workflows that represent the most common operational activities performed within a school.

---

## Flow 1 — Administrator Onboarding

Google Login

↓

Administrator Dashboard

↓

Configure School

↓

Create Academic Year

↓

Create Classes

↓

Create Sections

↓

Create Subjects

↓

Add Teachers

↓

Assign Subjects

↓

Platform Ready

---

## Flow 2 — Student Admission

Administrator Login

↓

Upload Admission Documents

↓

OCR Extraction

↓

AI Data Structuring

↓

Administrator Review

↓

Approve

↓

Student Created

↓

Parent Linked

---

## Flow 3 — Teacher Workflow

Teacher Login

↓

Dashboard

↓

Select Assigned Class

↓

Mark Attendance

↓

Upload Marks

↓

Save Records

---

## Flow 4 — Parent Workflow

Parent Login

↓

Dashboard

↓

View Attendance

↓

View Results

↓

View Notices

↓

Logout

---

## Flow 5 — Notice Publishing

Administrator Login

↓

Create Notice

↓

Select Audience

↓

Publish

↓

Teachers and Parents Receive Notice

---

## Flow 6 — AI School Assistant

User Login

↓

Open AI Assistant

↓

Ask Question

↓

AI Processes Query

↓

Context-Aware Response

↓

Conversation Ends
# 7. Database Design

## 7.1 Database Philosophy

VertoEdu follows a relational database architecture using MySQL. The database is designed using normalization principles to minimize redundancy, maintain consistency, and support scalable academic operations.

Instead of storing duplicate information across multiple tables, the platform separates data into logical entities connected through foreign-key relationships.

The database architecture emphasizes:

- Data Integrity
- Scalability
- Performance
- Security
- Maintainability
- Extensibility

The database is intentionally designed to support future modules such as Student Dashboard, Fee Management, Mobile Applications, Analytics, and Multi-school deployment without requiring structural redesign.

---

## 7.2 Why MySQL?

The project uses MySQL because:

- Strong relational support
- Excellent Spring Boot integration
- ACID-compliant transactions
- Mature ecosystem
- Easy deployment
- Excellent indexing
- Widely used in enterprise ERP systems
- Suitable for structured educational data

Unlike document databases, school operations rely heavily on relationships between students, teachers, classes, subjects, examinations, attendance, and parents. Therefore a relational database is a better architectural choice.

---

# 7.3 Database Modules

The database is divided into independent logical modules.

### Authentication Module

- Roles
- Users

---

### School Structure Module

- Schools
- Academic Years
- Classes
- Sections
- Subjects

---

### Human Resource Module

- Teachers
- Parents
- Students

---

### Academic Module

- Student Enrollments
- Subject Allocations

---

### Attendance Module

- Attendance Records

---

### Examination Module

- Exams
- Exam Results

---

### Document Module

- Student Documents
- OCR Processing Queue

---

### Scheduling Module

- Classrooms
- Periods
- Timetables

---

### Communication Module

- Notices
- Notice Attachments

---

### Audit Module

- Activity Logs

---

# 7.4 Database Entities

The MVP consists of the following normalized tables.

## Authentication

- roles
- users

---

## School Structure

- schools
- academic_years
- school_classes
- sections
- subjects

---

## Human Resources

- teachers
- parents
- students

---

## Academic

- student_class_enrollment
- subject_allocations

---

## Attendance

- attendance_records

---

## Examination

- exams
- exam_results

---

## Documents

- student_documents
- ocr_processing_queue

---

## Scheduling

- classrooms
- periods
- timetables

---

## Communication

- notices
- notice_attachments

---

## Audit

- activity_logs

---

# 7.5 Relationship Overview

The database follows a highly normalized relationship model.

Schools own:

- Teachers
- Students
- Parents
- Classes
- Subjects

Users authenticate through Google OAuth and receive one of three roles:

- Administrator
- Teacher
- Parent

Each teacher may teach multiple subjects.

Each subject may be assigned to multiple teachers for different classes or sections.

Students belong to parents.

Students are enrolled into classes using the Student Enrollment table rather than storing class information directly inside the Student table.

Attendance records reference both the student and the assigned subject allocation.

Examination records reference students, examinations, and subjects independently.

OCR records remain isolated until administrator approval.

Activity logs capture important system actions for security and auditing.

---

# 7.6 Database Design Principles

The following principles guide the database design:

- No duplicate academic data
- Separate authentication from academic information
- Human approval before AI-generated records become permanent
- Every record must be traceable
- Foreign key integrity enforced
- Soft deletion preferred over permanent deletion where appropriate
- Academic history preserved across years

---

# 8. AI Intelligence & Automation

Artificial Intelligence is one of the defining components of VertoEdu.

Unlike traditional School ERP systems, VertoEdu integrates AI into administrative workflows while ensuring that all critical decisions remain under human supervision.

The AI engine is designed to improve efficiency rather than replace administrative staff.

---

## 8.1 AI Philosophy

The platform follows three principles:

### AI Assists

Artificial Intelligence performs repetitive tasks such as information extraction and question answering.

### Humans Decide

Administrators always approve sensitive operations before they affect the database.

### AI Never Acts Independently

No AI-generated information is directly inserted into official academic records without human verification.

---

## 8.2 OCR-Assisted Student Admission

The admission workflow combines OCR technology with AI.

Workflow:

Upload Document

↓

OCR extracts text

↓

AI converts text into structured data

↓

Administrator reviews

↓

Approve

↓

Database updated

This workflow significantly reduces manual data entry while maintaining complete administrative control.

---

## 8.3 AI School Assistant

The AI School Assistant functions as an intelligent knowledge assistant.

Capabilities include:

- Explain attendance policies
- Explain examination procedures
- Help navigate the platform
- Answer timetable questions
- Explain admission workflow
- Provide platform guidance

The assistant does not:

- Modify attendance
- Publish notices
- Upload results
- Delete records
- Change user permissions

It is strictly informational.

---

## 8.4 AI Safety

VertoEdu follows responsible AI principles.

Safety measures include:

- Human approval required
- Read-only AI access for academic records
- Prompt validation
- Role-aware responses
- Secure API communication
- Administrator override

---

## 8.5 Future AI Enhancements

Future releases may include:

- Student Performance Prediction
- Attendance Prediction
- Intelligent Timetable Optimization
- Resource Allocation
- Personalized Parent Insights
- AI Analytics Dashboard

These features are intentionally excluded from the MVP.

---

# 9. Technical Architecture

## 9.1 Technology Stack

| Layer | Technology |
|--------|------------|
| Frontend | React + Vite |
| Backend | Spring Boot |
| Database | MySQL |
| ORM | Spring Data JPA (Hibernate) |
| Authentication | Google OAuth |
| OCR | OCR Engine |
| AI | OpenAI API |
| API Style | REST |
| Build Tool | Maven |
| Version Control | Git |
| Deployment | Vercel (Frontend), Render/Railway (Backend), MySQL |

---

## 9.2 Architecture Pattern

The project follows a Three-Tier Architecture.

Presentation Layer

↓

Business Logic Layer

↓

Data Layer

This separation improves maintainability, scalability, and testing.

---

## 9.3 Backend Architecture

Spring Boot follows layered architecture.

Controller

↓

Service

↓

Repository

↓

Entity

↓

Database

Each layer has a single responsibility, making the codebase modular and easy to maintain.

---

## 9.4 Frontend Architecture

React follows component-based architecture.

Pages

↓

Layouts

↓

Reusable Components

↓

API Services

↓

Backend

Reusable UI components reduce duplication and simplify future enhancements.

---

## 9.5 Security Architecture

Authentication:

Google OAuth

Authorization:

Role-Based Access Control

Session Management:

Secure JWT / OAuth Session

Password Storage:

Not Required

Data Validation:

Client-side + Server-side

Environment Variables:

All secrets stored securely.

---

## 9.6 Deployment Architecture

User

↓

React Frontend

↓

Spring Boot REST API

↓

MySQL Database

↓

OpenAI API

↓

OCR Engine

All communication occurs through secure HTTPS connections.

---

## 9.7 Scalability

The architecture supports future expansion without major redesign.

Possible future extensions include:

- Student Dashboard
- Android App
- iOS App
- Cloud Storage
- Multi-school Support
- Analytics
- Learning Management System
- AI Prediction Models

The modular architecture ensures that these additions can be implemented independently.
# 10. Non-Functional Requirements

The MVP must satisfy both functional and quality requirements to ensure the platform is reliable, secure, maintainable, and scalable.

---

## 10.1 Performance

The platform should provide a fast and responsive user experience.

Requirements:

- Dashboard should load within 2 seconds under normal conditions.
- API responses should generally complete within 500 milliseconds for standard CRUD operations.
- OCR processing should begin immediately after upload.
- AI responses should be displayed as soon as the external AI service responds.
- Database queries should be optimized using indexing where appropriate.

---

## 10.2 Scalability

Although the MVP targets a single institution, the architecture should support future expansion.

The system should be capable of supporting:

- Multiple schools
- Thousands of students
- Hundreds of teachers
- Multiple academic years
- Future mobile applications
- Additional AI services

The database structure and backend architecture should not require major redesign for future scaling.

---

## 10.3 Security

Security is a primary requirement throughout the platform.

Requirements include:

- Google OAuth authentication
- Role-Based Access Control (RBAC)
- Secure API endpoints
- HTTPS communication
- Environment variables for all secrets
- Input validation
- Server-side authorization
- Secure file upload validation
- Protection against unauthorized access

Only authenticated users may access protected resources.

---

## 10.4 Reliability

The platform should remain stable during continuous usage.

Requirements:

- Proper exception handling
- Transaction-safe database operations
- Graceful error handling
- Logging of critical operations
- Recovery from failed OCR processing
- AI failure should never interrupt normal school operations

---

## 10.5 Availability

The MVP should demonstrate high availability during the hackathon demonstration.

Target uptime:

Greater than 99%.

---

## 10.6 Maintainability

The project should follow clean architecture principles.

Requirements:

- Modular code
- Proper folder structure
- Reusable React components
- Layered Spring Boot architecture
- Meaningful naming conventions
- Consistent API design
- Documentation for future developers

---

## 10.7 Usability

The platform should remain simple enough for non-technical school staff.

Requirements:

- Minimal learning curve
- Clean dashboard design
- Responsive layouts
- Consistent navigation
- Clear validation messages
- Accessible forms
- Mobile-friendly interface

---

## 10.8 Compatibility

The application should support modern browsers including:

- Google Chrome
- Microsoft Edge
- Mozilla Firefox

Responsive layouts should support desktops, tablets, and modern smartphones.

---

## 10.9 Error Handling

The application should provide meaningful error messages.

Examples include:

- Invalid login
- Unauthorized access
- OCR failure
- AI service unavailable
- Database connection issues
- Invalid form submission

Errors should never expose sensitive internal information.

---

## 10.10 Logging & Monitoring

Important system events should be logged.

Examples:

- User login
- Attendance submission
- Result publication
- OCR approval
- AI request
- Notice publication

These logs improve debugging and accountability.

---

# Required Environment Variables

```env
# Database
DB_URL=
DB_USERNAME=
DB_PASSWORD=

# Google OAuth
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

# OpenAI
OPENAI_API_KEY=

# OCR
OCR_API_KEY=

# Spring Security
JWT_SECRET=

# Frontend
VITE_API_BASE_URL=

# Backend
SERVER_PORT=8080
```

---

# 11. Post-MVP Roadmap

The MVP focuses on demonstrating the core value of AI-assisted school administration.

Future versions will expand the platform into a complete School ERP.

---

## Phase 2

### Student Dashboard

Students receive personalized access to:

- Attendance
- Examination Results
- Timetable
- Notices

---

### Fee Management

- Fee collection
- Payment history
- Due reminders
- Receipt generation

---

### Library Management

- Book inventory
- Book issuing
- Due reminders

---

### Hostel Management

- Hostel allocation
- Room assignment
- Student records

---

### Transport Management

- Bus routes
- Driver information
- Student allocation

---

### SMS & WhatsApp Notifications

Automatic notifications for:

- Attendance
- Results
- Notices
- Holidays

---

## Phase 3

### Mobile Applications

Native Android and iOS applications.

---

### Face Recognition Attendance

AI-powered attendance through facial recognition.

---

### AI Performance Analytics

AI-generated:

- Performance trends
- Weak subject identification
- Student progress reports

---

### AI Attendance Prediction

Predict students at risk of low attendance.

---

### AI Timetable Optimization

Automatically generate conflict-free timetables.

---

### Multi-School Support

Allow multiple institutions to use the same platform through tenant isolation.

---

### Cloud Document Storage

Replace local storage with cloud services.

Possible providers:

- AWS S3
- Cloudinary
- Google Cloud Storage

---

# Technology Stack

| Category | Technology |
|----------|------------|
| Frontend | React + Vite |
| Backend | Spring Boot |
| Database | MySQL |
| ORM | Spring Data JPA (Hibernate) |
| Authentication | Google OAuth |
| OCR | OCR Engine |
| AI | OpenAI API |
| API | REST |
| Build Tool | Maven |
| Version Control | Git & GitHub |
| Deployment | Vercel + Render/Railway + MySQL |

---

# Assumptions

The following assumptions are made during MVP development:

- Every user owns a valid Google account.
- Teachers receive Google accounts created or managed by the school.
- Parents are linked to students by administrators.
- OCR quality depends on uploaded document clarity.
- AI responses assist users but never replace administrator approval.
- Internet connectivity is available during platform usage.
- School administrators configure the platform before teachers and parents begin using it.

---

# Constraints

The MVP intentionally excludes:

- Student Dashboard
- Payment Gateway
- Library System
- Hostel System
- Transport System
- Face Recognition
- Multi-school Support
- Offline Mode
- Mobile Applications

These features are reserved for future releases.

---

# Appendix

## Supported User Roles

- Administrator
- Teacher
- Parent

---

## Primary Modules

- Authentication
- Dashboard
- Student Management
- Teacher Management
- Parent Management
- Subject Management
- Class Management
- Attendance
- Examination
- Timetable
- OCR Processing
- AI School Assistant
- Notice Management

---

## AI Workflow

Document Upload

↓

OCR

↓

AI Structuring

↓

Administrator Approval

↓

Database Update

---

## System Architecture Summary

Google OAuth

↓

React Frontend

↓

Spring Boot REST API

↓

Business Logic

↓

MySQL Database

↓

OCR Engine

↓

OpenAI API

---

# Glossary

**OCR**

Optical Character Recognition used to extract text from uploaded documents.

---

**RBAC**

Role-Based Access Control.

---

**REST API**

A standard architecture for communication between frontend and backend.

---

**Google OAuth**

Secure authentication mechanism provided by Google.

---

**Spring Boot**

Backend framework used to develop REST APIs and business logic.

---

**React**

Frontend library used for building interactive user interfaces.

---

**JPA**

Java Persistence API used for object-relational mapping.

---

**MVP**

Minimum Viable Product.

---

**AI School Assistant**

A conversational AI component that answers platform-related and school-related queries without directly modifying academic records.

---

# Conclusion

VertoEdu demonstrates how Artificial Intelligence can be responsibly integrated into educational administration by combining intelligent automation with human supervision.

The platform reduces repetitive administrative work, streamlines academic workflows, improves communication between administrators, teachers, and parents, and introduces modern AI-assisted capabilities while maintaining security, transparency, and data integrity.

Designed using a scalable architecture and modern web technologies, VertoEdu serves as a strong foundation for future expansion into a complete AI-powered School ERP ecosystem.

---

# End of Document

**VertoEdu**

**Where AI Meets Education**

**Version 1.0**
