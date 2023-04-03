// package com.mms.demo.model;

// import java.time.LocalDateTime;
// import java.util.Base64;

// import com.mms.demo.entity.AppointmentDetails;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class AppointmentDetailsResponse {
//     private Long id;
//     private PatientResponse patientResponse;
//     private DoctorResponse doctorResponse;
//     private PrescriptionContentResponse prescriptionContentResponse;
//     private String feedbackResponse;
//     private LocalDateTime appointmentTimeStamp;

//     public static AppointmentDetailsResponse createResponseFromAppointmentDetails(
//             AppointmentDetails appointmentDetails) {
//         AppointmentDetailsResponse appointmentDetailsResponse = AppointmentDetailsResponse.builder()
//                 .id(appointmentDetails.getId())
//                 .patientResponse(PatientResponse.createResponseFromPatient(appointmentDetails.getPatient()))
//                 .doctorResponse(DoctorResponse.createResponseFromDoctor(appointmentDetails.getDoctor()))
//                 .prescriptionContentResponse(PrescriptionContentResponse.createResponseFromPrescriptionContent(appointmentDetails.getPrescription()))
//                 .feedbackResponse(new String(Base64.getDecoder().decode((appointmentDetails.getFeedback().getContents()))))
//                 .appointmentTimeStamp(appointmentDetails.getStamp())
//                 .build();
//         return appointmentDetailsResponse;
//     }

// }
