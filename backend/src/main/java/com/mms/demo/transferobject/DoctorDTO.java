package com.mms.demo.transferobject;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class DoctorDTO {
    Long id;
    String name;
    String gender;
    Integer age;
    String email;
    String phone;
    SpecialityDTO speciality;
}
