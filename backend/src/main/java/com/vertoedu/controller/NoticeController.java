package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.NoticeDto;
import com.vertoedu.entity.Notice;
import com.vertoedu.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notices")
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT')")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NoticeDto>>> getRecentNotices() {
        // Hardcoding schoolId = 1 for Prototype
        List<Notice> notices = noticeRepository.findTop10BySchoolIdOrderByCreatedAtDesc(1L);
        List<NoticeDto> dtos = notices.stream().map(n -> {
            NoticeDto dto = new NoticeDto();
            dto.setId(n.getId());
            dto.setTitle(n.getTitle());
            dto.setContent(n.getContent());
            dto.setCreatedAt(n.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Notices retrieved", dtos));
    }
}
