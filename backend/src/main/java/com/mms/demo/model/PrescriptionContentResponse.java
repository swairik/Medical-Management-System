package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionContentResponse {
    String medication;
    String test;
    String diagnosis;

    public static PrescriptionContentResponse createResponseFromPrescriptionContent(String[] contents) {
        PrescriptionContentResponse response = PrescriptionContentResponse.builder()
                .medication(contents[0])
                .diagnosis(contents[1])
                .test(contents[2])
                .build();
        return response;
    }
}
