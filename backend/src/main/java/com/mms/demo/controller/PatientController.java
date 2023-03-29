package com.mms.demo.controller;

import java.util.List;
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
import com.mms.demo.entity.Role;
import com.mms.demo.exception.Custom403Exception;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.PatientRequest;
import com.mms.demo.service.CredentialService;
import com.mms.demo.service.PatientService;
import com.mms.demo.transferobject.PatientDTO;

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
    public ResponseEntity<List<PatientDTO>> showAllPatients(
            @AuthenticationPrincipal Credential user) {

        List<PatientDTO> patientList = patientService.getAll();
        return new ResponseEntity<>(patientList, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<PatientDTO> showPatientById(@PathVariable Long id,
            @AuthenticationPrincipal Credential user) {
        PatientDTO patientDTO = patientService.get(id)
                .orElseThrow(() -> new CustomException("Patient with given id not found",
                        "PATIENT_NOT_FOUND"));

        if (checkPermissions(user, patientDTO.email()) == false) {
            throw new Custom403Exception(
                    "Logged in user is not permitted to view another user's profile",
                    "PROFILE_DISPLAY_NOT_ALLOWED");
        }

        return new ResponseEntity<>(patientDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id,
            @Valid @RequestBody PatientRequest patientRequest,
            @AuthenticationPrincipal Credential user) {

        // Check if patient exists or not
        PatientDTO patient = patientService.get(id)
                .orElseThrow(() -> new CustomException("Patient with given id not found",
                        "PATIENT_NOT_FOUND"));

        if (checkPermissions(user, patient.email()) == false) {
            throw new Custom403Exception(
                    "Logged in user is not permitted to edit another user's profile",
                    "PROFILE_DISPLAY_NOT_ALLOWED");
        }

        PatientDTO updatePatient = PatientRequest.createPatientFromRequest(id, patientRequest);
        PatientDTO updatedPatient = patientService.update(id, updatePatient)
                .orElseThrow(() -> new CustomException("Error while updating patient details", "PATIENT_NOT_UPDATED"));
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.get(id)
                .orElseThrow(() -> new CustomException("Patient with given id not found",
                        "PATIENT_NOT_FOUND"));
        // throw new CustomException("Account cannot be deleted",
        // "ACCOUNT_NOT_DELETED");
        // patientService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Boolean checkPermissions(Credential user, String email) {
        if (user.getRole().equals(Role.PATIENT) && !user.getEmail().equals(email))
            return false;
        return true;
    }

}
