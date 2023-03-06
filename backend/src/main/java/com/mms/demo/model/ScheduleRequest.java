package com.mms.demo.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(1)
    @Max(52)
    @NotNull(message = "Week cannot be null")
    private Integer week;
}
