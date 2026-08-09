import io
import os
import cv2
import numpy as np
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.responses import JSONResponse
from PIL import Image
import pytesseract
import fitz  # PyMuPDF

app = FastAPI(title="VertoEdu OCR Service")

# Optional: Ensure pytesseract points to the correct executable if not in PATH
# e.g., pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'
TESSERACT_CMD = os.getenv("TESSERACT_CMD")
if TESSERACT_CMD:
    pytesseract.pytesseract.tesseract_cmd = TESSERACT_CMD

@app.get("/health")
def health_check():
    # Attempt a trivial Tesseract command to verify it is installed
    try:
        tess_version = pytesseract.get_tesseract_version()
        return {"status": "UP", "tesseract_version": str(tess_version)}
    except Exception as e:
        return {"status": "DOWN", "error": f"Tesseract not installed or not in PATH. Details: {str(e)}"}


def preprocess_image(image: Image.Image) -> Image.Image:
    """Apply grayscale and thresholding to improve OCR accuracy."""
    # Convert PIL Image to OpenCV format
    cv_image = np.array(image.convert('RGB'))
    cv_image = cv_image[:, :, ::-1].copy()  # RGB to BGR

    # Grayscale
    gray = cv2.cvtColor(cv_image, cv2.COLOR_BGR2GRAY)
    
    # Thresholding
    _, thresh = cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)
    
    # Convert back to PIL
    return Image.fromarray(thresh)


def process_pdf(file_bytes: bytes) -> str:
    """Extracts text from PDF by converting pages to images and running OCR."""
    text_content = []
    try:
        pdf_document = fitz.open("pdf", file_bytes)
        for page_num in range(len(pdf_document)):
            page = pdf_document.load_page(page_num)
            pix = page.get_pixmap(dpi=200)
            img_data = pix.tobytes("png")
            img = Image.open(io.BytesIO(img_data))
            
            # Preprocess and OCR
            processed_img = preprocess_image(img)
            text = pytesseract.image_to_string(processed_img)
            text_content.append(text)
            
        pdf_document.close()
        return "\n".join(text_content)
    except Exception as e:
        raise ValueError(f"Failed to process PDF: {str(e)}")

def process_image(file_bytes: bytes) -> str:
    try:
        img = Image.open(io.BytesIO(file_bytes))
        processed_img = preprocess_image(img)
        text = pytesseract.image_to_string(processed_img)
        return text
    except Exception as e:
        raise ValueError(f"Failed to process Image: {str(e)}")


@app.post("/ocr")
async def process_ocr(file: UploadFile = File(...)):
    if not file:
        raise HTTPException(status_code=400, detail="No file uploaded")

    content = await file.read()
    if not content:
        raise HTTPException(status_code=400, detail="Empty file uploaded")

    filename = file.filename.lower()
    
    try:
        if filename.endswith(".pdf"):
            extracted_text = process_pdf(content)
        elif filename.endswith((".png", ".jpg", ".jpeg")):
            extracted_text = process_image(content)
        else:
            raise HTTPException(status_code=400, detail=f"Unsupported file format: {filename}")
            
        return JSONResponse(status_code=200, content={
            "success": True,
            "rawText": extracted_text.strip(),
            "filename": file.filename
        })
        
    except ValueError as ve:
        raise HTTPException(status_code=400, detail=str(ve))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"OCR processing failed: {str(e)}")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
