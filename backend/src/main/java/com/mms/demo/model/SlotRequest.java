package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotRequest {

    @NotNull(message = "Weekday cannot be null")
    private Integer weekday;

    @NotNull(message = "Start Time cannot be null")
    private String start;

    @NotNull(message = "End Time cannot be null")
    private String end;

}
