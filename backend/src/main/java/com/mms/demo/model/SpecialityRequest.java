package com.mms.demo.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mms.demo.entity.Speciality;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityRequest {
    @NotNull(message = "Patient Id cannot be null")
    private String name;

    public static Speciality createSpecialityFromRequest(SpecialityRequest specialityRequest) {
        Speciality speciality = Speciality.builder().name(specialityRequest.getName()).build();
        return speciality;
    }
}
