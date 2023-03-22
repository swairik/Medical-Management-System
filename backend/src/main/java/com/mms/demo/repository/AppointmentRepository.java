package com.mms.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;

/**
 * AppointmentRepository defines an interface to generate JPA defined queries to interact with the
 * Appointment table.
 * 
 * @author Mananveer Singh
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    /**
     * Find all appointments that correspond to the same patient.
     * 
     * @see Patient
     * @param patient the patient
     * @return the list of appointments that have the given patient
     */
    List<Appointment> findAllByPatient(Patient patient);

    /**
     * Find all appointments that correspond to the same slot.
     * 
     * @see Slot
     * @param slot the slot
     * @return the list of appointments that have the same slot
     */
    List<Appointment> findAllBySlot(Slot slot);
}
