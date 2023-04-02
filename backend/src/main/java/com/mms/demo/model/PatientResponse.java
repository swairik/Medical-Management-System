package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mms.demo.entity.Patient;
import com.mms.demo.transferobject.PatientDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {
    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;

    public static PatientResponse createResponseFromPatient(PatientDTO patient) {
        PatientResponse patientResponse = PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .email(patient.getEmail())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .build();
        return patientResponse;
    }

}
