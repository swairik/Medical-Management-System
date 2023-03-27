package com.mms.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Prescription;

/**
 * The Interface PrescriptionService defines all the interactions needed between a high level
 * controller and the corresponding repository
 * 
 * @author Mananveer Singh
 */
public interface PrescriptionService {
    /**
     * Gets a prescription by its unique ID
     * 
     * @param id the id
     * @return the qualifying prescription
     */
    Optional<Prescription> getPrescriptionById(Long id);

    /**
     * Gets all prescriptions corresponding to a given time stamp
     * 
     * @deprecated No longer necessary since the lookup coroutine for prescriptions no longer
     *             depends only on stamps.
     * @param stamp the timestamp at which the prescription was issued
     * @return the list of prescriptions that share the stamp
     */
    @Deprecated
    List<Prescription> getPrescriptionsByStamp(LocalDateTime stamp);

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
    List<Prescription> getAllPrescriptionsByStampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * gets all prescriptions
     * 
     * @return the list of all prescriptions
     */
    List<Prescription> getAllPrescriptions();

    /**
     * Creates a prescription.
     * 
     * @param prescription the prescription
     * @return the prescription, if successful
     */
    Prescription createPrescription(Prescription prescription);

    /**
     * Deletes a prescription given its ID
     * 
     * @param id the id
     */
    void deletePrescription(Long id);

    /**
     * Updates a prescription with new details
     * 
     * @param id the id
     * @param update the updated details
     * @return the updated prescription object wrapped in an optional container
     */
    Optional<Prescription> updatePrescription(Long id, Prescription update);
}
