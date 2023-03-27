package com.mms.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;

/**
 * The Interface AppointmentService defines all the interactions needed between a high level
 * controller and the corresponding repository.
 * 
 * @author Mananveer Singh
 */
public interface AppointmentService {
    /**
     * Gets the appointment by id.
     *
     * @param id the id
     * @return the appointment by id
     */
    Optional<Appointment> getAppointmentById(Long id);

    /**
     * Gets all the appointments stored in the database.
     *
     * @return list of all appointments
     */
    List<Appointment> getAllAppointments();

    /**
     * Gets the appointments by patient.
     *
     * @param patient the patient
     * @return the appointments by patient
     */
    List<Appointment> getAppointmentsByPatient(Patient patient);

    /**
     * Gets the appointments by slot.
     *
     * @param slot the slot
     * @return the appointments by slot
     */
    List<Appointment> getAppointmentsBySlot(Slot slot);

    /**
     * Gets all the appointments of a patient scheduled inside a range of time.
     * 
     * @param patient the patient
     * @param start the start of the range
     * @param end the end of the range
     * @return tle list of all qualifying appointments
     */
    List<Appointment> getAllByPatientBetween(Patient patient, LocalDateTime start,
                    LocalDateTime end);

    /**
     * Gets all the appointments of a patient scheduled after a given time.
     * 
     * @param patient the patient
     * @param stamp the time stamp after which the appointments are valid for consideration
     * @return the list of all qualifying appointments
     */
    List<Appointment> getAllByPatientAfter(Patient patient, LocalDateTime stamp);

    /**
     * Creates the appointment.
     *
     * @param appointment the appointment
     * @return the appointment
     */
    Appointment createAppointment(Appointment appointment);

    /**
     * Update appointment.
     *
     * @param id the id
     * @param appointmentUpdates the appointment updates
     * @return the appointment
     */
    Appointment updateAppointment(Long id, Appointment appointmentUpdates);

    /**
     * Delete appointment.
     *
     * @param id the id
     */
    void deleteAppointment(Long id);
}
