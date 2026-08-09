const mysql = require('mysql2/promise');

async function testDb() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: process.env.DB_PASSWORD || 'root24',
    database: 'VertoEdu'
  });

  try {
    console.log('\n--- USERS ---');
    const [users] = await connection.execute(`
      SELECT u.email, r.name as role 
      FROM users u JOIN roles r ON u.role_id = r.id 
      WHERE u.email IN ('clashclasher1124@gmail.com', 'clashclasher1102@gmail.com', 'parthdeshmukh167@gmail.com', 'workgpt678@gmail.com', 'regaltashwal@gmail.com')
    `);
    console.table(users);

    console.log('\n--- STUDENTS ---');
    const [students] = await connection.execute(`
      SELECT scholar_number, first_name, last_name, 
             (SELECT CONCAT(first_name, ' ', last_name) FROM parents p WHERE p.id = parent_id) as parent,
             (SELECT name FROM sections sec WHERE sec.id = section_id) as section
      FROM students 
      WHERE scholar_number LIKE 'TEST-SCH-1%'
      ORDER BY scholar_number
    `);
    console.table(students);

    console.log('\n--- TEACHERS AND SECTIONS ---');
    const [teachers] = await connection.execute(`
      SELECT u.email, t.employee_id, s.name as section
      FROM teachers t
      JOIN users u ON t.user_id = u.id
      JOIN teacher_sections ts ON ts.teacher_id = t.id
      JOIN sections s ON ts.section_id = s.id
      WHERE u.email LIKE '%gmail.com'
    `);
    console.table(teachers);

    console.log('\n--- EXECUTING DATABASE CONSTRAINT TEST ---');
    try {
      console.log('Inserting Student 1 (TEST-CONSTRAINT-001)...');
      await connection.execute(`
        INSERT INTO students (first_name, last_name, dob, scholar_number, school_id, created_at, updated_at)
        VALUES ('Const', 'Raint1', '2010-01-01', 'TEST-CONSTRAINT-001', 1, NOW(), NOW())
      `);
      console.log('Student 1 inserted successfully.');

      console.log('Inserting Student 2 with SAME scholar_number...');
      await connection.execute(`
        INSERT INTO students (first_name, last_name, dob, scholar_number, school_id, created_at, updated_at)
        VALUES ('Const', 'Raint2', '2011-02-02', 'TEST-CONSTRAINT-001', 1, NOW(), NOW())
      `);
      console.log('Student 2 inserted successfully (THIS SHOULD HAVE FAILED!)');
    } catch (err) {
      console.log('Student 2 insert failed AS EXPECTED. Error:', err.message);
      console.log('SQL State:', err.sqlState);
      console.log('Error Code:', err.code);
    } finally {
      console.log('Cleaning up constraint test records...');
      await connection.execute(`DELETE FROM students WHERE scholar_number = 'TEST-CONSTRAINT-001'`);
      console.log('Cleanup complete.');
    }
  } catch (err) {
    console.error('Database query failed:', err);
  } finally {
    await connection.end();
  }
}

testDb();
