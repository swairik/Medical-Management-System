package com.mms.demo.transferobject;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder(toBuilder = true)
public class PatientDTO {
    Long id;
    String name;
    String gender;
    Integer age;
    String email;
    String phone;
}
