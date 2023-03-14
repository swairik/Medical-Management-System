package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mms.demo.entity.Doctor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {
    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private SpecialityResponse speciality;

    public static DoctorResponse createResponseFromDoctor(Doctor doctor) {
        SpecialityResponse specialityResponse = SpecialityResponse.createResponseFromSpeciality(doctor.getSpeciality());
        DoctorResponse doctorResponse = DoctorResponse.builder()
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
