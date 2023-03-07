package com.mms.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

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

import com.mms.demo.entity.Patient;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.PatientRequest;
import com.mms.demo.model.PatientResponse;
import com.mms.demo.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @GetMapping("/display")
    public ResponseEntity<List<PatientResponse>> showAllPatients() {
        List<PatientResponse> response = new ArrayList<>();
        List<Patient> patients = patientService.getAllPatients();
        response = patients.stream().map((p) -> createResponseFromPatient(p)).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<PatientResponse> showAllPatients(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new CustomException("Patient with given id not found", "PATIENT_NOT_FOUND"));
        PatientResponse response = createResponseFromPatient(patient);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        List<Patient> patients = patientService.getAllPatients();
        Patient patientAlreadyCreated = patients.stream().filter((p) -> p.getEmail().equals(patientRequest.getEmail()))
                .findFirst().orElse(null);
        if (patientAlreadyCreated != null) {
            throw new CustomException("Patient with email id already exists", "PATIENT_ALREADY_CREATED");
        }
        Patient patient = createPatientFromRequest(patientRequest);
        Patient createdPatient = patientService.createPatient(patient);
        PatientResponse patientResponse = createResponseFromPatient(createdPatient);
        return new ResponseEntity<>(patientResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id,
            @Valid @RequestBody PatientRequest patientRequest) {
        Patient patient = createPatientFromRequest(patientRequest);
        Patient updatedPatient = patientService.createPatient(patient);
        PatientResponse patientResponse = createResponseFromPatient(updatedPatient);
        return new ResponseEntity<>(patientResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO : Add controllers for booking using session management
    // TODO : Add controllers for viewing report using session management

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

}
