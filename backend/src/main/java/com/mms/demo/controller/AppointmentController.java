package com.mms.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

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
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Role;
import com.mms.demo.entity.Slot;
import com.mms.demo.exception.Custom403Exception;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.AppointmentRequest;
import com.mms.demo.model.AppointmentResponse;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.SlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

        @Autowired
        PatientService patientService;

        @Autowired
        SlotService slotService;

        @Autowired
        AppointmentService appointmentService;

        @GetMapping("/display")
        public ResponseEntity<List<AppointmentResponse>> displayAllAppointments() {
                List<Appointment> appointments = appointmentService.getAllAppointments();
                List<AppointmentResponse> response = appointments.stream()
                                .map((a) -> AppointmentResponse.createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/{id}")
        public ResponseEntity<AppointmentResponse> displayAppointmentById(@PathVariable Long id) {
                Appointment appointment = appointmentService.getAppointmentById(id)
                                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));
                AppointmentResponse response = AppointmentResponse.createResponseFromAppointment(appointment);
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
                                .map((a) -> AppointmentResponse.createResponseFromAppointment(a))
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
                                .map((a) -> AppointmentResponse.createResponseFromAppointment(a))
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

                AppointmentResponse response = AppointmentResponse.createResponseFromAppointment(createdAppointment);
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
                AppointmentResponse response = AppointmentResponse.createResponseFromAppointment(updatedAppointment);
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
