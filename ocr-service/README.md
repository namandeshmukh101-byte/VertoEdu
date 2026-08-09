# VertoEdu Python OCR Service

This is a lightweight FastAPI microservice responsible for extracting text from admission documents using Tesseract OCR.

## Prerequisites

1. **Python 3.9+**
2. **Tesseract OCR Engine**
   - **Windows**: Download and install from [UB-Mannheim/tesseract/wiki](https://github.com/UB-Mannheim/tesseract/wiki). 
     - If Tesseract is not automatically added to your system `PATH`, you must set the environment variable `TESSERACT_CMD` to the executable path (e.g., `C:\Program Files\Tesseract-OCR\tesseract.exe`).
   - **Linux**: `sudo apt-get install tesseract-ocr`
   - **Mac**: `brew install tesseract`

## Installation

1. Create a virtual environment:
   ```bash
   python -m venv venv
   ```
2. Activate the virtual environment:
   - Windows: `venv\Scripts\activate`
   - Mac/Linux: `source venv/bin/activate`
3. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

## Running the Service

Start the FastAPI service:
```bash
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

## Endpoints
- `GET /health` - Checks if the service is up and if Tesseract is properly configured.
- `POST /ocr` - Accepts a `multipart/form-data` upload (PNG, JPG, JPEG, PDF) and returns extracted text.
