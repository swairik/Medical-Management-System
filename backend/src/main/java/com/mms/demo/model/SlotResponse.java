package com.mms.demo.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.mms.demo.entity.Slot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotResponse {
    private Long id;
    private DayOfWeek weekday;
    private LocalTime start;
    private LocalTime end;
    private Integer capacity;

    public static SlotResponse createResponseFromSlot(Slot slot) {
        SlotResponse slotResponse = SlotResponse.builder()
                .id(slot.getId())
                .weekday(slot.getWeekday())
                .start(slot.getStart())
                .end(slot.getEnd())
                .capacity(slot.getCapacity())
                .build();
        return slotResponse;
    }

}
