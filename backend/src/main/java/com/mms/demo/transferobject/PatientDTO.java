package com.mms.demo.transferobject;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

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
@Accessors(fluent = true)
public class PatientDTO {
    Long id;
    String name;
    String gender;
    Integer age;
    String email;
    String phone;
}
