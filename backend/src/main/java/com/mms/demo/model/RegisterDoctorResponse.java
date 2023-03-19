package com.mms.demo.model;

import com.mms.demo.entity.Doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDoctorResponse {
    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private String password;
    private SpecialityResponse speciality;

    public static RegisterDoctorResponse createResponseFromDoctor(Doctor doctor) {
        SpecialityResponse specialityResponse = SpecialityResponse.createResponseFromSpeciality(doctor.getSpeciality());
        RegisterDoctorResponse doctorResponse = RegisterDoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .age(doctor.getAge())
                .email(doctor.getEmail())
                .gender(doctor.getGender())
                .phone(doctor.getPhone())
                .speciality(specialityResponse)
                .build();
        return doctorResponse;
    }
}
