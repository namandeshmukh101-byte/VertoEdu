# VertoEdu

> **Where AI Meets Education**

AI-Powered School Operations Platform designed to modernize and automate school administration — built for Hackathon 2026 by Team VertoEdu.

---

> [!IMPORTANT]
> ### 🚀 Hackathon Judges: Skip Local Setup
> To avoid configuring Google Cloud OAuth credentials, MySQL databases, and Python Tesseract binaries locally, we highly recommend evaluating the live production deployment:
> **Live Demo:** [https://verto-edu.vercel.app/](https://verto-edu.vercel.app/)
> *(Fully configured with seed data, OCR, AI, and OAuth)*

---

## Project Overview

VertoEdu is a full-stack web application that streamlines school operations using AI. It provides role-based dashboards for Admins, Teachers, and Parents with features including student management, attendance tracking, AI-powered result analysis, OCR-based document processing, and intelligent notice generation.

---

## Technology Stack

| Layer          | Technology                          |
|----------------|-------------------------------------|
| Frontend       | React + Vite (JavaScript only)      |
| UI Library     | shadcn/ui + Tailwind CSS            |
| Icons          | Lucide React                        |
| Forms          | React Hook Form + Zod               |
| Backend        | Spring Boot 3.4                     |
| Security       | Spring Security + Google OAuth      |
| ORM            | Spring Data JPA (Hibernate)         |
| Database       | MySQL                               |
| AI             | OpenAI API                          |
| OCR Service    | Python + FastAPI + Tesseract        |

---

## Folder Structure

```text
VertoEdu/
├── frontend/                  # React + Vite application
├── backend/                   # Spring Boot application
├── ocr-service/               # Python FastAPI microservice for OCR processing
├── database/                  # SQL files for database setup and seed data
└── README.md                  # This file
```

---

## Prerequisites

- **Node.js** ≥ 20.x and **npm** ≥ 10.x
- **Java** ≥ 17 (JDK)
- **Maven** ≥ 3.9
- **MySQL** ≥ 8.0
- **Python** ≥ 3.9 (for OCR service)
- **Tesseract OCR Engine** (Required for OCR service, see instructions below)

---

## Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd VertoEdu
```

### 2. Database Setup (MySQL)

1. Ensure MySQL is running on your local machine (default port `3306`).
2. Create a database named `vertoedu`.
3. Import the provided seed data from the `database/` folder:
```bash
mysql -u root -p vertoedu < database/vertoedu_demo_export.sql
```

### 3. OCR Service Setup (Python)

The OCR microservice uses Tesseract to extract text from images and PDFs.

**Install Tesseract Engine:**
- **Windows:** Download and install from [UB-Mannheim/tesseract/wiki](https://github.com/UB-Mannheim/tesseract/wiki). Ensure the installation path (e.g., `C:\Program Files\Tesseract-OCR`) is added to your system's `PATH` environment variable.
- **macOS:** `brew install tesseract`
- **Linux (Ubuntu/Debian):** `sudo apt-get install tesseract-ocr`

**Install Python Dependencies and Start:**
```bash
cd ocr-service
pip install -r requirements.txt
python -m uvicorn main:app --reload --port 8000
```
The OCR service will start on **http://127.0.0.1:8000**.

### 4. Backend Setup (Spring Boot)

```bash
cd backend
mvn clean install -DskipTests
```

**Configuration (Local Development):**
The backend strictly requires several environment variables to start. Do NOT put your real secrets into version control. For local development, you must set these variables in your terminal before running the application. You can view `backend/.env.example` for a list of variables and safe placeholder values.

**Windows PowerShell:**
```powershell
$env:DB_PASSWORD="replace_with_your_local_mysql_password"
$env:JWT_SECRET="replace_with_your_local_jwt_secret_key"
$env:GOOGLE_CLIENT_ID="replace_with_your_google_client_id"
$env:GOOGLE_CLIENT_SECRET="replace_with_your_google_client_secret"
$env:OPENAI_API_KEY="replace_with_your_openai_api_key"

mvn spring-boot:run
```

**macOS / Linux:**
```bash
export DB_PASSWORD="replace_with_your_local_mysql_password"
export JWT_SECRET="replace_with_your_local_jwt_secret_key"
export GOOGLE_CLIENT_ID="replace_with_your_google_client_id"
export GOOGLE_CLIENT_SECRET="replace_with_your_google_client_secret"
export OPENAI_API_KEY="replace_with_your_openai_api_key"

mvn spring-boot:run
```

> **Note on Production:** In a production environment like Railway, these variables are configured directly in the deployment dashboard, ensuring secure isolation from the codebase.

The backend API will start at **http://localhost:8080/api**.

### 5. Frontend Setup (React/Vite)

```bash
cd frontend
npm install
```

**Configuration:**
Copy the environment template:
```bash
cp .env.example .env
```
Edit `.env` and fill in your Google OAuth Client ID (`VITE_GOOGLE_CLIENT_ID`).

**Start the Frontend:**
```bash
npm run dev
```
The frontend will start at **http://localhost:5173**.

---

## How to Test

Once all three services (Frontend, Backend, OCR Service) are running:

1. Open your browser and navigate to **http://localhost:5173**.
2. **Login:** Use the "Continue with Google" button.
3. **Roles:** Your role is determined by your email. To get Admin access, add your email to the `ADMIN_EMAILS` environment variable in the backend, or manually change your role in the `users` table in the database to `ROLE_ADMIN`.
4. **OCR Testing:** Navigate to the Admin Dashboard -> OCR Processing to test document uploads (requires the Python OCR service to be running).

---

## License

This project was built for **Hackathon 2026** by **Team VertoEdu**.
