const axios = require('axios');
const fs = require('fs');
const FormData = require('form-data');

const jwtToken = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGFzaGNsYXNoZXIxMTI0QGdtYWlsLmNvbSIsInJvbGUiOiJBRE1JTiIsInVzZXJJZCI6MSwiaWF0IjoxNzg2Mjg4NzE5LCJleHAiOjE3ODYzNzUxMTl9.xZ2seB4ds_YKGUxamtoEuOyyO1SHVDCKU_eeidyteel4DyzjZtFhj00p4ZYwVzy6kCbhlLXsccbD0323b9Kzxw';

async function runTest() {
  try {
    const formData = new FormData();
    formData.append('file', fs.createReadStream('test_doc.jpg'));
    formData.append('type', 'ADMISSION_FORM');

    console.log('Uploading document...');
    const uploadRes = await axios.post('http://localhost:8080/api/ocr/upload', formData, {
      headers: {
        ...formData.getHeaders(),
        Cookie: `jwt=${jwtToken}`
      }
    });

    console.log('Upload Success:', uploadRes.data);
    const docId = uploadRes.data.id;

    console.log(`Triggering OCR process for doc ${docId}...`);
    const ocrRes = await axios.post(`http://localhost:8080/api/ocr/process/${docId}`, {}, {
      headers: { Cookie: `jwt=${jwtToken}` }
    });
    
    console.log('OCR Success:', ocrRes.data);

    console.log(`Triggering AI review for doc ${docId}...`);
    try {
      const aiRes = await axios.post(`http://localhost:8080/api/ai/suggest/${docId}`, {}, {
        headers: { Cookie: `jwt=${jwtToken}` }
      });
      console.log('AI Review Success:', aiRes.data);
    } catch (aiErr) {
      console.log('AI Review Failed with status:', aiErr.response ? aiErr.response.status : 'No response');
      if (aiErr.response) {
        console.log('Response body:', aiErr.response.data);
      } else {
        console.log(aiErr);
      }
    }
  } catch (err) {
    console.error('Test Failed:', err.response ? err.response.data : err.message);
  }
}

runTest();
