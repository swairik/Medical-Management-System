// package com.mms.demo.model;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// import java.time.LocalDate;

// import com.mms.demo.entity.Schedule;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class ScheduleResponse {
//     private Long id;
//     private DoctorResponse doctorResponse;
//     private SlotResponse slotResponse;
//     private LocalDate weekDate;
//     private Boolean approval;

//     public static ScheduleResponse createResponseFromSchedule(Schedule schedule) {

//         DoctorResponse doctorResponse =
//                         DoctorResponse.createResponseFromDoctor(schedule.getDoctor());

//         SlotResponse slotResponse = SlotResponse.createResponseFromSlot(schedule.getSlot());

//         ScheduleResponse scheduleResponse = ScheduleResponse.builder().id(schedule.getId())
//                         .doctorResponse(doctorResponse).slotResponse(slotResponse)
//                         .weekDate(schedule.getWeekDate()
//                                         .plusDays(slotResponse.getWeekday().getValue()))
//                         .approval(schedule.getApproval()).build();

//         return scheduleResponse;
//     }

// }
