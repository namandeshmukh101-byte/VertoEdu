package com.vertoedu;

import com.vertoedu.entity.*;
import com.vertoedu.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IdorSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ParentRepository parentRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    private User parentUser1;
    private User parentUser2;
    private Parent parent1;
    private Parent parent2;
    private Student student1;
    private School testSchool;

    @BeforeEach
    public void setup() {
        testSchool = schoolRepository.findByName("Security Test School")
                .orElseGet(() -> {
                    School s = new School();
                    s.setName("Security Test School");
                    return schoolRepository.save(s);
                });

        Role parentRole = roleRepository.findByName("PARENT")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("PARENT");
                    return roleRepository.save(r);
                });

        parentUser1 = userRepository.findByEmail("parent1@test.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail("parent1@test.com");
                    u.setFullName("Parent One");
                    u.setRole(parentRole);
                    return userRepository.save(u);
                });

        parent1 = new Parent();
        parent1.setUser(parentUser1);
        parent1.setFirstName("Parent");
        parent1.setLastName("One");
        parent1.setSchool(testSchool);
        parent1 = parentRepository.save(parent1);

        parentUser2 = userRepository.findByEmail("parent2@test.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail("parent2@test.com");
                    u.setFullName("Parent Two");
                    u.setRole(parentRole);
                    return userRepository.save(u);
                });

        parent2 = new Parent();
        parent2.setUser(parentUser2);
        parent2.setFirstName("Parent");
        parent2.setLastName("Two");
        parent2.setSchool(testSchool);
        parent2 = parentRepository.save(parent2);

        student1 = new Student();
        student1.setFirstName("Student");
        student1.setLastName("One");
        student1.setScholarNumber("SCH-SEC-001");
        student1.setParent(parent1); // Belongs to Parent 1
        student1.setSchool(testSchool);
        student1 = studentRepository.saveAndFlush(student1);
    }

    @Test
    @WithMockUser(username = "teacher@test.com", roles = "TEACHER")
    public void testTeacherAccessingAdminApi_Denied() throws Exception {
        mockMvc.perform(get("/admin/schools"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "parent1@test.com", roles = "PARENT")
    public void testParentAccessingAdminApi_Denied() throws Exception {
        mockMvc.perform(get("/admin/schools"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "parent2@test.com", roles = "PARENT")
    public void testParentAccessingUnrelatedStudent_Denied() throws Exception {
        // Parent 2 trying to access Student 1's attendance (Student 1 belongs to Parent 1)
        mockMvc.perform(get("/parent/students/" + student1.getId() + "/attendance"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    
    @Test
    public void testUnauthenticatedAccess_Denied() throws Exception {
        mockMvc.perform(get("/admin/schools"))
                .andExpect(status().isUnauthorized());
    }
}
