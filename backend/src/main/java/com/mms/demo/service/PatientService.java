package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Patient;


/**
 * The Interface PatientService defines all the interactions needed between a high level controller
 * and the corresponding repository
 * 
 * @author Mananveer Singh
 */
public interface PatientService {
    /**
     * Gets all patients.
     *
     * @return the list of all patients
     */
    List<Patient> getAllPatients();

    /**
     * Gets the patient by id.
     *
     * @param id the id
     * @return the patient by id
     */
    Optional<Patient> getPatientById(Long id);

    /**
     * Creates the patient.
     *
     * @param patient the patient
     * @return the patient
     */
    Patient createPatient(Patient patient);

    /**
     * Update patient.
     *
     * @param id the id
     * @param patient the patient
     * @return the patient
     */
    Patient updatePatient(Long id, Patient patient);

    /**
     * Delete patient.
     *
     * @param id the id
     */
    void deletePatient(Long id);
}
