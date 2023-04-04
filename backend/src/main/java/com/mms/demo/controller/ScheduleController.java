package com.mms.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Role;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.ScheduleRequest;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.ScheduleDTO;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PatientService patientService;

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/display")
    public ResponseEntity<List<ScheduleDTO>> displayAllSchedules() {
        List<ScheduleDTO> schedulesList = scheduleService.getAll();
        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<ScheduleDTO> displayScheduleById(@PathVariable Long id) {
        ScheduleDTO schedule = scheduleService.get(id)
                .orElseThrow(() -> new CustomException("Schedule with given id not found",
                        "SCHEDULE_NOT_FOUND", HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping("/display/upcoming")
    public ResponseEntity<List<ScheduleDTO>> displayAllSchedulesUpcoming(@RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter).truncatedTo(ChronoUnit.MINUTES);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT",
                            HttpStatus.BAD_REQUEST);
        }
        List<ScheduleDTO> schedulesList = scheduleService.getAllAfter(dateTime);
        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }


    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<List<ScheduleDTO>> displaySchedulesByDoctor(@PathVariable Long did) {
        List<ScheduleDTO> schedulesList = scheduleService.getByDoctor(did);
        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }

    // deprecated endpoint
    // @GetMapping("/display/schedule/{sid}")
    // public ResponseEntity<List<ScheduleResponse>>
    // displaySchedulesBySlot(@PathVariable Long sid) {}

    @GetMapping("/display/approved/{did}")
    public ResponseEntity<List<ScheduleDTO>> displayApprovedSchedulesByDoctor(
            @PathVariable Long did) {
        List<ScheduleDTO> schedulesList = scheduleService.getApprovedByDoctor(did, true, Optional.empty());
        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }

    // ? can be modified to cater to all upcoming-style requests with customizable
    // approved/booked fields
    @GetMapping("/display/doctor/{did}/upcoming")
    public ResponseEntity<List<ScheduleDTO>> displayApprovedSchedulesByDoctorUpcoming(
            @PathVariable Long did, @RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter).truncatedTo(ChronoUnit.MINUTES);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT",
                    HttpStatus.BAD_REQUEST);
        }

        List<ScheduleDTO> schedulesList = scheduleService.getApprovedByDoctor(did, true, Optional.of(dateTime));
        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }

    @GetMapping("/display/doctor/{did}/upcomingAll")
    public ResponseEntity<List<ScheduleDTO>> displayAllSchedulesByDoctorUpcoming(
            @PathVariable Long did, @RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter).truncatedTo(ChronoUnit.MINUTES);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT",
                    HttpStatus.BAD_REQUEST);
        }

        List<ScheduleDTO> schedulesList = scheduleService.getByDoctorAfter(did, dateTime);

        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }

    // TODO: Add a stamp field since this endpoint is only supposed to show upcoming
    // schedules
    @GetMapping("/display/patient/approved/{did}")
    public ResponseEntity<List<ScheduleDTO>> displayNonZeroApprovedSchedulesByDoctor(
            @PathVariable Long did) {
        List<ScheduleDTO> schedulesList = scheduleService.getBookedAndApprovedByDoctor(did, true,
                false, Optional.empty());
        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }

    // @GetMapping("/display/patient/approved/{did}")
    // public ResponseEntity<List<ScheduleDTO>>
    // displayNonZeroApprovedSchedulesByDoctor(
    // @PathVariable Long did) {
    // List<ScheduleDTO> schedulesList =
    // scheduleService.getBookedAndApprovedByDoctor(did);
    // return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    // }

    // @GetMapping("/display/{pid}/{did}")
    // public ResponseEntity<List<ScheduleResponse>>
    // displayDoctorSchedulesByPatient(
    // @PathVariable Long did, @PathVariable Long pid) {
    // Doctor doctor = doctorService.getDoctortById(did)
    // .orElseThrow(() -> new CustomException("Doctor with given id not found",
    // "DOCTOR_NOT_FOUND"));
    // Patient patient = patientService.getPatientById(pid)
    // .orElseThrow(() -> new CustomException("Patient with given id not found",
    // "PATIENT_NOT_FOUND"));
    // List<Appointment> bookedAppointments =
    // appointmentService.getAppointmentsByPatient(patient);
    // List<Long> bookedSlotsByPatient = bookedAppointments.stream().map((s) ->
    // (s.getId()))
    // .collect(Collectors.toList());
    // List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
    // List<ScheduleResponse> response = schedules.stream().filter((s) ->
    // s.getApproval())
    // .filter((s) -> (!bookedSlotsByPatient.contains(s.getId())))
    // .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
    // .collect(Collectors.toList());
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @GetMapping("/display/unapproved")
    public ResponseEntity<List<ScheduleDTO>> displayUnapprovedSchedules() {
        List<ScheduleDTO> schedulesList = scheduleService.getAllUnapproved();
        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }

    // @GetMapping("/display/doctor/{id}/weekday")
    // public ResponseEntity<List<ScheduleResponse>>
    // displayDoctorSchedulesByPatientWeekday(
    // @PathVariable Long id, @RequestParam String from, @RequestParam String to) {
    // Doctor doctor = doctorService.getDoctortById(id)
    // .orElseThrow(() -> new CustomException("Doctor with given id not found",
    // "DOCTOR_NOT_FOUND"));
    // LocalDate start, end;
    // try {
    // start = LocalDate.parse(from);
    // end = LocalDate.parse(to);
    // } catch (Exception e) {
    // throw new CustomException("Error while parsing from and to date",
    // "WRONG_FORMAT");
    // }
    // List<Schedule> schedules =
    // scheduleService.getSchedulesByDoctorAndWeekDay(doctor, start, end);
    // List<ScheduleResponse> response = schedules.stream()
    // .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
    // .collect(Collectors.toList());
    // return new ResponseEntity<>(response, HttpStatus.OK);

    // }

    @PostMapping("/")
    public ResponseEntity<List<ScheduleDTO>> createSchedule(
            @Valid @RequestBody ScheduleRequest scheduleRequest,
            @AuthenticationPrincipal Credential user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startTime;
        Optional<LocalDateTime> endTime = Optional.empty();
        try {
            startTime = LocalDateTime.parse(scheduleRequest.getStartTime(), formatter)
                    .truncatedTo(ChronoUnit.SECONDS);
            if (scheduleRequest.getEndTime().isEmpty() == false)
                endTime = Optional.ofNullable(
                        LocalDateTime.parse(scheduleRequest.getEndTime(), formatter)
                                .truncatedTo(ChronoUnit.SECONDS));
        } catch (Exception e) {
            throw new CustomException("Wrong format of date & time", "WRONG_FROMAT",
                    HttpStatus.BAD_REQUEST);
        }

        DoctorDTO doctor = doctorService.get(scheduleRequest.getDoctorId())
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND", HttpStatus.NOT_FOUND));
        if (checkPermissions(user, doctor.getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to create another user's schedule",
                    "SCHEDULE_CREATION_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }

        // List<ScheduleDTO> allSchedules =
        // scheduleService.getByDoctor(scheduleRequest.getDoctorId());
        // ScheduleDTO alreadyCreatedSchedule = allSchedules.stream().filter((s) ->
        // s.getStart().equals(startTime))
        // .findFirst()
        // .orElse(null);

        // if (alreadyCreatedSchedule != null) {
        // throw new CustomException("Schedule with given combination of doctor & time
        // already exists",
        // "SCHEDULE_ALREADY_EXISTS", HttpStatus.CONFLICT);
        // }
        List<ScheduleDTO> createdSchedule = scheduleService.create(scheduleRequest.getDoctorId(), startTime, endTime);

        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable Long id,
            @Valid @RequestBody ScheduleRequest scheduleRequest,
            @AuthenticationPrincipal Credential user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(scheduleRequest.getStartTime(), formatter)
                    .truncatedTo(ChronoUnit.SECONDS);
        } catch (Exception e) {
            throw new CustomException("Wrong format of date & time", "WRONG_FROMAT",
                    HttpStatus.BAD_REQUEST);
        }

        ScheduleDTO schedule = scheduleService.get(id)
                .orElseThrow(() -> new CustomException("Schedule with given id not found",
                        "SCHEDULE_NOT_FOUND", HttpStatus.NOT_FOUND));
        if (checkPermissions(user, schedule.getDoctor().getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to edit another user's schedule",
                    "SCHEDULE_UPDATE_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }

        List<ScheduleDTO> allSchedules = scheduleService.getByDoctor(scheduleRequest.getDoctorId());
        ScheduleDTO alreadyCreatedSchedule = allSchedules.stream()
                .filter((s) -> s.getStart().equals(startTime)).findFirst().orElse(null);

        if (alreadyCreatedSchedule != null) {
            throw new CustomException(
                    "Schedule with given combination of doctor & time already exists",
                    "SCHEDULE_ALREADY_EXISTS", HttpStatus.CONFLICT);
        }

        ScheduleDTO updateSchedule = ScheduleDTO.builder()
                .approvalStatus(scheduleRequest.getApproval()).start(startTime).build();

        ScheduleDTO updatedSchedule = scheduleService.update(id, updateSchedule)
                .orElseThrow(() -> new CustomException("Error while updating schedule",
                        "SCHEDULE_NOT_UPDATED", HttpStatus.INTERNAL_SERVER_ERROR));

        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> updateApproval(@PathVariable Long id) {
        scheduleService.markAsApproved(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id,
            @AuthenticationPrincipal Credential user) {
        ScheduleDTO schedule = scheduleService.get(id)
                .orElseThrow(() -> new CustomException("Schedule with given id not found",
                        "SCHEDULE_NOT_FOUND", HttpStatus.NOT_FOUND));

        if (checkPermissions(user, schedule.getDoctor().getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to delete another user's schedule",
                    "SCHEDULE_DELETE_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }

        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Boolean checkPermissions(Credential user, String email) {
        if (user.getRole().equals(Role.DOCTOR) && !user.getEmail().equals(email))
            return false;
        return true;
    }

}
