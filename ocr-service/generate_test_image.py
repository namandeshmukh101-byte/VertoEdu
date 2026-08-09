from PIL import Image, ImageDraw, ImageFont

img = Image.new('RGB', (800, 400), color=(255, 255, 255))
d = ImageDraw.Draw(img)

text = """
Verto Education Admission Form

Name: Test Student
Date of Birth: 2010-05-15
Grade: 10
Address: 456 Demo Way
Parent Name: John Doe
Contact Phone: 555-1234
"""

d.text((50, 50), text, fill=(0, 0, 0))
img.save('test_admission.png')
print("Image saved as test_admission.png")
