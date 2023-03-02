package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Patient;


public interface PatientService {
    List<Patient> getAllPatients();
    Optional<Patient> getPatientById(Long id);

    Patient createPatient(Patient patient);
    Patient updatePatient(Long id, Patient patient);
    void deletePatient(Long id);
}
