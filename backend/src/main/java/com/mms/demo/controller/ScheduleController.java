package com.mms.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;
import com.mms.demo.model.ScheduleRequest;
import com.mms.demo.model.ScheduleResponse;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.service.SlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    SlotService slotService;

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("/display")
    public ResponseEntity<List<ScheduleResponse>> displayAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        List<ScheduleResponse> response = schedules.stream().map((s) -> createResponseFromSchedule(s))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<ScheduleResponse> displayScheduleById(@PathVariable Long id) {
        Schedule schedule = scheduleService.getScheduleById(id)
                .orElseThrow(() -> new RuntimeException("Schedule with given id not found"));
        ScheduleResponse response = createResponseFromSchedule(schedule);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<List<ScheduleResponse>> displaySchedulesByDoctor(@PathVariable Long did) {
        Doctor doctor = doctorService.getDoctortById(did)
                .orElseThrow(() -> new RuntimeException("Doctor with given id not found"));
        List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
        List<ScheduleResponse> response = schedules.stream().map((s) -> createResponseFromSchedule(s))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/schedule/{sid}")
    public ResponseEntity<List<ScheduleResponse>> displaySchedulesBySlot(@PathVariable Long sid) {
        Slot slot = slotService.getSlotById(sid)
                .orElseThrow(() -> new RuntimeException("Slot with given id not found"));
        List<Schedule> schedules = scheduleService.getSchedulesBySlot(slot);
        List<ScheduleResponse> response = schedules.stream().map((s) -> createResponseFromSchedule(s))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        Schedule schedule = createScheduleFromRequest(scheduleRequest);
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        ScheduleResponse response = createResponseFromSchedule(createdSchedule);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id,
            @Valid @RequestBody ScheduleRequest scheduleRequest) {
        Schedule schedule = createScheduleFromRequest(scheduleRequest);
        Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);
        ScheduleResponse response = createResponseFromSchedule(updatedSchedule);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ScheduleResponse createResponseFromSchedule(Schedule schedule) {
        ScheduleResponse scheduleResponse = new ScheduleResponse();
        BeanUtils.copyProperties(schedule, scheduleResponse);
        return scheduleResponse;
    }

    public Schedule createScheduleFromRequest(ScheduleRequest scheduleRequest) {
        Doctor doctor = doctorService.getDoctortById(scheduleRequest.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor with given id not found"));
        Slot slot = slotService.getSlotById(scheduleRequest.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot with given id not found"));
        Schedule schedule = Schedule.builder()
                .doctor(doctor)
                .slot(slot)
                .week(scheduleRequest.getWeek())
                .build();

        return schedule;
    }

}
