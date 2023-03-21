package com.mms.demo.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;
import com.mms.demo.repository.ScheduleRepository;
import com.mms.demo.service.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository repository;

    @Override
    public Schedule createSchedule(Schedule schedule) {
        return repository.save(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return repository.findAll();
    }

    @Override
    public Optional<Schedule> getScheduleById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Schedule> getSchedulesByDoctor(Doctor doctor) {
        return repository.findAllByDoctor(doctor);
    }


    @Override
    public List<Schedule> getSchedulesBySlot(Slot slot) {
        return repository.findAllBySlot(slot);
    }

    @Override
    public List<Schedule> getSchedulesByDoctorAndWeekDay(Doctor doctor, LocalDate start, LocalDate end) {
        return repository.findAllByDoctorAndWeekDateBetween(doctor, start, end);
    }

    @Override
    public Schedule updateSchedule(Long id, Schedule scheduleUpdates) {
        Optional<Schedule> temp = getScheduleById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Schedule schedule = temp.get();
        schedule.setDoctor(scheduleUpdates.getDoctor());
        schedule.setSlot(scheduleUpdates.getSlot());
        schedule.setWeekDate(scheduleUpdates.getWeekDate());

        return repository.save(schedule);
    }

    
    
}
