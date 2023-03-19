package com.mms.demo.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Role;
import com.mms.demo.entity.Slot;
import com.mms.demo.exception.Custom403Exception;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.AppointmentRequest;
import com.mms.demo.model.AppointmentResponse;
import com.mms.demo.model.DoctorResponse;
import com.mms.demo.model.PatientResponse;
import com.mms.demo.model.ScheduleResponse;
import com.mms.demo.model.SlotResponse;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.service.SlotService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

        @Autowired
        PatientService patientService;

        @Autowired
        DoctorService doctorService;

        @Autowired
        SlotService slotService;

        @Autowired
        AppointmentService appointmentService;

        @Autowired
        ScheduleService scheduleService;

        @GetMapping("/display")
        public ResponseEntity<List<AppointmentResponse>> displayAllAppointments() {
                List<Appointment> appointments = appointmentService.getAllAppointments();
                List<AppointmentResponse> response = appointments.stream()
                                .map((a) -> createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/{id}")
        public ResponseEntity<AppointmentResponse> displayAppointmentById(@PathVariable Long id) {
                Appointment appointment = appointmentService.getAppointmentById(id)
                                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));
                AppointmentResponse response = createResponseFromAppointment(appointment);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/patient/{id}")
        public ResponseEntity<List<AppointmentResponse>> displayAppointmentsByPatient(
                        @PathVariable Long id) {
                Patient patient = patientService.getPatientById(id)
                                .orElseThrow(() -> new CustomException("Patient with given id not found",
                                                "PATIENT_NOT_FOUND"));
                List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
                List<AppointmentResponse> response = appointments.stream()
                                .map((a) -> createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/slot/{id}")
        public ResponseEntity<List<AppointmentResponse>> displayAppointmentsBySlot(
                        @PathVariable Long id) {
                Slot slot = slotService.getSlotById(id)
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));
                List<Appointment> appointments = appointmentService.getAppointmentsBySlot(slot);
                List<AppointmentResponse> response = appointments.stream()
                                .map((a) -> createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/doctor/{id}/upcoming")
        public ResponseEntity<List<AppointmentResponse>> showUpcomingAppointmentsByDoctor(@PathVariable Long id,
                        @RequestParam String stamp) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime dateTime;
                try {
                        dateTime = LocalDateTime.parse(stamp, formatter);
                } catch (Exception e) {
                        throw new CustomException("Wrong format of stamp", "WRONG_FORMAT");
                }

                Doctor doctor = doctorService.getDoctortById(id)
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));

                List<Appointment> appointments = appointmentService.getAllAppointments();
                List<Appointment> filteredAppointments = appointments.stream().filter((a) -> {
                        List<Schedule> schedules = scheduleService.getSchedulesBySlot(a.getSlot());
                        List<Schedule> filteredSchedules = schedules.stream()
                                        .filter((s) -> s.getSlot().getId() == a.getSlot().getId()
                                                        && s.getDoctor().getId() == id)
                                        .collect(Collectors.toList());

                        if (filteredSchedules.size() == 0) {
                                throw new CustomException("Invalid schedule id", "SCHEDULE_NOT_FOUND");
                        }

                        Slot slot = slotService.getSlotById(a.getSlot().getId()).orElseThrow(
                                        () -> new CustomException("Slot with given id not found", "SLOT_NOT_FOUND"));

                        LocalDateTime appointmentDateTime = filteredSchedules.get(0).getWeekDate()
                                        .atTime(slot.getStart());

                        return appointmentDateTime.isAfter(dateTime);
                }).collect(Collectors.toList());

                // slots for a doctor scheduled between
                // appointments after a specific date (now)
                // LocalDateTime temporalTargetStart = LocalDateTime.now();
                // LocalDateTime temporalTargetEnd =
                // LocalDateTime.now().withMonth(12).withDayOfMonth(31);
                // List<Schedule> schedulesUpcoming =
                // scheduleService.getSchedulesByDoctorAndWeekDay(doctor,
                // temporalTargetStart.toLocalDate(), temporalTargetEnd.toLocalDate());

                // List<Appointment> appointments = appointmentService.getAllAppointments();
                // List<Appointment> validAppointments = appointments.stream().filter((a) ->
                // a.getId() != 0)
                // .collect(Collectors.toList());

                // Set<Slot> validSlots = new HashSet<>();
                // for (Schedule sched : schedulesUpcoming) {
                // validSlots.add(sched.getSlot());
                // }

                // List<Appointment> validAppointments = new ArrayList<Appointment>();
                // for (Slot slot : validSlots) {
                // validAppointments.addAll(appointmentService.getAppointmentsBySlot(slot).stream()
                // .filter(a -> a.getSlot().getStart()
                // .isAfter(temporalTargetStart
                // .truncatedTo(ChronoUnit.SECONDS).toLocalTime())
                // &&
                // a.getSlot().getEnd().isBefore(temporalTargetEnd
                // .truncatedTo(ChronoUnit.SECONDS).toLocalTime()))
                // .collect(Collectors.toList()));
                // }

                List<AppointmentResponse> response = filteredAppointments.stream()
                                .map((a) -> createResponseFromAppointment(a))
                                .collect(Collectors.toList());

                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/patient/{id}/upcoming")
        public ResponseEntity<List<AppointmentResponse>> showAllPatientUpcoming(@PathVariable Long id,
                        @RequestParam String stamp) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime dateTime;
                try {
                        dateTime = LocalDateTime.parse(stamp, formatter);
                } catch (Exception e) {
                        throw new CustomException("Wrong format of stamp", "WRONG_FORMAT");
                }
                Patient patient = patientService.getPatientById(id)
                                .orElseThrow(() -> new CustomException("Patient with given id not found",
                                                "PATIENT_NOT_FOUND"));
                List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);

                List<Appointment> upcomingAppointments = appointments.stream().filter((a) -> {
                        List<Schedule> schedules = scheduleService.getSchedulesBySlot(a.getSlot());
                        List<Schedule> filteredSchedules = schedules.stream()
                                        .filter((s) -> s.getSlot().getId() == a.getSlot().getId())
                                        .collect(Collectors.toList());

                        if (filteredSchedules.size() == 0) {
                                throw new CustomException("Invalid slot id", "SCHEDULE_NOT_FOUND");
                        }

                        Slot slot = slotService.getSlotById(a.getSlot().getId()).orElseThrow(
                                        () -> new CustomException("Slot with given id not found", "SLOT_NOT_FOUND"));

                        LocalDateTime appointmentDateTime = filteredSchedules.get(0).getWeekDate()
                                        .atTime(slot.getStart());

                        return appointmentDateTime.isAfter(dateTime);

                }).collect(Collectors.toList());

                List<AppointmentResponse> response = upcomingAppointments.stream()
                                .map((a) -> createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/patient/{id}/between")
        public ResponseEntity<List<AppointmentResponse>> showAllPatientBetween(@PathVariable Long id,
                        @RequestParam String start, @RequestParam String end) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime startTime, endTime;
                try {
                        startTime = LocalDateTime.parse(start, formatter);
                        endTime = LocalDateTime.parse(end, formatter);
                } catch (Exception e) {
                        throw new CustomException("Wrong format of timestamp", "WRONG_FORMAT");
                }
                Patient patient = patientService.getPatientById(id)
                                .orElseThrow(() -> new CustomException("Patient with given id not found",
                                                "PATIENT_NOT_FOUND"));
                List<Appointment> appointments = appointmentService.getAllByPatientBetween(patient, startTime, endTime);
                List<AppointmentResponse> response = appointments.stream()
                                .map((a) -> createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/doctor/{id}/between")
        public ResponseEntity<List<AppointmentResponse>> showAllDoctorAppointmentBetween(@PathVariable Long id,
                        @RequestParam String start, @RequestParam String end) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime startTime, endTime;
                try {
                        startTime = LocalDateTime.parse(start, formatter);
                        endTime = LocalDateTime.parse(end, formatter);
                } catch (Exception e) {
                        throw new CustomException("Wrong format of timestamp", "WRONG_FORMAT");
                }
                List<Appointment> appointments = appointmentService.getAllAppointments();
                List<Appointment> filteredAppointments = appointments.stream().filter((a) -> {
                        List<Schedule> schedules = scheduleService.getSchedulesBySlot(a.getSlot());
                        List<Schedule> filteredSchedules = schedules.stream()
                                        .filter((s) -> s.getSlot().getId() == a.getSlot().getId())
                                        .collect(Collectors.toList());

                        if (filteredSchedules.size() == 0) {
                                throw new CustomException("Invalid slot id", "SCHEDULE_NOT_FOUND");
                        }

                        Slot slot = slotService.getSlotById(a.getSlot().getId()).orElseThrow(
                                        () -> new CustomException("Slot with given id not found", "SLOT_NOT_FOUND"));

                        LocalDateTime appointmentDateTime = filteredSchedules.get(0).getWeekDate()
                                        .atTime(slot.getStart());

                        return appointmentDateTime.isAfter(startTime) && appointmentDateTime.isBefore(endTime);
                }).collect(Collectors.toList());
                if (filteredAppointments.size() == 0) {
                        throw new CustomException("No appointments found in given time interval",
                                        "NO_APPOINTMENT_FOUND");
                }
                List<AppointmentResponse> response = filteredAppointments.stream()
                                .map((a) -> createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PostMapping("/")
        public ResponseEntity<AppointmentResponse> createAppointment(
                        @Valid @RequestBody AppointmentRequest appointmentRequest,
                        @AuthenticationPrincipal Credential user) {
                Appointment appointment = createAppointmentFromRequest(appointmentRequest);

                if (checkPermissions(user, appointment.getPatient().getEmail()) == false) {
                        throw new Custom403Exception(
                                        "Logged in user is not permitted to create another user's appointment",
                                        "APPOINTMENT_CREATION_NOT_ALLOWED");
                }

                if (user.getRole().equals(Role.PATIENT)) {
                        Patient patient = patientService.getPatientById(appointmentRequest.getPatientId())
                                        .orElseThrow(() -> new CustomException("Patient with given id not found",
                                                        "PATIENT_NOT_FOUND"));
                        if (patient.getGender() == null || patient.getAge() == null || patient.getPhone() == null) {
                                throw new CustomException("Patient with given id has incomplete details",
                                                "PATIENT_DETAILS_INCOMPLETE");
                        }
                }

                List<Appointment> allAppointments = appointmentService.getAllAppointments();

                Appointment alreadyCreatedAppointment = allAppointments.stream()
                                .filter((a) -> (a.getPatient().getId() == appointmentRequest.getPatientId()
                                                && a.getSlot().getId() == appointmentRequest.getSlotId()))
                                .findFirst().orElse(null);

                if (alreadyCreatedAppointment != null) {
                        throw new CustomException("Appointment with given patient id and slot id already created",
                                        "APPOINTMENT_ALREADY_CREATED");
                }

                Appointment createdAppointment = appointmentService.createAppointment(appointment);

                Slot slot = slotService.getSlotById(appointmentRequest.getSlotId())
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));

                slot.setCapacity(slot.getCapacity() - 1);
                slotService.updateSlot(slot.getId(), slot);

                AppointmentResponse response = createResponseFromAppointment(createdAppointment);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PutMapping("/{id}")
        public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Long id,
                        @Valid @RequestBody AppointmentRequest appointmentRequest,
                        @AuthenticationPrincipal Credential user) {
                Appointment appointment = appointmentService.getAppointmentById(id)
                                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));

                if (checkPermissions(user, appointment.getPatient().getEmail()) == false) {
                        throw new Custom403Exception(
                                        "Logged in user is not permitted to edit another user's appointments",
                                        "APPOINTMENT_EDIT_NOT_ALLOWED");

                }
                Appointment updateAppointment = createAppointmentFromRequest(appointmentRequest);
                Appointment updatedAppointment = appointmentService.updateAppointment(id, updateAppointment);
                AppointmentResponse response = createResponseFromAppointment(updatedAppointment);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteAppointment(@PathVariable Long id, @AuthenticationPrincipal Credential user) {
                Appointment appointment = appointmentService.getAppointmentById(id)
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));

                if (checkPermissions(user, appointment.getPatient().getEmail()) == false) {
                        throw new Custom403Exception(
                                        "Logged in user is not permitted to delete another user's appointments",
                                        "APPOINTMENT_DELETE_NOT_ALLOWED");

                }

                appointmentService.deleteAppointment(id);

                Slot slot = appointment.getSlot();
                Integer maxCapacity = (int) slot.getStart().until(slot.getEnd(), ChronoUnit.MINUTES) / 30;
                slot.setCapacity(Integer.min(slot.getCapacity() + 1, maxCapacity));
                slotService.updateSlot(slot.getId(), slot);

                return new ResponseEntity<>(HttpStatus.OK);
        }

        public AppointmentResponse createResponseFromAppointment(Appointment appointment) {
                PatientResponse patientResponse = PatientResponse.createResponseFromPatient(appointment.getPatient());
                List<Schedule> schedules = scheduleService.getAllSchedules();
                List<Schedule> filteredSchedules = schedules.stream()
                                .filter((s) -> {
                                        return s.getSlot().getId() == appointment.getSlot().getId();
                                })
                                .collect(Collectors.toList());
                if (filteredSchedules.size() == 0) {
                        throw new CustomException("Schedule does not exist", "SCHEDULE_NOT_FOUND");
                }
                ScheduleResponse scheduleResponse = ScheduleResponse
                                .createResponseFromSchedule(filteredSchedules.get(0));
                AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                                .id(appointment.getId())
                                .patientResponse(patientResponse)
                                .scheduleResponse(scheduleResponse)
                                .attended(appointment.getAttended())
                                .build();
                return appointmentResponse;
        }

        public Appointment createAppointmentFromRequest(AppointmentRequest appointmentRequest) {
                Patient patient = patientService.getPatientById(appointmentRequest.getPatientId())
                                .orElseThrow(() -> new CustomException("Patient with given id not found",
                                                "PATIENT_NOT_FOUND"));
                Slot slot = slotService.getSlotById(appointmentRequest.getSlotId())
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));
                if (slot.getCapacity() == 0) {
                        throw new CustomException("No more capacity for this slot", "SLOT_CAPACITY_LIMIT_REACHED");
                }
                Appointment appointment = Appointment.builder()
                                .patient(patient)
                                .slot(slot)
                                .build();
                return appointment;
        }

        private Boolean checkPermissions(Credential user, String email) {
                if (user.getRole().equals(Role.PATIENT) && !user.getEmail().equals(email))
                        return false;
                return true;
        }

}
