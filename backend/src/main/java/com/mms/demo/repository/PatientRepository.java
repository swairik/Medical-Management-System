package com.mms.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Patient;

/**
 * PatientRepository defines an interface to generate JPA defined queries to interact with the
 * Patient table.
 * 
 * @author Mananveer Singh
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {
    /**
     * Find a patient with the given email.
     *
     * @param email the email
     * @return the Patient with the given email
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Find all patients that were registered in a given time range. Meant for report keeping, so no
     * endpoints from the Controller layer map to this function.
     * 
     * @param start the start of the range
     * @param end the end of the range
     * @return the list of patients that registered in the given range
     */
    List<Patient> findAllByStampBetween(LocalDateTime start, LocalDateTime end);
}
