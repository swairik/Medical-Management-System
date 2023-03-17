package com.mms.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;

public interface ScheduleService {
    Optional<Schedule> getScheduleById(Long id);

    List<Schedule> getAllSchedules();

    List<Schedule> getSchedulesByDoctor(Doctor doctor);

    List<Schedule> getSchedulesBySlot(Slot slot);

    List<Schedule> getSchedulesByDoctorAndWeekDay(Doctor doctor, LocalDate start, LocalDate end);

    Schedule createSchedule(Schedule schedule);

    Schedule updateSchedule(Long id, Schedule schedule);

    void deleteSchedule(Long id);
}
