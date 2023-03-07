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

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.AppointmentRequest;
import com.mms.demo.model.AppointmentResponse;
import com.mms.demo.model.PatientRequest;
import com.mms.demo.model.PatientResponse;
import com.mms.demo.model.SlotRequest;
import com.mms.demo.model.SlotResponse;
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
        List<AppointmentResponse> response = appointments.stream().map((a) -> createResponseFromAppointment(a))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<AppointmentResponse> displayAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new CustomException("Appointment with given id not found", "APPOINTMENT_NOT_FOUND"));
        AppointmentResponse response = createResponseFromAppointment(appointment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/patient")
    public ResponseEntity<List<AppointmentResponse>> displayAppointmentsByPatient(
            @RequestBody @Valid PatientRequest patientRequest) {
        Patient patient = createPatientFromRequest(patientRequest);
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
        List<AppointmentResponse> response = appointments.stream().map((a) -> createResponseFromAppointment(a))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/slot")
    public ResponseEntity<List<AppointmentResponse>> displayAppointmentsBySlot(
            @RequestBody @Valid SlotRequest slotRequest) {
        Slot slot = createSlotFromRequest(slotRequest);
        List<Appointment> appointments = appointmentService.getAppointmentsBySlot(slot);
        List<AppointmentResponse> response = appointments.stream().map((a) -> createResponseFromAppointment(a))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody AppointmentRequest appointmentRequest) {
        Appointment appointment = createAppointmentFromRequest(appointmentRequest);
        Appointment createdAppointment = appointmentService.createAppointment(appointment);
        AppointmentResponse response = createResponseFromAppointment(createdAppointment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Long id,
            @Valid @RequestBody AppointmentRequest appointmentRequest) {
        Appointment appointment = createAppointmentFromRequest(appointmentRequest);
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
        AppointmentResponse response = createResponseFromAppointment(updatedAppointment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Appointment createAppointmentFromRequest(AppointmentRequest appointmentRequest) {
        Patient patient = patientService.getPatientById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new CustomException("Patient with given id not found", "PATIENT_NOT_FOUND"));
        Slot slot = slotService.getSlotById(appointmentRequest.getSlotId())
                .orElseThrow(() -> new CustomException("Slot with given id not found", "SLOT_NOT_FOUND"));
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .slot(slot)
                .build();
        return appointment;
    }

    public AppointmentResponse createResponseFromAppointment(Appointment appointment) {
        PatientResponse patientResponse = createResponseFromPatient(appointment.getPatient());
        SlotResponse slotResponse = createResponseFromSlot(appointment.getSlot());
        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .id(appointment.getId())
                .patientResponse(patientResponse)
                .slotResponse(slotResponse)
                .build();
        return appointmentResponse;
    }

    public Patient createPatientFromRequest(PatientRequest patientRequest) {
        Patient patient = Patient.builder()
                .name(patientRequest.getName())
                .gender(patientRequest.getGender())
                .age(patientRequest.getAge())
                .email(patientRequest.getEmail())
                .phone(patientRequest.getPhone())
                .build();
        return patient;
    }

    public PatientResponse createResponseFromPatient(Patient patient) {
        PatientResponse patientResponse = new PatientResponse();
        BeanUtils.copyProperties(patient, patientResponse);
        return patientResponse;
    }

    public Slot createSlotFromRequest(SlotRequest slotRequest) {
        Slot slot = Slot.builder()
                .weekday(slotRequest.getWeekday())
                .start(slotRequest.getStart())
                .end(slotRequest.getEnd())
                .capacity(slotRequest.getCapacity())
                .build();
        return slot;
    }

    public SlotResponse createResponseFromSlot(Slot slot) {
        SlotResponse slotResponse = new SlotResponse();
        BeanUtils.copyProperties(slot, slotResponse);
        return slotResponse;
    }

}
