import os
from PIL import Image, ImageDraw, ImageFont

def generate_image(filename, text, format):
    img = Image.new('RGB', (400, 200), color = (255, 255, 255))
    d = ImageDraw.Draw(img)
    # Use default font
    d.text((10,10), text, fill=(0,0,0))
    img.save(filename, format=format)
    print(f"Generated {filename}")

generate_image("test_image.jpg", "This is a test JPEG document for OCR.", "JPEG")
generate_image("test_image.png", "This is a test PNG document for OCR.", "PNG")

# Generate a PDF using fpdf if available, or just convert image to PDF
try:
    img = Image.new('RGB', (400, 200), color = (255, 255, 255))
    d = ImageDraw.Draw(img)
    d.text((10,10), "This is a test PDF document for OCR.", fill=(0,0,0))
    img.save("test_document.pdf", "PDF", resolution=100.0)
    print("Generated test_document.pdf")
except Exception as e:
    print("PDF generation failed:", e)
