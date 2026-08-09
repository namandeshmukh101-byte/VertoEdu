const mysql = require('mysql2/promise');

async function testConstraint() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: process.env.DB_PASSWORD || 'root24',
    database: 'VertoEdu'
  });

  try {
    console.log("--- EXECUTING DATABASE CONSTRAINT TEST ---");
    
    // Attempt 1: Should succeed
    console.log("Inserting Student 1 (TEST-CONSTRAINT-001)...");
    await connection.execute(`
      INSERT INTO students (scholar_number, first_name, last_name, school_id, created_at, updated_at) 
      VALUES ('TEST-CONSTRAINT-001', 'Constraint', 'One', 1, NOW(), NOW())
    `);
    console.log("Student 1 inserted successfully.");

    // Attempt 2: Should fail
    console.log("Inserting Student 2 with SAME scholar_number...");
    await connection.execute(`
      INSERT INTO students (scholar_number, first_name, last_name, school_id, created_at, updated_at) 
      VALUES ('TEST-CONSTRAINT-001', 'Constraint', 'Two', 1, NOW(), NOW())
    `);
    
    console.log("❌ ERROR: Student 2 insertion succeeded but should have failed!");
  } catch (err) {
    console.log("Student 2 insert failed AS EXPECTED.");
    console.log("Error Message:", err.message);
    console.log("SQL State:", err.sqlState);
    console.log("Error Code:", err.code);
  } finally {
    console.log("Cleaning up constraint test records...");
    await connection.execute(`DELETE FROM students WHERE scholar_number = 'TEST-CONSTRAINT-001'`);
    console.log("Cleanup complete.");
    await connection.end();
  }
}

testConstraint();
