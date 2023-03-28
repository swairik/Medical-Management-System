package com.mms.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Schedule;
import com.mms.demo.transferobject.AppointmentDTO;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.PatientDTO;
import com.mms.demo.transferobject.ScheduleDTO;

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
    Optional<AppointmentDTO> get(Long id);

    /**
     * Gets all the appointments stored in the database.
     *
     * @return list of all appointments
     */
    List<AppointmentDTO> getAll();

    /**
     * Gets the appointments by patient.
     *
     * @param patient the patient
     * @return the appointments by patient
     */
    List<AppointmentDTO> getAllByPatient(Long patientID);


    /**
     * Gets all the appointments of a patient scheduled inside a range of time.
     * 
     * @param patient the patient
     * @param start the start of the range
     * @param end the end of the range
     * @return tle list of all qualifying appointments
     */
    List<AppointmentDTO> getAllByPatientBetween(Long patientID, LocalDateTime start,
                    LocalDateTime end);

    /**
     * Gets all the appointments of a patient scheduled after a given time.
     * 
     * @param patient the patient
     * @param stamp the time stamp after which the appointments are valid for consideration
     * @return the list of all qualifying appointments
     */
    List<AppointmentDTO> getAllByPatientAfter(Long patientID, LocalDateTime stamp);

    List<AppointmentDTO> getAllByDoctor(Long doctorID);

    List<AppointmentDTO> getAllByDoctorBetween(Long doctorID, LocalDateTime from, LocalDateTime to);

    List<AppointmentDTO> getAllByDoctorAfter(Long doctorID, LocalDateTime after);

    List<AppointmentDTO> getAllByPatientAndDoctor(Long patientID, Long doctorID);

    List<AppointmentDTO> getAllByPatientAndDoctorBetween(Long patientID, Long doctorID,
                    LocalDateTime start, LocalDateTime end);

    List<AppointmentDTO> getAllByPatientAndDoctorAfter(Long patientID, Long doctorID,
                    LocalDateTime after);


    /**
     * Creates the appointment.
     *
     * @param appointment the appointment
     * @return the appointment
     */
    AppointmentDTO create(Long patientID, Long scheduleID,
                    AppointmentDetailsDTO appointmentDetailsDTO) throws IllegalArgumentException;



    Optional<AppointmentDTO> updateSchedule(Long id, Long scheduleID)
                    throws IllegalArgumentException;

    void markAsAttended(Long id);

    /**
     * Delete appointment.
     *
     * @param id the id
     */
    void delete(Long id);
}
