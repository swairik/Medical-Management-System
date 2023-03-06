package com.mms.demo.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotRequest {

    @NotNull(message = "Name cannot be null")
    private DayOfWeek weekday;

    @NotNull(message = "Start Time cannot be null")
    private LocalTime start;

    @NotNull(message = "End Time cannot be null")
    private LocalTime end;

    @NotNull(message = "Capacity cannot be null")
    @Min(1)
    private Integer capacity;

}
