package com.vertoedu;

import com.vertoedu.entity.*;
import com.vertoedu.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseIntegrityClosureTest {

    @Autowired private StudentRepository studentRepository;
    @Autowired private SchoolRepository schoolRepository;
    @Autowired private ParentRepository parentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private OCRResultRepository ocrResultRepository;
    @Autowired private DocumentUploadRepository documentUploadRepository;
    @Autowired private AIReviewRepository aiReviewRepository;

    private Long testSchoolId;
    private Long testStudentId;
    private Long testParentId;
    private Long testUserId;
    private Long testDocumentId;

    @BeforeEach
    public void setup() {
        System.out.println("=== SETUP: Creating Test Data ===");
        
        School school = new School();
        school.setName("Integrity Test School " + UUID.randomUUID());
        school = schoolRepository.saveAndFlush(school);
        testSchoolId = school.getId();

        Role parentRole = roleRepository.findByName("PARENT").orElseThrow();
        User user = new User();
        user.setEmail("integrity" + UUID.randomUUID() + "@test.com");
        user.setFullName("Test Integrity User");
        user.setRole(parentRole);
        user = userRepository.saveAndFlush(user);
        testUserId = user.getId();

        Parent parent = new Parent();
        parent.setUser(user);
        parent.setSchool(school);
        parent.setFirstName("Test");
        parent.setLastName("Parent");
        parent = parentRepository.saveAndFlush(parent);
        testParentId = parent.getId();

        Student student = new Student();
        student.setSchool(school);
        student.setFirstName("Test");
        student.setLastName("Student");
        student.setScholarNumber("SCH-INT-" + UUID.randomUUID().toString().substring(0, 8));
        student = studentRepository.saveAndFlush(student);
        testStudentId = student.getId();
        
        DocumentUpload doc = new DocumentUpload();
        doc.setFileName("test.pdf");
        doc.setFilePath("/tmp/test.pdf");
        doc.setDocumentType(com.vertoedu.entity.enums.DocumentType.ADMISSION_FORM);
        doc.setUploadedBy(user);
        doc = documentUploadRepository.saveAndFlush(doc);
        testDocumentId = doc.getId();
    }

    @AfterEach
    public void cleanup() {
        System.out.println("=== CLEANUP: Removing Test Data ===");
        // Manual cleanup to prove deletion
        
        aiReviewRepository.deleteAll(); // Cleanup AI reviews first
        ocrResultRepository.deleteAll(); // Cleanup OCR results
        documentUploadRepository.deleteById(testDocumentId);
        
        studentRepository.deleteById(testStudentId);
        parentRepository.deleteById(testParentId);
        userRepository.deleteById(testUserId);
        schoolRepository.deleteById(testSchoolId);

        // Prove deletion
        boolean studentExists = studentRepository.existsById(testStudentId);
        System.out.println("EVIDENCE: Test Student Exists after cleanup? " + studentExists);
        assertFalse(studentExists, "Student was not cleaned up!");
    }

    @Test
    public void testDuplicateScholarNumber_Rejected() {
        Student existing = studentRepository.findById(testStudentId).orElseThrow();
        
        System.out.println("Executing targeted manual scholarNumber constraint test...");
        
        Student duplicate = new Student();
        duplicate.setSchool(existing.getSchool());
        duplicate.setFirstName("Valid");
        duplicate.setLastName("Student");
        duplicate.setScholarNumber(existing.getScholarNumber()); // Duplicate!

        DataIntegrityViolationException exception = assertThrows(
            DataIntegrityViolationException.class, 
            () -> studentRepository.saveAndFlush(duplicate)
        );
        
        // Print the full cause tree to capture exact MySQL error
        Throwable rootCause = exception;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        System.out.println("EVIDENCE: Duplicate Scholar Number rejected!");
        System.out.println("EVIDENCE Exact MySQL Constraint Error: " + rootCause.getMessage());
    }

    @Test
    public void testDuplicateOCRResult_Rejected() {
        DocumentUpload doc = documentUploadRepository.findById(testDocumentId).orElseThrow();
        
        OCRResult res1 = new OCRResult();
        res1.setDocumentUpload(doc);
        res1.setRawText("text1");
        res1.setExtractedDataJson("{}");
        ocrResultRepository.saveAndFlush(res1);

        OCRResult res2 = new OCRResult();
        res2.setDocumentUpload(doc); // Duplicate document_upload_id
        res2.setRawText("text2");
        res2.setExtractedDataJson("{}");

        DataIntegrityViolationException exception = assertThrows(
            DataIntegrityViolationException.class,
            () -> ocrResultRepository.saveAndFlush(res2)
        );
        System.out.println("EVIDENCE: Duplicate OCRResult rejected! " + exception.getMessage());
    }

    @Test
    public void testDuplicateAIReview_Rejected() {
        DocumentUpload doc = documentUploadRepository.findById(testDocumentId).orElseThrow();
        
        OCRResult ocr = new OCRResult();
        ocr.setDocumentUpload(doc);
        ocr.setRawText("text1");
        ocr.setExtractedDataJson("{}");
        ocr = ocrResultRepository.saveAndFlush(ocr);

        AIReview ai1 = new AIReview();
        ai1.setOcrResult(ocr);
        ai1.setSuggestedDataJson("{}");
        aiReviewRepository.saveAndFlush(ai1);

        AIReview ai2 = new AIReview();
        ai2.setOcrResult(ocr); // Duplicate ocr_result_id
        ai2.setSuggestedDataJson("{}");

        DataIntegrityViolationException exception = assertThrows(
            DataIntegrityViolationException.class,
            () -> aiReviewRepository.saveAndFlush(ai2)
        );
        System.out.println("EVIDENCE: Duplicate AIReview rejected! " + exception.getMessage());
    }

    @Autowired
    private org.springframework.transaction.support.TransactionTemplate transactionTemplate;

    @Test
    public void testTransactionRollbackOnPartialFailure() {
        System.out.println("Executing transactional rollback test...");
        long countBefore = studentRepository.count();
        
        try {
            transactionTemplate.execute(status -> {
                School school = schoolRepository.findById(testSchoolId).orElseThrow();
                Student s = new Student();
                s.setSchool(school);
                s.setFirstName("Fail");
                s.setLastName("Student");
                s.setScholarNumber("SCH-TX-" + UUID.randomUUID().toString().substring(0, 8));
                studentRepository.save(s);
                
                throw new RuntimeException("Intentional failure to trigger rollback");
            });
        } catch (RuntimeException e) {
            System.out.println("Caught expected exception during transaction: " + e.getMessage());
        }
        
        long countAfter = studentRepository.count();
        assertEquals(countBefore, countAfter, "Student count should be identical; transaction failed to rollback!");
        System.out.println("EVIDENCE: Transaction completely rolled back! Count remained at " + countBefore);
    }
}
