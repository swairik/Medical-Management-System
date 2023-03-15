package com.mms.demo.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Role;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;
import com.mms.demo.exception.CustomException;
import com.mms.demo.exception.Custom403Exception;
import com.mms.demo.model.ScheduleRequest;
import com.mms.demo.model.ScheduleResponse;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.service.SlotService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

        @Autowired
        DoctorService doctorService;

        @Autowired
        SlotService slotService;

        @Autowired
        ScheduleService scheduleService;

        @Autowired
        PatientService patientService;

        @Autowired
        AppointmentService appointmentService;

        @GetMapping("/display")
        public ResponseEntity<List<ScheduleResponse>> displayAllSchedules() {
                List<Schedule> schedules = scheduleService.getAllSchedules();
                List<ScheduleResponse> response = schedules.stream()
                                .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/{id}")
        public ResponseEntity<ScheduleResponse> displayScheduleById(@PathVariable Long id) {
                Schedule schedule = scheduleService.getScheduleById(id)
                                .orElseThrow(() -> new CustomException("Schedule with given id not found",
                                                "SCHEDULE_NOT_FOUND"));
                ScheduleResponse response = ScheduleResponse.createResponseFromSchedule(schedule);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/doctor/{did}")
        public ResponseEntity<List<ScheduleResponse>> displaySchedulesByDoctor(@PathVariable Long did) {
                Doctor doctor = doctorService.getDoctortById(did)
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));
                List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
                List<ScheduleResponse> response = schedules.stream()
                                .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/schedule/{sid}")
        public ResponseEntity<List<ScheduleResponse>> displaySchedulesBySlot(@PathVariable Long sid) {
                Slot slot = slotService.getSlotById(sid)
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));
                List<Schedule> schedules = scheduleService.getSchedulesBySlot(slot);
                List<ScheduleResponse> response = schedules.stream()
                                .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/approved/{did}")
        public ResponseEntity<List<ScheduleResponse>> displayApprovedSchedulesByDoctor(@PathVariable Long did) {
                Doctor doctor = doctorService.getDoctortById(did)
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));
                List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
                List<ScheduleResponse> response = schedules.stream().filter((s) -> s.getApproval())
                                .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/patient/approved/{did}")
        public ResponseEntity<List<ScheduleResponse>> displayNonZeroApprovedSchedulesByDoctor(@PathVariable Long did) {
                Doctor doctor = doctorService.getDoctortById(did)
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));
                List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
                List<ScheduleResponse> response = schedules.stream().filter((s) -> s.getApproval())
                                .filter((s) -> (s.getSlot().getCapacity() != 0))
                                .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/{pid}/{did}")
        public ResponseEntity<List<ScheduleResponse>> displayDoctorSchedulesByPatient(@PathVariable Long did,
                        @PathVariable Long pid) {
                Doctor doctor = doctorService.getDoctortById(did)
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));
                Patient patient = patientService.getPatientById(pid).orElseThrow(
                                () -> new CustomException("Patient with given id not found", "PATIENT_NOT_FOUND"));
                List<Appointment> bookedAppointments = appointmentService.getAppointmentsByPatient(patient);
                List<Long> bookedSlotsByPatient = bookedAppointments.stream().map((s) -> (s.getId()))
                                .collect(Collectors.toList());
                List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
                List<ScheduleResponse> response = schedules.stream().filter((s) -> s.getApproval())
                                .filter((s) -> (!bookedSlotsByPatient.contains(s.getId())))
                                .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/unapproved")
        public ResponseEntity<List<ScheduleResponse>> displayUnapprovedSchedules() {
                List<Schedule> schedules = scheduleService.getAllSchedules();
                List<Schedule> unapprovedSchedules = schedules.stream().filter((s) -> (s.getApproval() == false))
                                .collect(Collectors.toList());
                List<ScheduleResponse> response = unapprovedSchedules.stream()
                                .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PutMapping("/{id}/approve")
        public ResponseEntity<ScheduleResponse> updateApproval(@PathVariable Long id) {
                Schedule schedule = scheduleService.getScheduleById(id).orElseThrow(
                                () -> new CustomException("Schedule with given id not found", "SCHEDULE_NOT_FOUND"));
                schedule.setApproval(true);
                Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);
                ScheduleResponse response = ScheduleResponse.createResponseFromSchedule(updatedSchedule);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PostMapping("/")
        public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest,
                        @AuthenticationPrincipal Credential user) {
                Schedule schedule = createScheduleFromRequest(scheduleRequest);
                if (checkPermissions(user, schedule.getDoctor().getEmail())) {
                        throw new Custom403Exception("Logged in user is not permitted to edit another user's schedule",
                                        "SCHEDULE_CREATION_NOT_ALLOWED");
                }
                Schedule createdSchedule = scheduleService.createSchedule(schedule);
                ScheduleResponse response = ScheduleResponse.createResponseFromSchedule(createdSchedule);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id,
                        @Valid @RequestBody ScheduleRequest scheduleRequest) {

                Schedule udpateSchedule = createScheduleFromRequest(scheduleRequest);
                Schedule updatedSchedule = scheduleService.updateSchedule(id, udpateSchedule);
                ScheduleResponse response = ScheduleResponse.createResponseFromSchedule(updatedSchedule);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
                scheduleService.deleteSchedule(id);
                return new ResponseEntity<>(HttpStatus.OK);
        }

        public Schedule createScheduleFromRequest(ScheduleRequest scheduleRequest) {
                Doctor doctor = doctorService.getDoctortById(scheduleRequest.getDoctorId())
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));
                Slot slot = slotService.getSlotById(scheduleRequest.getSlotId())
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));
                String weekDate = scheduleRequest.getWeekDate();

                Pattern pattern = Pattern.compile("^(?<year>\\d{4})-W(?<week>\\d{2})$");
                Matcher matcher = pattern.matcher(weekDate);

                if (!matcher.find()) {
                        throw new CustomException("Wrong format of weekDate", "WRONG_FORMAT");
                }

                Integer year = Integer.parseInt(matcher.group("year"));
                Integer week = Integer.parseInt(matcher.group("week"));

                if (week < 1 || week > 52) {
                        throw new CustomException("Week is not valid", "INVALID_WEEK_FORMAT");
                }

                Schedule schedule = Schedule.builder()
                                .doctor(doctor)
                                .slot(slot)
                                .week(week)
                                .year(year)
                                .approval(false)
                                .build();

                return schedule;
        }

        private Boolean checkPermissions(Credential user, String email) {
                if (user.getRole().equals(Role.DOCTOR) && !user.getEmail().equals(email))
                        return false;
                return true;
        }

}
