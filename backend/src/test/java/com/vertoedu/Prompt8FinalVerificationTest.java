package com.vertoedu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertoedu.entity.*;
import com.vertoedu.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class Prompt8FinalVerificationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private ParentRepository parentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private DataSource dataSource;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void executeAllVerifications() throws Exception {
        System.out.println("============================================================");
        System.out.println("1. TEST ENVIRONMENT");
        System.out.println("============================================================");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println("MySQL Version: " + metaData.getDatabaseProductVersion());
            System.out.println("Database connectivity: SUCCESS. URL: " + metaData.getURL());
        }

        System.out.println("============================================================");
        System.out.println("2. ADMIN ACCOUNT");
        System.out.println("============================================================");
        User admin = userRepository.findByEmail("clashclasher1124@gmail.com").orElse(null);
        if (admin != null) {
            System.out.println("User ID: " + admin.getId());
            System.out.println("Email: " + admin.getEmail());
            System.out.println("Role: " + admin.getRole().getName());
        }

        System.out.println("============================================================");
        System.out.println("3. TEST USERS");
        System.out.println("============================================================");
        String[] testEmails = {"clashclasher1102@gmail.com", "parthdeshmukh167@gmail.com", "workgpt678@gmail.com", "regaltashwal@gmail.com"};
        for (String email : testEmails) {
            User u = userRepository.findByEmail(email).orElse(null);
            if (u != null) {
                System.out.println(email + " -> " + u.getRole().getName() + " (DB ID: " + u.getId() + ")");
            }
        }

        System.out.println("============================================================");
        System.out.println("4. TEST STUDENTS");
        System.out.println("============================================================");
        System.out.println("DB ID | Name | Scholar Number | Class | Section | Parent");
        List<Student> students = studentRepository.findAll();
        for (int i = 1; i <= 11; i++) {
            String scholarNumber = String.format("TEST-SCH-%05d", i);
            Student s = students.stream().filter(st -> st.getScholarNumber().equals(scholarNumber)).findFirst().orElse(null);
            if (s != null) {
                String className = s.getSchoolClass() != null ? s.getSchoolClass().getName() : "N/A";
                String parentEmail = s.getParent() != null ? s.getParent().getUser().getEmail() : "N/A";
                System.out.println(s.getId() + " | " + s.getFirstName() + " " + s.getLastName() + " | " + s.getScholarNumber() + " | " + className + " | " + className + " | " + parentEmail);
            }
        }

        System.out.println("============================================================");
        System.out.println("5. TEACHER SECTION AUTHORIZATION");
        System.out.println("============================================================");
        // Will be tested in individual methods for @WithMockUser

        System.out.println("============================================================");
        System.out.println("9. PARENT LINK INTEGRITY");
        System.out.println("============================================================");
        Parent parent1 = parentRepository.findAll().stream().filter(p -> p.getUser().getEmail().equals("workgpt678@gmail.com")).findFirst().orElse(null);
        Parent parent2 = parentRepository.findAll().stream().filter(p -> p.getUser().getEmail().equals("regaltashwal@gmail.com")).findFirst().orElse(null);
        
        long p1Count = parent1 != null ? studentRepository.findAll().stream().filter(s -> s.getParent() != null && s.getParent().getId().equals(parent1.getId())).count() : 0;
        long p2Count = parent2 != null ? studentRepository.findAll().stream().filter(s -> s.getParent() != null && s.getParent().getId().equals(parent2.getId())).count() : 0;
        long noParentCount = studentRepository.findAll().stream().filter(s -> s.getParent() == null && s.getScholarNumber().startsWith("TEST-SCH-100")).count();
        
        System.out.println("Parent 1 (workgpt678@gmail.com) children count: " + p1Count);
        System.out.println("Parent 2 (regaltashwal@gmail.com) children count: " + p2Count);
        System.out.println("Students without parent (should be 6): " + noParentCount);
    }

    @Test
    @WithMockUser(username = "clashclasher1124@gmail.com", roles = "ADMIN")
    public void testDuplicateScholarNumber() throws Exception {
        System.out.println("============================================================");
        System.out.println("8. DUPLICATE SCHOLAR NUMBER");
        System.out.println("============================================================");
        String payload = "{\"schoolId\": 1, \"firstName\": \"Dup\", \"lastName\": \"Student\", \"scholarNumber\": \"TEST-SCH-10001\", \"dob\": \"2010-01-01\"}";
        int status = mockMvc.perform(post("/admin/students").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andReturn().getResponse().getStatus();
        System.out.println("HTTP status: " + status);
        System.out.println("Final count of TEST-SCH-10001: " + studentRepository.findAll().stream().filter(s -> s.getScholarNumber().equals("TEST-SCH-10001")).count());
    }

    @Test
    @WithMockUser(username = "clashclasher1102@gmail.com", roles = "TEACHER")
    public void testTeacher1Auth() throws Exception {
        System.out.println("============================================================");
        System.out.println("5. TEACHER SECTION AUTHORIZATION (Teacher 1)");
        System.out.println("============================================================");
        int status1 = mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10001")).andReturn().getResponse().getStatus();
        System.out.println("Teacher 1 (10-A, 9-B) searching TEST-SCH-10001 (10-A): HTTP " + status1);
        
        int status2 = mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10004")).andReturn().getResponse().getStatus();
        System.out.println("Teacher 1 (10-A, 9-B) searching TEST-SCH-10004 (10-B): HTTP " + status2);
    }

    @Test
    @WithMockUser(username = "workgpt678@gmail.com", roles = "PARENT")
    public void testParent1Auth() throws Exception {
        System.out.println("============================================================");
        System.out.println("6. PARENT AUTHORIZATION (Parent 1)");
        System.out.println("============================================================");
        int status1 = mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10001")).andReturn().getResponse().getStatus();
        System.out.println("Parent 1 searching TEST-SCH-10001 (Own child): HTTP " + status1);
        
        int status2 = mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10004")).andReturn().getResponse().getStatus();
        System.out.println("Parent 1 searching TEST-SCH-10004 (Other child): HTTP " + status2);
    }

    @Test
    @WithMockUser(username = "clashclasher1124@gmail.com", roles = "ADMIN")
    public void testFileSecurity() throws Exception {
        System.out.println("============================================================");
        System.out.println("11. FILE SECURITY");
        System.out.println("============================================================");
        
        MockMultipartFile validPdf = new MockMultipartFile("file", "test.pdf", "application/pdf", "%PDF-1.4...".getBytes());
        int statusPdf = mockMvc.perform(multipart("/admin/ocr/upload").file(validPdf).param("documentType", "ADMISSION_FORM")).andReturn().getResponse().getStatus();
        System.out.println("Valid PDF upload: HTTP " + statusPdf);

        MockMultipartFile fakeJpg = new MockMultipartFile("file", "test.jpg", "image/jpeg", "This is a text file".getBytes());
        int statusFakeJpg = mockMvc.perform(multipart("/admin/ocr/upload").file(fakeJpg).param("documentType", "ADMISSION_FORM")).andReturn().getResponse().getStatus();
        System.out.println("TXT renamed JPG upload: HTTP " + statusFakeJpg);
    }
}
