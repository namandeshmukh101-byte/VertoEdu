You are a Senior Full Stack Software Architect and Software Engineer.

Before writing any code:

1. Read VertoEdu_PRD_v1.0.md.
2. Continue from Prompts 1–5.
3. Do not redesign the existing architecture.
4. Integrate with existing modules instead of replacing them.

====================================================================
OBJECTIVE
====================================================================

Build the OCR + AI Intelligence Module.

This module allows administrators to upload student-related documents,
extract structured information using OCR, review the extracted data,
and use AI to assist with processing.

IMPORTANT:

AI must NEVER directly modify the database.

Every AI-generated result requires explicit administrator approval
before any database update.

====================================================================
SUPPORTED DOCUMENTS
====================================================================

Support:

- Student Admission Forms
- Student Mark Sheets
- Transfer Certificates (placeholder)
- Birth Certificates (placeholder)

The architecture must allow additional document types later.

====================================================================
OCR WORKFLOW
====================================================================

Workflow:

Administrator uploads document

↓

OCR extracts raw text

↓

Backend converts extracted text into structured JSON

↓

Display extracted fields

↓

Administrator reviews extracted data

↓

Administrator edits incorrect values if required

↓

Administrator approves

↓

Only then save to MySQL

====================================================================
AI WORKFLOW
====================================================================

After OCR completes:

Send structured OCR output to OpenAI.

AI may:

- Detect missing fields
- Suggest corrections
- Standardize formatting
- Identify possible inconsistencies

Examples:

Incorrect capitalization

Missing date

Invalid roll number format

Incomplete address

Duplicate-looking student records

AI returns suggestions only.

AI must NEVER write directly to the database.

====================================================================
ADMIN REVIEW
====================================================================

Administrator must be able to:

Review OCR output

Accept AI suggestions

Reject AI suggestions

Edit values manually

Approve final version

Cancel operation

====================================================================
DATABASE
====================================================================

Create entities:

DocumentUpload

OCRResult

AIReview

ApprovalLog

Relationships:

DocumentUpload

↓

OCRResult

↓

AIReview

↓

ApprovalLog

Store:

Upload metadata

Processing status

Approval status

Audit timestamps

====================================================================
BACKEND
====================================================================

Implement:

OCRController

OCRService

AIController

AIService

ApprovalController

DocumentStorageService

Create REST APIs for:

Upload document

Process OCR

Generate AI suggestions

Approve data

Reject data

View processing history

====================================================================
FRONTEND
====================================================================

Create pages:

OCR Dashboard

Upload Document

OCR Review

AI Suggestions

Approval Screen

Processing History

Use:

React

React Router

Axios

shadcn/ui

React Hook Form

Zod

====================================================================
UI REQUIREMENTS
====================================================================

Provide:

Drag-and-drop upload area

Upload progress indicator

OCR processing indicator

AI processing indicator

Side-by-side comparison:

Original Document

↓

Extracted Data

↓

AI Suggestions

↓

Editable Form

Approval buttons:

Approve

Reject

Save Draft

====================================================================
SECURITY
====================================================================

Only ADMIN users may:

Upload documents

Run OCR

Run AI

Approve extracted data

Teacher and Parent roles must not access this module.

====================================================================
VALIDATION
====================================================================

Validate:

Supported file type

Maximum file size

Required fields

Duplicate upload detection

Reject malformed requests.

====================================================================
ERROR HANDLING
====================================================================

Return consistent JSON:

timestamp

status

message

path

Handle OCR failures gracefully.

Handle AI failures gracefully.

Allow retry.

====================================================================
AUDIT
====================================================================

Log:

Upload time

OCR completion time

AI completion time

Administrator approval

Administrator rejection

====================================================================
TESTING
====================================================================

Verify:

Upload succeeds

↓

OCR extracts text

↓

AI suggestions generated

↓

Administrator edits values

↓

Approval works

↓

Database updated only after approval

↓

Unauthorized users blocked

====================================================================
OUTPUT
====================================================================

Provide:

1. Folder Tree

2. React Pages

3. Components

4. Controllers

5. Services

6. Repositories

7. Entities

8. DTOs

9. APIs

10. OCR Workflow

11. AI Workflow

12. Example Requests

13. Example Responses

====================================================================
VERIFICATION
====================================================================

Verify:

✓ React builds

✓ Spring Boot builds

✓ OCR upload works

✓ OCR extraction works

✓ AI suggestions work

✓ Approval workflow works

✓ Database updates only after approval

✓ Admin-only access enforced

✓ Ready for Prompt 7

Stop after completing the OCR + AI Intelligence Module.
