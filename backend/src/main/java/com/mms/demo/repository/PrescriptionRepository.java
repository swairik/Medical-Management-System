package com.mms.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Prescription;

/**
 * PrescriptionRepository defines an interface to generate JPA defined queries to interact with the
 * Prescription table.
 * 
 * @author Mananveer Singh
 */
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    /**
     * Find all prescriptions that share the same time stamp.
     * 
     * @deprecated No longer necessary since the lookup coroutine for prescriptions no longer
     *             depends only on stamps.
     * 
     * @param stamp the timestamp at which the prescription was issued
     * @return the list of prescriptions that share the stamp
     */
    @Deprecated
    List<Prescription> findAllByStamp(LocalDateTime stamp);

    /**
     * Find all prescriptions that contain timestamps between the given range.
     * 
     * @deprecated No longer necessary since the lookup coroutine for prescriptions no longer
     *             depends only on stamps.
     * 
     * @param start the start of the range
     * @param end the end of the range
     * @return the list of prescriptions issued within the range
     */
    @Deprecated
    List<Prescription> findAllByStampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find all prescriptions issued by a doctor.
     * 
     * @param doctor the doctor
     * @return the list of all such prescriptions
     */
    List<Prescription> findAllByDoctor(Doctor doctor);

    /**
     * Find all the prescriptions issued to the same patient.
     * 
     * @param patient the patient
     * @return the list of all of a patient's prescriptions
     */
    List<Prescription> findAllByPatient(Patient patient);
}
