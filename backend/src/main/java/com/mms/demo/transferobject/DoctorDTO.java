package com.mms.demo.transferobject;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@Builder
public class DoctorDTO {
    private final @NonNull Long id;
    private final @NonNull String name;
    private final String gender;
    private final Integer age;
    private final @NonNull String email;
    private final @NonNull String phone;
    private final @NonNull SpecialityDTO speciality;
}
