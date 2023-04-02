package com.mms.demo.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequest {
    @NotNull(message = "Doctor Id cannot be null")
    private Long doctorId;

    @NotNull(message = "Year & week cannot be null")
    private String startTime;

    @Builder.Default
    private String endTime = "";

    @Builder.Default
    private Boolean approval = false;

}
