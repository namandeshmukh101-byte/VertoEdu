package com.vertoedu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiRegressionClosureTest {

    @Autowired
    private MockMvc mockMvc;

    // --- ADMIN API TESTS ---

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminAccessToAdminApi_Allowed() throws Exception {
        System.out.println("EVIDENCE: Testing Admin -> /api/admin/students/school/1");
        mockMvc.perform(get("/admin/students/school/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    public void testTeacherAccessToAdminApi_Denied() throws Exception {
        System.out.println("EVIDENCE: Testing Teacher -> /api/admin/students/school/1");
        mockMvc.perform(get("/admin/students/school/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PARENT")
    public void testParentAccessToAdminApi_Denied() throws Exception {
        System.out.println("EVIDENCE: Testing Parent -> /api/admin/students/school/1");
        mockMvc.perform(get("/admin/students/school/1"))
                .andExpect(status().isForbidden());
    }

    // --- TEACHER API TESTS ---

    @Test
    @WithMockUser(username = "teacher_user", roles = "TEACHER")
    public void testTeacherAccessToTeacherApi_Allowed() throws Exception {
        System.out.println("EVIDENCE: Testing Teacher -> /teacher/summary");
        // Needs a user in DB, but even 404 is allowed (since it passed the 403 authorization filter)
        mockMvc.perform(get("/teacher/summary"))
                .andExpect(status().isNotFound()); // Or 200 if seeded
    }

    @Test
    @WithMockUser(roles = "PARENT")
    public void testParentAccessToTeacherApi_Denied() throws Exception {
        System.out.println("EVIDENCE: Testing Parent -> /teacher/summary");
        mockMvc.perform(get("/teacher/summary"))
                .andExpect(status().isForbidden());
    }

    // --- PARENT API TESTS ---

    @Test
    @WithMockUser(username = "parent_user", roles = "PARENT")
    public void testParentAccessToParentApi_Allowed() throws Exception {
        System.out.println("EVIDENCE: Testing Parent -> /api/parent/me/students");
        // We test /api/parent/me/students which might return 404 if user not seeded, but not 403
        mockMvc.perform(get("/parent/me/students"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    public void testTeacherAccessToParentApi_Denied() throws Exception {
        System.out.println("EVIDENCE: Testing Teacher -> /api/parent/me/students");
        mockMvc.perform(get("/parent/me/students"))
                .andExpect(status().isForbidden());
    }

    // --- UNAUTHENTICATED TESTS ---
    
    @Test
    public void testUnauthenticatedAccess_Denied() throws Exception {
        System.out.println("EVIDENCE: Testing Unauthenticated -> /api/admin/students/school/1");
        mockMvc.perform(get("/admin/students/school/1"))
                .andExpect(status().isUnauthorized());
    }
}
