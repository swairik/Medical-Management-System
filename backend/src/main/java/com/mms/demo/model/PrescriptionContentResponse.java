// package com.mms.demo.model;

// import java.util.Base64;

// import com.mms.demo.entity.Prescription;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class PrescriptionContentResponse {
//     Long id;
//     String medication;
//     String test;
//     String diagnosis;

//     public static PrescriptionContentResponse createResponseFromPrescriptionContent(
//                     Prescription prescription) {
//         String delimiter = ":,-";
//         String[] contents =
//                         new String(Base64.getDecoder().decode(prescription.getContents()))
//                                         .split(delimiter);
//         PrescriptionContentResponse response = PrescriptionContentResponse.builder()
//         .id(prescription.getId())
//         .build();
//         if(contents.length >= 1) 
//                         response.setMedication(contents[0]);
//         if(contents.length >= 2) 
//                         response.setDiagnosis(contents[1]);
//         if(contents.length >= 3) 
//                         response.setTest(contents[2]);
//         return response;
//     }
// }
