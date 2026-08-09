# VertoEdu

> **Where AI Meets Education**

AI-Powered School Operations Platform designed to modernize and automate school administration — built for Hackathon 2026 by Team VertoEdu.

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
| HTTP Client    | Axios                               |
| Routing        | React Router DOM                    |
| Backend        | Spring Boot 3.4                     |
| Security       | Spring Security + Google OAuth      |
| ORM            | Spring Data JPA (Hibernate)         |
| Database       | MySQL                               |
| AI             | OpenAI API                          |
| OCR            | OCR Engine                          |
| Build Tool     | Maven (backend) / Vite (frontend)   |

---

## Folder Structure

```
VertoEdu/
├── frontend/                  # React + Vite application
│   ├── public/                # Static assets (favicon, etc.)
│   └── src/
│       ├── assets/            # Images, icons, media
│       ├── components/        # Reusable UI components
│       │   └── ui/            # shadcn/ui components
│       ├── hooks/             # Custom React hooks
│       ├── layouts/           # Page layout wrappers
│       ├── lib/               # Utility libraries (cn, etc.)
│       ├── pages/             # Page-level components
│       ├── services/          # API service layer (Axios)
│       ├── utils/             # Shared helper functions
│       ├── App.jsx            # Root component with routing
│       ├── main.jsx           # Application entry point
│       └── index.css          # Global styles + Tailwind
│
├── backend/                   # Spring Boot application
│   └── src/
│       ├── main/
│       │   ├── java/com/vertoedu/
│       │   │   ├── config/        # Configuration classes
│       │   │   ├── controller/    # REST API controllers
│       │   │   ├── dto/           # Data Transfer Objects
│       │   │   ├── entity/        # JPA entities
│       │   │   ├── exception/     # Custom exceptions
│       │   │   ├── repository/    # Data access layer
│       │   │   ├── security/      # Security configuration
│       │   │   ├── service/       # Business logic
│       │   │   └── util/          # Helper utilities
│       │   └── resources/
│       │       └── application.properties
│       └── test/                  # Unit & integration tests
│
├── docs/                      # Project documentation
├── prompts/                   # AI prompt scripts
└── README.md                  # This file
```

---

## Prerequisites

- **Node.js** ≥ 18.x and **npm** ≥ 9.x
- **Java** ≥ 17 (JDK)
- **Maven** ≥ 3.9
- **MySQL** ≥ 8.0

---

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd VertoEdu
```

### 2. Frontend Setup

```bash
cd frontend
npm install
```

Copy the environment template:

```bash
cp .env.example .env
```

Edit `.env` and fill in the required values.

### 3. Backend Setup

```bash
cd backend
mvn clean install -DskipTests
```

Copy the environment template:

```bash
cp src/main/resources/application.properties.example src/main/resources/application-local.properties
```

Edit `application-local.properties` with your database credentials.

---

## How to Run

### Frontend (React + Vite)

```bash
cd frontend
npm run dev
```

The frontend will start at **http://localhost:5173**.

### Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

The backend API will start at **http://localhost:8080/api**.

---

## Environment Variables

### Frontend (`.env`)

| Variable              | Description                |
|-----------------------|----------------------------|
| `VITE_API_BASE_URL`   | Backend API base URL       |
| `VITE_GOOGLE_CLIENT_ID` | Google OAuth Client ID   |

### Backend (`application.properties`)

| Variable              | Description                |
|-----------------------|----------------------------|
| `DB_URL`              | MySQL connection URL       |
| `DB_USERNAME`         | MySQL username             |
| `DB_PASSWORD`         | MySQL password             |
| `GOOGLE_CLIENT_ID`    | Google OAuth Client ID     |
| `GOOGLE_CLIENT_SECRET`| Google OAuth Client Secret |
| `OPENAI_API_KEY`      | OpenAI API Key             |
| `OCR_API_KEY`         | OCR Engine API Key         |
| `JWT_SECRET`          | JWT signing secret         |

---

## License

This project was built for **Hackathon 2026** by **Team VertoEdu**.
