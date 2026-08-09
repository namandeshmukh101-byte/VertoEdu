const fs = require('fs');
const jwt = require('jsonwebtoken');

const SECRET = 'vertoedu_super_secret_key_for_development_2026_abcdefghijklmnopqrstuvwxyz123456';

// Generate JWT token for admin
const token = jwt.sign(
    { role: 'ADMIN', userId: 1 }, 
    SECRET,
    { subject: 'teacher@vertoedu.com', expiresIn: '1d' } 
);

console.log("Generated Test JWT Token:", token);

async function runE2E() {
    const baseUrl = 'http://localhost:8080/api/api';
    
    // We send token in Cookie header
    const authHeaders = {
        'Cookie': `jwt=${token}`
    };

    // 2. Upload Document
    console.log("\n--- 1. Uploading Document ---");
    const formData = new FormData();
    const fileBlob = new Blob([fs.readFileSync('../ocr-service/test_admission.png')], { type: 'image/png' });
    formData.append("file", fileBlob, "test_admission.png");
    formData.append("type", "ADMISSION_FORM");

    const uploadRes = await fetch(`${baseUrl}/ocr/upload`, {
        method: 'POST',
        headers: authHeaders,
        body: formData
    });
    const uploadData = await uploadRes.json();
    console.log("Upload Status:", uploadRes.status);
    console.log("Upload Data:", uploadData);
    const docId = uploadData.id;
    console.log("Document ID:", docId);

    // 3. Process OCR
    console.log("\n--- 2. Processing Real OCR ---");
    const ocrRes = await fetch(`${baseUrl}/ocr/process/${docId}`, {
        method: 'POST',
        headers: authHeaders
    });
    const ocrData = await ocrRes.json();
    console.log("OCR Result Status:", ocrRes.status);
    console.log("OCR Extracted Text:\n" + ocrData.rawText);

    // 4. Process AI
    console.log("\n--- 3. Processing Real AI ---");
    const aiRes = await fetch(`${baseUrl}/ai/suggest/${docId}`, {
        method: 'POST',
        headers: authHeaders
    });
    const aiData = await aiRes.json();
    console.log("AI Review Status:", aiRes.status);
    console.log("AI Suggestions:\n" + aiData.suggestionsText);
    console.log("AI Suggested Data:\n" + JSON.stringify(aiData.suggestedData, null, 2));

    // 5. Approve
    console.log("\n--- 4. Approving Document ---");
    const approvePayload = {
        documentUploadId: docId,
        isApproved: true,
        finalApprovedData: aiData.suggestedData
    };
    const approveRes = await fetch(`${baseUrl}/approval/submit`, {
        method: 'POST',
        headers: { 
            ...authHeaders,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(approvePayload)
    });
    const approveData = await approveRes.json();
    console.log("Approval Status:", approveRes.status);
    console.log("Approval Message:", approveData.message);
    
    // 6. Reject Test
    console.log("\n--- 5. Testing Rejection Flow ---");
    const uploadRes2 = await fetch(`${baseUrl}/ocr/upload`, {
        method: 'POST',
        headers: authHeaders,
        body: formData
    });
    const uploadData2 = await uploadRes2.json();
    const docId2 = uploadData2.id;
    
    await fetch(`${baseUrl}/ocr/process/${docId2}`, {
        method: 'POST',
        headers: authHeaders
    });
    
    const rejectPayload = {
        documentUploadId: docId2,
        isApproved: false,
        rejectionReason: "Missing critical information"
    };
    const rejectRes = await fetch(`${baseUrl}/approval/submit`, {
        method: 'POST',
        headers: { 
            ...authHeaders,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(rejectPayload)
    });
    const rejectData = await rejectRes.json();
    console.log("Rejection Status:", rejectRes.status);
    console.log("Rejection Message:", rejectData.message);

    console.log("\nE2E Pipeline Tests Complete.");
}

runE2E().catch(console.error);
