package com.vertoedu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FinalTestDataValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "clashclasher1102@gmail.com", roles = "TEACHER")
    public void testTeacher1SearchScholarNumber_AllowedFor10A() throws Exception {
        System.out.println("EVIDENCE: Teacher 1 (10-A, 9-B) -> Search TEST-SCH-10001 (10-A)");
        mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "clashclasher1102@gmail.com", roles = "TEACHER")
    public void testTeacher1SearchScholarNumber_DeniedFor10B() throws Exception {
        System.out.println("EVIDENCE: Teacher 1 (10-A, 9-B) -> Search TEST-SCH-10004 (10-B)");
        mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10004"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "parthdeshmukh167@gmail.com", roles = "TEACHER")
    public void testTeacher2SearchScholarNumber_AllowedFor10B() throws Exception {
        System.out.println("EVIDENCE: Teacher 2 (10-B, 9-A) -> Search TEST-SCH-10004 (10-B)");
        mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10004"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "parthdeshmukh167@gmail.com", roles = "TEACHER")
    public void testTeacher2SearchScholarNumber_DeniedFor10A() throws Exception {
        System.out.println("EVIDENCE: Teacher 2 (10-B, 9-A) -> Search TEST-SCH-10001 (10-A)");
        mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10001"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "workgpt678@gmail.com", roles = "PARENT")
    public void testParent1SearchScholarNumber_AllowedForOwnChild() throws Exception {
        System.out.println("EVIDENCE: Parent 1 (P1) -> Search TEST-SCH-10001");
        mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "workgpt678@gmail.com", roles = "PARENT")
    public void testParent1SearchScholarNumber_DeniedForOtherChild() throws Exception {
        System.out.println("EVIDENCE: Parent 1 (P1) -> Search TEST-SCH-10004");
        mockMvc.perform(get("/search/students").param("scholarNumber", "TEST-SCH-10004"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "clashclasher1124@gmail.com", roles = "ADMIN")
    public void testAdminListing_Allowed() throws Exception {
        System.out.println("EVIDENCE: Admin -> /api/admin/students/school/1");
        mockMvc.perform(get("/admin/students/school/1"))
                .andExpect(status().isOk());
        System.out.println("EVIDENCE: Admin -> /api/admin/teachers/school/1");
        mockMvc.perform(get("/admin/teachers/school/1"))
                .andExpect(status().isOk());
        System.out.println("EVIDENCE: Admin -> /api/admin/parents/school/1");
        mockMvc.perform(get("/admin/parents/school/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "clashclasher1124@gmail.com", roles = "ADMIN")
    public void testDuplicateScholarNumber_Rejected() throws Exception {
        System.out.println("EVIDENCE: Admin -> Create Duplicate Scholar Number TEST-SCH-10001");
        
        String payload = "{" +
                "\"schoolId\": 1," +
                "\"firstName\": \"Duplicate\"," +
                "\"lastName\": \"Student\"," +
                "\"scholarNumber\": \"TEST-SCH-10001\"," +
                "\"dob\": \"2010-01-01\"" +
                "}";

        mockMvc.perform(post("/admin/students")
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isConflict()); // Expected to be mapped to 409 Conflict by GlobalExceptionHandler
    }
}
