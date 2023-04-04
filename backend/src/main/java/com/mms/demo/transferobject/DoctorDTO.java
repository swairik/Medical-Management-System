package com.mms.demo.transferobject;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder(toBuilder = true)
public class DoctorDTO {
    Long id;
    String name;
    String gender;
    Integer age;
    String email;
    String phone;
    SpecialityDTO speciality;
    Long ratingCount;
    Double ratingAverage;
}
