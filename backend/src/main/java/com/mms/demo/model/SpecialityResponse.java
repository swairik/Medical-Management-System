package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mms.demo.entity.Speciality;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityResponse {
    private Long id;
    private String name;

    public static SpecialityResponse createResponseFromSpeciality(Speciality speciality) {
        SpecialityResponse specialityResponse = SpecialityResponse.builder()
                .id(speciality.getId())
                .name(speciality.getName())
                .build();
        return specialityResponse;
    }
}
