package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mms.demo.entity.Schedule;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponse {
    private Long id;
    private DoctorResponse doctorResponse;
    private SlotResponse slotResponse;
    private Integer week;
    private Integer year;
    private Boolean approval;

    public static ScheduleResponse createResponseFromSchedule(Schedule schedule) {

        DoctorResponse doctorResponse = DoctorResponse.createResponseFromDoctor(schedule.getDoctor());

        SlotResponse slotResponse = SlotResponse.createResponseFromSlot(schedule.getSlot());

        ScheduleResponse scheduleResponse = ScheduleResponse.builder()
                .id(schedule.getId())
                .doctorResponse(doctorResponse)
                .slotResponse(slotResponse)
                .year(schedule.getYear())
                .week(schedule.getWeek())
                .approval(schedule.getApproval())
                .build();

        return scheduleResponse;
    }

}
