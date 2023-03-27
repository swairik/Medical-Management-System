package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;
    private PatientResponse patientResponse;
    private ScheduleResponse scheduleResponse;
    // private PrescriptionContentResponse prescriptionContentResponse;
    // private FeedbackResponse feedbackResponse;
    private Boolean attended;

}
