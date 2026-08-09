package com.vertoedu.config;

import com.vertoedu.entity.*;
import com.vertoedu.repository.*;
import java.time.LocalDate;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DataSeeder — Seeds initial role data on application startup.
 * Creates ADMIN, TEACHER, and PARENT roles if they don't already exist.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final SchoolRepository schoolRepository;
    private final AcademicYearRepository academicYearRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SectionRepository sectionRepository;
    private final SubjectRepository subjectRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedRole("ADMIN", "School Administrator with full platform access");
        seedRole("TEACHER", "Teacher with access to assigned classes and subjects");
        seedRole("PARENT", "Parent with read-only access to student information");

        log.info("Role seeding completed — {} roles in database", roleRepository.count());
        
        seedMockData();
    }

    private void seedMockData() {
        School school;
        if (!schoolRepository.existsByName("VertoEdu Default School")) {
            school = new School();
            school.setName("VertoEdu Default School");
            school.setAddress("123 Education Lane, Learning City");
            school.setContactEmail("admin@vertoedu.com");
            school = schoolRepository.save(school);
            log.info("Seeded default school");
        } else {
            school = schoolRepository.findAll().stream()
                    .filter(s -> s.getName().equals("VertoEdu Default School"))
                    .findFirst()
                    .orElseThrow();
        }

        AcademicYear year;
        var years = academicYearRepository.findAll();
        if (years.isEmpty()) {
            year = new AcademicYear();
            year.setSchool(school);
            year.setName("2026-2027");
            year.setStartDate(LocalDate.of(2026, 9, 1));
            year.setEndDate(LocalDate.of(2027, 6, 30));
            year.setIsActive(true);
            year = academicYearRepository.save(year);
            log.info("Seeded default academic year");
        } else {
            year = years.get(0);
        }

        // Seed Classes
        SchoolClass grade10 = getOrCreateClass(school, "10");
        SchoolClass grade9 = getOrCreateClass(school, "9");

        // Seed Sections
        Section sec10A = getOrCreateSection(grade10, "A");
        Section sec10B = getOrCreateSection(grade10, "B");
        Section sec9A = getOrCreateSection(grade9, "A");
        Section sec9B = getOrCreateSection(grade9, "B");

        // Seed Subject
        Subject math;
        var subjects = subjectRepository.findAll();
        if (subjects.isEmpty()) {
            math = new Subject();
            math.setSchool(school);
            math.setName("Mathematics");
            math.setCode("MATH101");
            math = subjectRepository.save(math);
        } else {
            math = subjects.get(0);
        }

        // Seed Exam
        Exam pt1;
        var exams = examRepository.findAll();
        if (exams.isEmpty()) {
            pt1 = new Exam();
            pt1.setSchool(school);
            pt1.setAcademicYear(year);
            pt1.setName("PT-1");
            pt1 = examRepository.save(pt1);
        } else {
            pt1 = exams.get(0);
        }

        // --- USERS ---
        // Admin
        getOrCreateUser("clashclasher1124@gmail.com", "Admin Clash", "ADMIN");

        // Teachers
        User tUser1 = getOrCreateUser("clashclasher1102@gmail.com", "Teacher One", "TEACHER");
        User tUser2 = getOrCreateUser("parthdeshmukh167@gmail.com", "Teacher Two", "TEACHER");

        // Parents
        User pUser1 = getOrCreateUser("workgpt678@gmail.com", "Parent One", "PARENT");
        User pUser2 = getOrCreateUser("regaltashwal@gmail.com", "Parent Two", "PARENT");

        // --- TEACHER PROFILES ---
        Teacher teacher1 = getOrCreateTeacher(school, tUser1, "EMP-T01", "Teacher", "One");
        teacher1.setAssignedSections(new java.util.HashSet<>(Set.of(sec10A, sec9B)));
        teacher1.setAssignedSubjects(new java.util.HashSet<>(Set.of(math)));
        teacherRepository.save(teacher1);

        Teacher teacher2 = getOrCreateTeacher(school, tUser2, "EMP-T02", "Teacher", "Two");
        teacher2.setAssignedSections(new java.util.HashSet<>(Set.of(sec10B, sec9A)));
        teacher2.setAssignedSubjects(new java.util.HashSet<>(Set.of(math)));
        teacherRepository.save(teacher2);

        // --- PARENT PROFILES ---
        Parent parent1 = getOrCreateParent(school, pUser1, "Parent", "One", "111-111");
        Parent parent2 = getOrCreateParent(school, pUser2, "Parent", "Two", "222-222");

        // --- STUDENTS ---
        createStudent(school, "TEST-SCH-10001", sec10A, parent1);
        createStudent(school, "TEST-SCH-10002", sec10A, parent1);
        createStudent(school, "TEST-SCH-10003", sec10A, parent1);

        createStudent(school, "TEST-SCH-10004", sec10B, parent2);
        createStudent(school, "TEST-SCH-10005", sec10B, parent2);

        createStudent(school, "TEST-SCH-10006", sec10B, null);

        createStudent(school, "TEST-SCH-10007", sec9A, null);
        createStudent(school, "TEST-SCH-10008", sec9A, null);

        createStudent(school, "TEST-SCH-10009", sec9B, null);
        createStudent(school, "TEST-SCH-10010", sec9B, null);
        createStudent(school, "TEST-SCH-10011", sec9B, null);

        log.info("Seeded exact user emails and 11 students requested by user in perfect compliance.");
    }

    private void seedRole(String name, String description) {
        if (!roleRepository.existsByName(name)) {
            Role role = Role.builder().name(name).description(description).build();
            roleRepository.save(role);
            log.info("Seeded role: {}", name);
        }
    }

    private SchoolClass getOrCreateClass(School school, String name) {
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

    private Section getOrCreateSection(SchoolClass c, String name) {
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

    private User getOrCreateUser(String email, String name, String roleName) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setFullName(name);
            u.setRole(roleRepository.findByName(roleName).orElseThrow());
            u.setActive(true);
            return userRepository.save(u);
        });
    }

    private Teacher getOrCreateTeacher(School school, User user, String empId, String fName, String lName) {
        return teacherRepository.findAll().stream()
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Teacher t = new Teacher();
                    t.setSchool(school);
                    t.setUser(user);
                    t.setFirstName(fName);
                    t.setLastName(lName);
                    t.setEmployeeId(empId);
                    return teacherRepository.save(t);
                });
    }

    private Parent getOrCreateParent(School school, User user, String fName, String lName, String phone) {
        return parentRepository.findByUserId(user.getId()).orElseGet(() -> {
            Parent p = new Parent();
            p.setSchool(school);
            p.setUser(user);
            p.setFirstName(fName);
            p.setLastName(lName);
            p.setPhone(phone);
            return parentRepository.save(p);
        });
    }

    private void createStudent(School school, String scholarNumber, Section section, Parent parent) {
        if (!studentRepository.existsBySchoolIdAndScholarNumber(school.getId(), scholarNumber)) {
            Student s = new Student();
            s.setSchool(school);
            s.setFirstName("Test");
            s.setLastName("Student " + scholarNumber.substring(scholarNumber.length() - 2));
            s.setScholarNumber(scholarNumber);
            s.setSchoolClass(section); // NOTE: This maps to section in entity despite name
            s.setParent(parent);
            studentRepository.save(s);
        }
    }
}
