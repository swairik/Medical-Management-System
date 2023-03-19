package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequest {
    private Long doctorId;
    private Long patientId;
    private Long appointmentId;
    private PrescriptionContentRequest contents;
}