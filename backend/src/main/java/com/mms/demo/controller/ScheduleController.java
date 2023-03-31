package com.mms.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<List<ScheduleDTO>> displaySchedulesByDoctor(@PathVariable Long did) {
        List<ScheduleDTO> schedulesList = scheduleService.getByDoctor(did);
        return new ResponseEntity<>(schedulesList, HttpStatus.OK);
    }

    // deprecated endpoint
    // @GetMapping("/display/schedule/{sid}")
    // public ResponseEntity<List<ScheduleResponse>>
    // displaySchedulesBySlot(@PathVariable Long sid) {}

    // @GetMapping("/display/approved/{did}")
    // public ResponseEntity<List<ScheduleResponse>>
    // displayApprovedSchedulesByDoctor(
    // @PathVariable Long did) {
    // Doctor doctor = doctorService.getDoctortById(did)
    // .orElseThrow(() -> new CustomException("Doctor with given id not found",
    // "DOCTOR_NOT_FOUND"));
    // List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
    // List<ScheduleResponse> response = schedules.stream().filter((s) ->
    // s.getApproval())
    // .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
    // .collect(Collectors.toList());
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    // @GetMapping("/display/patient/approved/{did}")
    // public ResponseEntity<List<ScheduleResponse>>
    // displayNonZeroApprovedSchedulesByDoctor(
    // @PathVariable Long did) {
    // Doctor doctor = doctorService.getDoctortById(did)
    // .orElseThrow(() -> new CustomException("Doctor with given id not found",
    // "DOCTOR_NOT_FOUND"));
    // List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
    // List<ScheduleResponse> response = schedules.stream().filter((s) ->
    // s.getApproval())
    // .filter((s) -> (s.getSlot().getCapacity() != 0))
    // .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
    // .collect(Collectors.toList());
    // return new ResponseEntity<>(response, HttpStatus.OK);
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

    // @GetMapping("/display/unapproved")
    // public ResponseEntity<List<ScheduleResponse>> displayUnapprovedSchedules() {
    // List<Schedule> schedules = scheduleService.getAllSchedules();
    // List<Schedule> unapprovedSchedules = schedules.stream()
    // .filter((s) -> (s.getApproval() == false)).collect(Collectors.toList());
    // List<ScheduleResponse> response = unapprovedSchedules.stream()
    // .map((s) -> ScheduleResponse.createResponseFromSchedule(s))
    // .collect(Collectors.toList());
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

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
    public ResponseEntity<ScheduleDTO> createSchedule(
            @Valid @RequestBody ScheduleRequest scheduleRequest,
            @AuthenticationPrincipal Credential user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(scheduleRequest.getStartTime(), formatter).truncatedTo(ChronoUnit.SECONDS);
        } catch (Exception e) {
            throw new CustomException("Wrong format of date & time", "WRONG_FROMAT", HttpStatus.BAD_REQUEST);
        }

        DoctorDTO doctor = doctorService.get(scheduleRequest.getDoctorId()).orElseThrow(
                () -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND", HttpStatus.NOT_FOUND));
        if (checkPermissions(user, doctor.getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to create another user's schedule",
                    "SCHEDULE_CREATION_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }

        List<ScheduleDTO> allSchedules = scheduleService.getByDoctor(scheduleRequest.getDoctorId());
        ScheduleDTO alreadyCreatedSchedule = allSchedules.stream().filter((s) -> s.getStart().equals(dateTime))
                .findFirst()
                .orElse(null);

        if (alreadyCreatedSchedule != null) {
            throw new CustomException("Schedule with given combination of doctor & time already exists",
                    "SCHEDULE_ALREADY_EXISTS", HttpStatus.CONFLICT);
        }
        System.out.println(scheduleRequest);
        ScheduleDTO createdSchedule = scheduleService.create(scheduleRequest.getDoctorId(), dateTime);

        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable Long id,
            @Valid @RequestBody ScheduleRequest scheduleRequest, @AuthenticationPrincipal Credential user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(scheduleRequest.getStartTime(), formatter).truncatedTo(ChronoUnit.SECONDS);
        } catch (Exception e) {
            throw new CustomException("Wrong format of date & time", "WRONG_FROMAT", HttpStatus.BAD_REQUEST);
        }

        ScheduleDTO schedule = scheduleService.get(id)
                .orElseThrow(() -> new CustomException("Schedule with given id not found", "SCHEDULE_NOT_FOUND",
                        HttpStatus.NOT_FOUND));
        if (checkPermissions(user, schedule.getDoctor().getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to edit another user's schedule",
                    "SCHEDULE_UPDATE_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }

        List<ScheduleDTO> allSchedules = scheduleService.getByDoctor(scheduleRequest.getDoctorId());
        ScheduleDTO alreadyCreatedSchedule = allSchedules.stream().filter((s) -> s.getStart().equals(startTime))
                .findFirst()
                .orElse(null);

        if (alreadyCreatedSchedule != null) {
            throw new CustomException("Schedule with given combination of doctor & time already exists",
                    "SCHEDULE_ALREADY_EXISTS", HttpStatus.CONFLICT);
        }

        ScheduleDTO updateSchedule = ScheduleDTO.builder().approvalStatus(scheduleRequest.getApproval())
                .start(startTime).build();

        ScheduleDTO updatedSchedule = scheduleService.update(id, updateSchedule)
                .orElseThrow(() -> new CustomException("Error while updating schedule", "SCHEDULE_NOT_UPDATED",
                        HttpStatus.INTERNAL_SERVER_ERROR));

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
