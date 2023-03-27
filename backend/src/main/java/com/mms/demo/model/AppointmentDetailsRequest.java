package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDetailsRequest {
    private Long patientId;
    private Long doctorId;
    @Builder.Default
    private PrescriptionContentRequest prescriptionContentRequest = null;
    @Builder.Default
    private String feedbackRequest = null;
    private String appointmentDateTime;
}
