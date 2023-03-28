package com.mms.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;

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
    Optional<Schedule> getScheduleById(Long id);

    /**
     * Gets all schedules.
     *
     * @return the list of all schedules
     */
    List<Schedule> getAllSchedules();


    /**
     * Gets the schedules by doctor.
     *
     * @param doctor the doctor
     * @return the schedules by doctor
     */
    List<Schedule> getSchedulesByDoctor(Doctor doctor);

    /**
     * Gets the schedules by slot.
     *
     * @param slot the slot
     * @return the schedules by slot
     * @deprecated
     */
    @Deprecated
    List<Schedule> getSchedulesBySlot(Slot slot);

    /**
     * Gets all schedules assigned to a doctor between given days.
     * 
     * @param doctor the doctor
     * @param start the first day
     * @param end the last day
     * @return the list of qualifying schedule entries
     * @deprecated
     */
    @Deprecated
    List<Schedule> getSchedulesByDoctorAndWeekDay(Doctor doctor, LocalDate start, LocalDate end);

    /**
     * Creates the schedule.
     *
     * @param schedule the schedule
     * @return the schedule
     */
    Schedule createSchedule(Schedule schedule);

    /**
     * Update schedule.
     *
     * @param id the id
     * @param schedule the schedule
     * @return the schedule
     */
    Schedule updateSchedule(Long id, Schedule schedule);

    /**
     * Delete schedule.
     *
     * @param id the id
     */
    void deleteSchedule(Long id);
}
