package com.mms.demo.model;

import java.time.LocalDateTime;

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
    private PatientResponse patientResponse;
    private DoctorResponse doctorResponse;
    private LocalDateTime stamp;
    private byte[] reportText;
}
