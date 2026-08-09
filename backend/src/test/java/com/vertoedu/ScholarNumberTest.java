package com.vertoedu;

import com.vertoedu.entity.School;
import com.vertoedu.entity.Student;
import com.vertoedu.repository.SchoolRepository;
import com.vertoedu.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ScholarNumberTest {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private SchoolRepository schoolRepository;

    private School testSchool;

    @BeforeEach
    public void setup() {
        testSchool = new School();
        testSchool.setName("Test School Audit");
        testSchool = schoolRepository.save(testSchool);
    }

    @Test
    public void testDuplicateScholarNumber_Rejected() {
        Student student1 = new Student();
        student1.setFirstName("Audit");
        student1.setLastName("One");
        student1.setScholarNumber("SCH-AUDIT-001");
        student1.setSchool(testSchool);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setFirstName("Audit");
        student2.setLastName("Two");
        student2.setScholarNumber("SCH-AUDIT-001"); // Duplicate
        student2.setSchool(testSchool);

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            studentRepository.save(student2);
        });
        
        System.out.println("EVIDENCE: Duplicate scholar number blocked successfully: " + exception.getMessage());
    }
}
