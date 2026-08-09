package com.vertoedu;

import com.vertoedu.entity.Role;
import com.vertoedu.entity.User;
import com.vertoedu.entity.enums.DocumentType;
import com.vertoedu.repository.RoleRepository;
import com.vertoedu.repository.UserRepository;
import com.vertoedu.service.DocumentStorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FileSecurityClosureTest {

    @Autowired
    private DocumentStorageService documentStorageService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    private String testUserEmail;
    
    @BeforeEach
    public void setup() {
        Role parentRole = roleRepository.findByName("PARENT").orElseThrow();
        User user = new User();
        testUserEmail = "filetest" + UUID.randomUUID() + "@test.com";
        user.setEmail(testUserEmail);
        user.setFullName("File Test");
        user.setRole(parentRole);
        userRepository.saveAndFlush(user);
    }
    
    @AfterEach
    public void cleanup() {
        userRepository.findByEmail(testUserEmail).ifPresent(user -> userRepository.delete(user));
    }
    
    @Test
    public void testEmptyFile_Rejected() {
        System.out.println("EVIDENCE: Testing empty file upload");
        MultipartFile file = new MockMultipartFile("file", "empty.pdf", "application/pdf", new byte[0]);
        
        assertThrows(IllegalArgumentException.class, () -> {
            documentStorageService.uploadDocument(file, DocumentType.ADMISSION_FORM, testUserEmail);
        });
    }

    @Test
    public void testInvalidExtension_Rejected() {
        System.out.println("EVIDENCE: Testing TXT file upload");
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello".getBytes());
        
        assertThrows(IllegalArgumentException.class, () -> {
            documentStorageService.uploadDocument(file, DocumentType.ADMISSION_FORM, testUserEmail);
        });
    }

    @Test
    public void testExeDisguisedAsJpg_Rejected() {
        System.out.println("EVIDENCE: Testing EXE disguised as JPG");
        // MZ header indicating Windows Executable
        byte[] exeBytes = new byte[] {0x4D, 0x5A, 0x00, 0x00, 0x00};
        MultipartFile file = new MockMultipartFile("file", "virus.jpg", "image/jpeg", exeBytes);
        
        assertThrows(IllegalArgumentException.class, () -> {
            documentStorageService.uploadDocument(file, DocumentType.ADMISSION_FORM, testUserEmail);
        });
    }
}
