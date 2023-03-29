package com.mms.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.ScheduleDTO;

/**
 * The Interface ScheduleService defines all the interactions needed between a high level controller
 * and the corresponding repository
 * 
 * @author Mananveer Singh
 */
public interface ScheduleService {
    /**
     * Gets the schedule by id.
     *
     * @param id the id
     * @return the schedule by id
     */
    Optional<ScheduleDTO> get(Long id);

    /**
     * Gets all schedules.
     *
     * @return the list of all schedules
     */
    List<ScheduleDTO> getAll();


    /**
     * Gets the schedules by doctor.
     *
     * @param doctor the doctor
     * @return the schedules by doctor
     */
    List<ScheduleDTO> getByDoctor(Long doctorID) throws IllegalArgumentException;


    /**
     * Gets all schedules assigned to a doctor between given days.
     * 
     * @param doctor the doctor
     * @param start the first day
     * @param end the last day
     * @return the list of qualifying schedule entries
     */
    List<ScheduleDTO> getByDoctorBetween(Long doctorID, LocalDateTime start, LocalDateTime end)
                    throws IllegalArgumentException;

    /**
     * Creates the schedule.
     *
     * @param schedule the schedule
     * @return the schedule
     */
    ScheduleDTO create(Long doctorID, LocalDateTime start) throws IllegalArgumentException;

    /**
     * Update start and end, and approval status of a schedule.
     *
     * @param id the id
     * @param schedule the schedule
     * @return the schedule
     */
    Optional<ScheduleDTO> update(Long id, ScheduleDTO scheduleUpdates)
                    throws IllegalArgumentException;

    /**
     * Delete schedule.
     *
     * @param id the id
     */
    void deleteSchedule(Long id);
}
