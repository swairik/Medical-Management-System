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

    @NotNull(message = "Slot Id cannot be null")
    private Long slotId;

    @NotNull(message = "Year & week cannot be null")
    private String weekDate;
}
