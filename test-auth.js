const TOKENS = {
  "Teacher 1 (clashclasher1102)": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVEVBQ0hFUiIsInVzZXJJZCI6Miwic3ViIjoiY2xhc2hjbGFzaGVyMTEwMkBnbWFpbC5jb20iLCJpYXQiOjE3ODYyOTA1OTUsImV4cCI6MTc4NjM3Njk5NX0.momA8AnJJmnAT1ezPspea46qV0kytZdYvZ5m527-WfA",
  "Teacher 2 (parthdeshmukh167)": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVEVBQ0hFUiIsInVzZXJJZCI6Mywic3ViIjoicGFydGhkZXNobXVraDE2N0BnbWFpbC5jb20iLCJpYXQiOjE3ODYyOTA1OTYsImV4cCI6MTc4NjM3Njk5Nn0.pPuA_6bLP5x_JmWbF5qqd0yxFbVfHtOMb4s8LcxYK6A",
  "Parent 1 (workgpt678)": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUEFSRU5UIiwidXNlcklkIjo0LCJzdWIiOiJ3b3JrZ3B0Njc4QGdtYWlsLmNvbSIsImlhdCI6MTc4NjI5MDU5NiwiZXhwIjoxNzg2Mzc2OTk2fQ.Rl1MdiyxmOA17T9uWI7rf2qEMXhpSr3-eSoF5w2k-n4",
  "Parent 2 (regaltashwal)": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUEFSRU5UIiwidXNlcklkIjo1LCJzdWIiOiJyZWdhbHRhc2h3YWxAZ21haWwuY29tIiwiaWF0IjoxNzg2MjkwNTk2LCJleHAiOjE3ODYzNzY5OTZ9.Cxftu9Mz662zVZbMWq7uroRygutz36QP67Bv--dtxI8"
};

const STUDENTS = Array.from({ length: 11 }, (_, i) => `TEST-SCH-100${(i + 1).toString().padStart(2, '0')}`);

async function testAuth() {
  console.log("--- API AUTHORIZATION REGRESSION TEST ---");
  for (const [user, token] of Object.entries(TOKENS)) {
    console.log(`\nTesting as ${user}:`);
    for (const student of STUDENTS) {
      try {
        const res = await fetch(`http://localhost:8080/api/search/students?scholarNumber=${student}`, {
          headers: {
            "Cookie": `jwt=${token}`
          }
        });
        
        let status = res.status;
        console.log(`  ${student} -> ${status} ${status === 200 ? '✅ ALLOW' : '❌ DENY'}`);
      } catch (err) {
        console.error(`  ${student} -> FAILED TO FETCH`, err.message);
      }
    }
  }
}

testAuth();
