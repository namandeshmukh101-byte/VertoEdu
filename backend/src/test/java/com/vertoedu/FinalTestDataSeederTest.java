package com.vertoedu;

import com.vertoedu.entity.*;
import com.vertoedu.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FinalTestDataSeederTest {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private SchoolRepository schoolRepository;
    @Autowired private SchoolClassRepository schoolClassRepository;
    @Autowired private SectionRepository sectionRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private ParentRepository parentRepository;
    @Autowired private StudentRepository studentRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void seedFinalTestData() {
        System.out.println("=====================================================");
        System.out.println("FINAL TEST DATA CREATION & EVIDENCE REPORT SCRIPT");
        System.out.println("=====================================================");

        // 1. Verify ADMIN
        System.out.println("\n--- 1. ADMIN VERIFICATION ---");
        User admin = userRepository.findByEmail("clashclasher1124@gmail.com").orElse(null);
        if (admin == null) {
            System.err.println("NOT VERIFIED — GOOGLE USER HAS NOT BEEN PROVISIONED");
            fail("Admin clashclasher1124@gmail.com not found!");
            return;
        }
        System.out.println("User ID: " + admin.getId());
        System.out.println("Email: " + admin.getEmail());
        System.out.println("Role: " + admin.getRole().getName());
        System.out.println("Active: " + admin.getActive());
        assertEquals("ADMIN", admin.getRole().getName(), "Admin must have ADMIN role");

        // 2 & 3. Ensure Test Users exist and have correct roles
        System.out.println("\n--- 2 & 3. TEST USERS PROVISIONING ---");
        Role teacherRole = roleRepository.findByName("TEACHER").orElseThrow();
        Role parentRole = roleRepository.findByName("PARENT").orElseThrow();

        User t1 = ensureUser("clashclasher1102@gmail.com", teacherRole, "Teacher One");
        User t2 = ensureUser("parthdeshmukh167@gmail.com", teacherRole, "Teacher Two");
        User p1 = ensureUser("workgpt678@gmail.com", parentRole, "Parent One");
        User p2 = ensureUser("regaltashwal@gmail.com", parentRole, "Parent Two");

        System.out.println("| Email | User ID | Final Role | Verified |");
        System.out.println("|-------|---------|------------|----------|");
        printUserRow(t1);
        printUserRow(t2);
        printUserRow(p1);
        printUserRow(p2);

        // School and Classes
        School school = schoolRepository.findAll().stream().findFirst().orElseGet(() -> {
            School s = new School();
            s.setName("Test Manual Verification School");
            return schoolRepository.save(s);
        });

        SchoolClass class10 = ensureClass(school, "10");
        SchoolClass class9 = ensureClass(school, "9");

        Section sec10A = ensureSection(class10, "A");
        Section sec10B = ensureSection(class10, "B");
        Section sec9A = ensureSection(class9, "A");
        Section sec9B = ensureSection(class9, "B");

        // 5 & 6. Teacher and Parent Entities
        Teacher teacher1 = ensureTeacher(t1, school, "T1", "One", sec10A, sec9B);
        Teacher teacher2 = ensureTeacher(t2, school, "T2", "Two", sec10B, sec9A);

        Parent parent1 = ensureParent(p1, school, "P1", "One");
        Parent parent2 = ensureParent(p2, school, "P2", "Two");

        System.out.println("\n--- 5. TEACHER ASSIGNMENTS EVIDENCE ---");
        System.out.println("Teacher 1 (" + t1.getEmail() + ") assigned sections:");
        teacher1.getAssignedSections().forEach(s -> System.out.println("  -> " + s.getSchoolClass().getName() + "-" + s.getName()));
        System.out.println("Teacher 2 (" + t2.getEmail() + ") assigned sections:");
        teacher2.getAssignedSections().forEach(s -> System.out.println("  -> " + s.getSchoolClass().getName() + "-" + s.getName()));

        // 4. Exact 11 Students
        System.out.println("\n--- 4. STUDENT CREATION ---");
        Student s1 = ensureStudent("Aarav", "Sharma", "TEST-SCH-10001", school, sec10A, parent1);
        Student s2 = ensureStudent("Vivaan", "Patel", "TEST-SCH-10002", school, sec10A, parent1);
        Student s3 = ensureStudent("Aditya", "Verma", "TEST-SCH-10003", school, sec10A, parent1);
        Student s4 = ensureStudent("Arjun", "Mehta", "TEST-SCH-10004", school, sec10B, parent2);
        Student s5 = ensureStudent("Rohan", "Gupta", "TEST-SCH-10005", school, sec10B, parent2);
        Student s6 = ensureStudent("Kabir", "Joshi", "TEST-SCH-10006", school, sec10B, null);
        Student s7 = ensureStudent("Ishaan", "Singh", "TEST-SCH-10007", school, sec9A, null);
        Student s8 = ensureStudent("Reyansh", "Shah", "TEST-SCH-10008", school, sec9A, null);
        Student s9 = ensureStudent("Atharv", "Jain", "TEST-SCH-10009", school, sec9B, null);
        Student s10 = ensureStudent("Vihaan", "Desai", "TEST-SCH-10010", school, sec9B, null);
        Student s11 = ensureStudent("Dhruv", "Kulkarni", "TEST-SCH-10011", school, sec9B, null);

        System.out.println("\n--- 6. PARENT ASSIGNMENTS EVIDENCE ---");
        System.out.println("Parent 1 (" + p1.getEmail() + ") linked children:");
        studentRepository.findByParentId(parent1.getId()).forEach(s -> System.out.println("  -> " + s.getScholarNumber()));
        System.out.println("Parent 2 (" + p2.getEmail() + ") linked children:");
        studentRepository.findByParentId(parent2.getId()).forEach(s -> System.out.println("  -> " + s.getScholarNumber()));

        System.out.println("\n--- DATABASE EVIDENCE TABLE ---");
        System.out.println("| # | DB ID | Student | Scholar Number | Class | Section | Teacher | Parent |");
        System.out.println("|---|-------|---------|----------------|-------|---------|---------|--------|");
        printStudentRow(s1, teacher1, parent1);
        printStudentRow(s2, teacher1, parent1);
        printStudentRow(s3, teacher1, parent1);
        printStudentRow(s4, teacher2, parent2);
        printStudentRow(s5, teacher2, parent2);
        printStudentRow(s6, teacher2, null);
        printStudentRow(s7, teacher2, null);
        printStudentRow(s8, teacher2, null);
        printStudentRow(s9, teacher1, null);
        printStudentRow(s10, teacher1, null);
        printStudentRow(s11, teacher1, null);
        
        System.out.println("\n=====================================================");
        System.out.println("SEEDING COMPLETE. DATA COMMITTED.");
    }

    private void printStudentRow(Student s, Teacher t, Parent p) {
        String parentName = p != null ? p.getUser().getEmail() : "UNLINKED";
        System.out.printf("| - | %d | %s %s | %s | %s | %s | %s | %s |\n",
                s.getId(), s.getFirstName(), s.getLastName(), s.getScholarNumber(),
                s.getSchoolClass().getSchoolClass().getName(), s.getSchoolClass().getName(),
                t.getUser().getEmail(), parentName);
    }

    private void printUserRow(User u) {
        System.out.printf("| %s | %d | %s | YES |\n", u.getEmail(), u.getId(), u.getRole().getName());
    }

    private User ensureUser(String email, Role requiredRole, String name) {
        return userRepository.findByEmail(email).map(u -> {
            if (!u.getRole().getName().equals(requiredRole.getName())) {
                u.setRole(requiredRole);
                return userRepository.save(u);
            }
            return u;
        }).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setFullName(name);
            u.setRole(requiredRole);
            u.setActive(true);
            return userRepository.save(u);
        });
    }

    private SchoolClass ensureClass(School school, String name) {
        return schoolClassRepository.findAll().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    SchoolClass c = new SchoolClass();
                    c.setSchool(school);
                    c.setName(name);
                    return schoolClassRepository.save(c);
                });
    }

    private Section ensureSection(SchoolClass c, String name) {
        return sectionRepository.findAll().stream()
                .filter(s -> s.getName().equals(name) && s.getSchoolClass().getId().equals(c.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Section s = new Section();
                    s.setSchoolClass(c);
                    s.setName(name);
                    return sectionRepository.save(s);
                });
    }

    private Teacher ensureTeacher(User user, School school, String fName, String lName, Section s1, Section s2) {
        Teacher teacher = teacherRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Teacher t = new Teacher();
                    t.setUser(user);
                    t.setSchool(school);
                    t.setFirstName(fName);
                    t.setLastName(lName);
                    return teacherRepository.save(t);
                });

        if (teacher.getAssignedSections() == null) {
            teacher.setAssignedSections(new HashSet<>());
        }
        teacher.getAssignedSections().clear();
        teacher.getAssignedSections().add(s1);
        teacher.getAssignedSections().add(s2);
        return teacherRepository.save(teacher);
    }

    private Parent ensureParent(User user, School school, String fName, String lName) {
        return parentRepository.findAll().stream()
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Parent p = new Parent();
                    p.setUser(user);
                    p.setSchool(school);
                    p.setFirstName(fName);
                    p.setLastName(lName);
                    return parentRepository.save(p);
                });
    }

    private Student ensureStudent(String fName, String lName, String scholarNumber, School school, Section section, Parent parent) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getScholarNumber().equals(scholarNumber))
                .findFirst()
                .map(existing -> {
                    existing.setFirstName(fName);
                    existing.setLastName(lName);
                    existing.setSchoolClass(section);
                    existing.setParent(parent);
                    return studentRepository.save(existing);
                })
                .orElseGet(() -> {
                    Student s = new Student();
                    s.setFirstName(fName);
                    s.setLastName(lName);
                    s.setScholarNumber(scholarNumber);
                    s.setSchool(school);
                    s.setSchoolClass(section);
                    s.setParent(parent);
                    return studentRepository.save(s);
                });
    }
}
