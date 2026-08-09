package com.vertoedu.service;

import com.vertoedu.dto.AttendanceRecordDto;
import com.vertoedu.entity.AttendanceRecord;
import com.vertoedu.repository.AttendanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceQueryService {

    private final AttendanceRecordRepository attendanceRecordRepository;

    @Transactional(readOnly = true)
    public List<AttendanceRecordDto> getStudentAttendanceHistory(Long studentId) {
        List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdOrderByDateDesc(studentId);
        return records.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private AttendanceRecordDto mapToDto(AttendanceRecord record) {
        return AttendanceRecordDto.builder()
                .id(record.getId())
                .studentId(record.getStudent().getId())
                .studentName(record.getStudent().getFirstName() + " " + record.getStudent().getLastName())
                .scholarNumber(record.getStudent().getScholarNumber())
                .date(record.getDate())
                .status(record.getStatus())
                .remarks(record.getRemarks())
                .recordedById(record.getRecordedBy().getId())
                .recordedByName(record.getRecordedBy().getFirstName() + " " + record.getRecordedBy().getLastName())
                .build();
    }
}
