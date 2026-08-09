package com.vertoedu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"app.admin-emails=admin@vertoedu.com"})
public class OcrIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin@vertoedu.com", roles = "ADMIN")
    public void testOcrIntegrationPdf() throws Exception {
        runTestForFile("test_document.pdf", "application/pdf", "PDF");
    }

    @Test
    @WithMockUser(username = "admin@vertoedu.com", roles = "ADMIN")
    public void testOcrIntegrationJpg() throws Exception {
        runTestForFile("test_image.jpg", "image/jpeg", "JPEG");
    }

    @Test
    @WithMockUser(username = "admin@vertoedu.com", roles = "ADMIN")
    public void testOcrIntegrationPng() throws Exception {
        runTestForFile("test_image.png", "image/png", "PNG");
    }

    private void runTestForFile(String filename, String contentType, String docType) throws Exception {
        Path filePath = Path.of("../ocr-service/" + filename);
        byte[] fileBytes = Files.readAllBytes(filePath);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                filename,
                contentType,
                fileBytes
        );

        System.out.println("===============================");
        System.out.println("TESTING: " + filename);
        
        // 1. Upload Document
        MvcResult uploadResult = mockMvc.perform(multipart("/ocr/upload")
                .file(file)
                .param("type", "ADMISSION_FORM"))
                .andExpect(status().isOk())
                .andReturn();
        
        String uploadResponse = uploadResult.getResponse().getContentAsString();
        System.out.println("HTTP STATUS: 200 OK");
        System.out.println("UPLOAD RESPONSE: " + uploadResponse);
        
        // Extract ID
        String idStr = uploadResponse.split("\"id\":")[1].split(",")[0];
        Long id = Long.parseLong(idStr);

        // 2. Process OCR (and AI)
        try {
            MvcResult processResult = mockMvc.perform(post("/ocr/process/" + id))
                    .andReturn();
            System.out.println("PROCESS OCR STATUS: " + processResult.getResponse().getStatus());
            System.out.println("PROCESS OCR RESPONSE: " + processResult.getResponse().getContentAsString());
        } catch (Exception e) {
            System.out.println("PROCESS EXCEPTION (Expected AI 429 handled/unhandled): " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("===============================");
    }
}
