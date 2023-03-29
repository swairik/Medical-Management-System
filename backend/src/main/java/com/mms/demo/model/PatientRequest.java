package com.mms.demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class PatientRequest {

    @NotNull(message = "Name cannot be null")
    private String name;

    @Pattern(regexp = "M|F", message = "Gender must be M or F")
    private String gender;

    @NotNull(message = "Age cannot be null")
    @Min(0)
    private Integer age;

    @Size(min = 7, max = 14, message = "Phone number must be between 7-14 characters")
    @NotNull
    private String phone;

    @Email
    private String email;

    public static PatientDTO createPatientFromRequest(Long id, PatientRequest patientRequest) {
        PatientDTO patient = PatientDTO.builder().id(id).name(patientRequest.getName())
                .gender(patientRequest.getGender()).age(patientRequest.getAge())
                .phone(patientRequest.getPhone()).email(patientRequest.getEmail()).build();
        return patient;
    }

}
