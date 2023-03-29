package com.mms.demo.transferobject;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class PatientDTO {
    private final @NonNull Long id;
    private final @NonNull String name;
    private final String gender;
    private final Integer age;
    private final @NonNull String email;
    private final String phone;
}
