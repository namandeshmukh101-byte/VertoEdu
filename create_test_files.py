import os
from PIL import Image, ImageDraw, ImageFont

def create_image(filename, text, format):
    img = Image.new('RGB', (800, 600), color=(255, 255, 255))
    d = ImageDraw.Draw(img)
    try:
        font = ImageFont.truetype("arial.ttf", 36)
    except IOError:
        font = ImageFont.load_default()
    d.text((50, 50), text, fill=(0, 0, 0), font=font)
    img.save(filename, format=format)

if __name__ == '__main__':
    text = """
    Admission Form
    First Name: Test
    Last Name: Student
    Date of Birth: 2010-01-01
    Grade Level: 10
    Address: 123 Test St
    Parent Name: Test Parent
    Contact Phone: 555-1234
    """
    create_image('test_doc.jpg', text, 'JPEG')
    create_image('test_doc.png', text, 'PNG')
    
    try:
        from reportlab.pdfgen import canvas
        c = canvas.Canvas("test_doc.pdf")
        textobject = c.beginText()
        textobject.setTextOrigin(50, 750)
        textobject.setFont("Helvetica", 14)
        for line in text.strip().split('\n'):
            textobject.textLine(line.strip())
        c.drawText(textobject)
        c.save()
        print("Created PDF successfully.")
    except ImportError:
        print("reportlab not installed. Will just install it and run again.")
