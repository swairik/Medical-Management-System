package com.mms.demo.model;

import java.time.LocalDateTime;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private Long id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime stamp;
    private byte[] reportText;
}
