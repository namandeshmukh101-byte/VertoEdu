const mysql = require('mysql2/promise');

async function run() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: process.env.DB_PASSWORD || 'root24',
    database: 'vertoedu'
  });
  
  await connection.execute("DELETE FROM students WHERE scholar_number = 'SCH-AUDIT-001'");
  await connection.execute("DELETE FROM schools WHERE name = 'Test School Audit'");
  console.log("Deleted stuck records");
  await connection.end();
}

run().catch(console.error);
