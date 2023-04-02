package com.mms.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;

/**
 * ScheduleRepository defines an interface to generate JPA defined queries to interact with the
 * schedule table.
 * 
 * @author Mananveer Singh
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    /**
     * Find all of a doctor's schedules.
     *
     * @param doctor the doctor
     * @return the list of Schedules belonging to the same Doctor
     */
    List<Schedule> findAllByDoctor(Doctor doctor);

    /**
     * Find all schedules corresponding to a doctor that fall between a range of days.
     * 
     * @param doctor the doctor
     * @param start the start of the range
     * @param end the end of the range
     * @return the list of all qualifying schedules
     */
    Optional<Schedule> findByDoctorAndStart(Doctor doctor, LocalDateTime start);

    List<Schedule> findAllByDoctorAndStartBetween(Doctor doctor, LocalDateTime start,
                    LocalDateTime end);
}
