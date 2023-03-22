package com.mms.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Role;
import com.mms.demo.exception.Custom403Exception;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.PatientRequest;
import com.mms.demo.model.PatientResponse;
import com.mms.demo.service.CredentialService;
import com.mms.demo.service.PatientService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @Autowired
    CredentialService credentialService;

    @GetMapping("/display")
    public ResponseEntity<List<PatientResponse>> showAllPatients(
                    @AuthenticationPrincipal Credential user) {

        List<PatientResponse> response = new ArrayList<>();
        List<Patient> patients = patientService.getAllPatients();
        response = patients.stream().map((p) -> PatientResponse.createResponseFromPatient(p))
                        .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<PatientResponse> showPatientById(@PathVariable Long id,
                    @AuthenticationPrincipal Credential user) {
        Patient patient = patientService.getPatientById(id)
                        .orElseThrow(() -> new CustomException("Patient with given id not found",
                                        "PATIENT_NOT_FOUND"));

        if (checkPermissions(user, patient.getEmail()) == false) {
            throw new Custom403Exception(
                            "Logged in user is not permitted to view another user's profile",
                            "PROFILE_DISPLAY_NOT_ALLOWED");

        }

        PatientResponse response = PatientResponse.createResponseFromPatient(patient);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id,
                    @Valid @RequestBody PatientRequest patientRequest,
                    @AuthenticationPrincipal Credential user) {

        // Check if patient exists or not
        Patient patient = patientService.getPatientById(id)
                        .orElseThrow(() -> new CustomException("Patient with given id not found",
                                        "PATIENT_NOT_FOUND"));

        if (checkPermissions(user, patient.getEmail()) == false) {
            throw new Custom403Exception(
                            "Logged in user is not permitted to edit another user's profile",
                            "PROFILE_DISPLAY_NOT_ALLOWED");

        }
        Patient updatePatient = PatientRequest.createPatientFromRequest(patientRequest);
        Patient updatedPatient = patientService.updatePatient(id, updatePatient);
        PatientResponse patientResponse = PatientResponse.createResponseFromPatient(updatedPatient);
        return new ResponseEntity<>(patientResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id)
                        .orElseThrow(() -> new CustomException("Patient with given id not found",
                                        "PATIENT_NOT_FOUND"));
        Credential credential = credentialService.getCredentialsByEmail(patient.getEmail())
                        .orElseThrow(() -> new CustomException("Patient credentials not found",
                                        "CREDENTIAL_NOT_FOUND"));
        credentialService.deleteCredentials(credential.getId());
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Boolean checkPermissions(Credential user, String email) {
        if (user.getRole().equals(Role.PATIENT) && !user.getEmail().equals(email))
            return false;
        return true;
    }

}
