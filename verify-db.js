const mysql = require('mysql2/promise');

async function verifyDb() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: process.env.DB_PASSWORD || 'root24',
    database: 'VertoEdu'
  });

  try {
    console.log('\n--- STUDENT MATRIX ---');
    const [students] = await connection.execute(`
      SELECT 
        s.scholar_number, 
        CONCAT(s.first_name, ' ', s.last_name) as student_name, 
        c.name as class, 
        sec.name as section, 
        IFNULL(u.email, 'NO PARENT') as parent_email
      FROM students s
      LEFT JOIN parents p ON s.parent_id = p.id
      LEFT JOIN users u ON p.user_id = u.id
      LEFT JOIN sections sec ON s.section_id = sec.id
      LEFT JOIN school_classes c ON sec.school_class_id = c.id
      WHERE s.scholar_number LIKE 'TEST-SCH-1%'
      ORDER BY s.scholar_number
    `);
    console.table(students);

    console.log('\n--- TEACHER MATRIX ---');
    const [teachers] = await connection.execute(`
      SELECT 
        u.email as teacher_email, 
        c.name as class, 
        sec.name as section
      FROM teachers t
      JOIN users u ON t.user_id = u.id
      LEFT JOIN teacher_sections ts ON ts.teacher_id = t.id
      LEFT JOIN sections sec ON ts.section_id = sec.id
      LEFT JOIN school_classes c ON sec.school_class_id = c.id
      WHERE u.email IN ('clashclasher1102@gmail.com', 'parthdeshmukh167@gmail.com')
      ORDER BY u.email, c.name DESC, sec.name
    `);
    console.table(teachers);

    console.log('\n--- PARENT COUNTS ---');
    const [parents] = await connection.execute(`
      SELECT 
        u.email as parent_email, 
        COUNT(s.id) as child_count
      FROM parents p
      JOIN users u ON p.user_id = u.id
      LEFT JOIN students s ON s.parent_id = p.id
      WHERE u.email IN ('workgpt678@gmail.com', 'regaltashwal@gmail.com')
      GROUP BY p.id, u.email
    `);
    console.table(parents);

    console.log('\n--- VERIFY parthdeshmukh167@gmail.com ---');
    const [parthUser] = await connection.execute(`
      SELECT r.name as role FROM users u JOIN roles r ON u.role_id = r.id WHERE u.email = 'parthdeshmukh167@gmail.com'
    `);
    const [parthParent] = await connection.execute(`
      SELECT * FROM parents p JOIN users u ON p.user_id = u.id WHERE u.email = 'parthdeshmukh167@gmail.com'
    `);
    
    console.log(`Role in users table: ${parthUser[0]?.role}`);
    console.log(`Exists in parents table: ${parthParent.length > 0 ? 'YES' : 'NO'}`);
    
    if (parthUser[0]?.role === 'TEACHER' && parthParent.length === 0) {
      console.log('✅ EXPLICIT VERIFICATION PASSED: parthdeshmukh167@gmail.com is TEACHER and NOT PARENT.');
    } else {
      console.log('❌ EXPLICIT VERIFICATION FAILED.');
    }

  } catch (err) {
    console.error('Database query failed:', err);
  } finally {
    await connection.end();
  }
}

verifyDb();
