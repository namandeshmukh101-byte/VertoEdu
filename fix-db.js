const mysql = require('mysql2/promise');

async function fixDb() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: process.env.DB_PASSWORD || 'root24',
    database: 'VertoEdu'
  });

  try {
    await connection.beginTransaction();

    console.log('--- SAFETY CHECK ---');
    const [parent81] = await connection.execute(`
      SELECT p.id, u.email 
      FROM parents p 
      JOIN users u ON p.user_id = u.id 
      WHERE p.id = 81
    `);
    
    if (parent81.length === 0) {
      console.log('parent_id 81 does not exist. It may have already been cleaned up.');
    } else {
      console.log(`Found parent_id = 81 with email: ${parent81[0].email}`);
      if (parent81[0].email !== 'parthdeshmukh167@gmail.com') {
        throw new Error('SAFETY ABORT: parent_id 81 is not parthdeshmukh167@gmail.com');
      }

      const [studentsRef81] = await connection.execute(`
        SELECT scholar_number FROM students WHERE parent_id = 81
      `);
      console.log(`Students referencing parent_id 81:`, studentsRef81.map(s => s.scholar_number));
      
      const nonTest = studentsRef81.filter(s => !s.scholar_number.startsWith('TEST-SCH-'));
      if (nonTest.length > 0) {
        throw new Error('SAFETY ABORT: non-test students referencing parent_id 81');
      }

      console.log('Safety check passed. Unlinking test students and removing invalid parent record...');
      await connection.execute(`UPDATE students SET parent_id = NULL WHERE parent_id = 81 AND scholar_number LIKE 'TEST-SCH-%'`);
      await connection.execute(`DELETE FROM parents WHERE id = 81`);
      console.log('Invalid parent 81 removed.');
    }

    console.log('--- APPLYING APPROVED STUDENT MATRIX ---');
    
    // Parent 1 (workgpt678@gmail.com) -> ID 55
    // Section 10-A -> ID 2
    await connection.execute(`
      UPDATE students SET parent_id = 55, section_id = 2 
      WHERE scholar_number IN ('TEST-SCH-10001', 'TEST-SCH-10002', 'TEST-SCH-10003')
    `);

    // Parent 2 (regaltashwal@gmail.com) -> ID 56
    // Section 10-B -> ID 3
    await connection.execute(`
      UPDATE students SET parent_id = 56, section_id = 3 
      WHERE scholar_number IN ('TEST-SCH-10004', 'TEST-SCH-10005')
    `);

    // No parent, Section 10-B -> ID 3
    await connection.execute(`
      UPDATE students SET parent_id = NULL, section_id = 3 
      WHERE scholar_number = 'TEST-SCH-10006'
    `);

    // No parent, Section 9-A -> ID 4
    await connection.execute(`
      UPDATE students SET parent_id = NULL, section_id = 4 
      WHERE scholar_number IN ('TEST-SCH-10007', 'TEST-SCH-10008')
    `);

    // No parent, Section 9-B -> ID 5
    await connection.execute(`
      UPDATE students SET parent_id = NULL, section_id = 5 
      WHERE scholar_number IN ('TEST-SCH-10009', 'TEST-SCH-10010', 'TEST-SCH-10011')
    `);

    await connection.commit();
    console.log('Database correction committed successfully.');
  } catch (err) {
    await connection.rollback();
    console.error('Database correction failed. Rolled back.', err);
  } finally {
    await connection.end();
  }
}

fixDb();
