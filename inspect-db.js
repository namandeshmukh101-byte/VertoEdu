const mysql = require('mysql2/promise');

async function inspectDb() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: process.env.DB_PASSWORD || 'root24',
    database: 'VertoEdu'
  });

  try {
    console.log('\n--- CLASSES & SECTIONS ---');
    const [sections] = await connection.execute(`
      SELECT c.id as class_id, c.name as class_name, s.id as section_id, s.name as section_name
      FROM school_classes c
      JOIN sections s ON s.school_class_id = c.id
    `);
    console.table(sections);

    console.log('\n--- PARENTS ---');
    const [parents] = await connection.execute(`
      SELECT p.id as parent_id, u.email
      FROM parents p
      JOIN users u ON p.user_id = u.id
    `);
    console.table(parents);

  } catch (err) {
    console.error('Database query failed:', err);
  } finally {
    await connection.end();
  }
}

inspectDb();
