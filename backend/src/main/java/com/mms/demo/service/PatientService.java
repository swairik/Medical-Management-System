package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.transferobject.PatientDTO;


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
    List<PatientDTO> getAll();

    /**
     * Gets the patient by id.
     *
     * @param id the id
     * @return the patient by id
     */
    Optional<PatientDTO> get(Long id);

    /**
     * Creates the patient.
     *
     * @param patient the patient
     * @return the patient
     */
    PatientDTO create(PatientDTO patient);

    /**
     * Update patient.
     *
     * @param id the id
     * @param patient the patient
     * @return the patient
     */
    Optional<PatientDTO> update(Long id, PatientDTO patient);

    /**
     * Delete patient.
     *
     * @param id the id
     */
    void delete(Long id);
}
