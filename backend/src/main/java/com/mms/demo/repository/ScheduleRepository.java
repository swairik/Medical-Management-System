package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByDoctor(Doctor doctor);
    List<Schedule> findAllBySlot(Slot slot);
    List<Schedule> findAllByDoctorAndWeekDateBetween(Doctor doctor, LocalDate start, LocalDate end);
}
