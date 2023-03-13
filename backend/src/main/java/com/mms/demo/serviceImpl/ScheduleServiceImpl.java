package com.mms.demo.serviceImpl;

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
    private ScheduleRepository repo;

    @Override
    public Schedule createSchedule(Schedule schedule) {
        return repo.save(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return repo.findAll();
    }

    @Override
    public Optional<Schedule> getScheduleById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Schedule> getSchedulesByDoctor(Doctor doctor) {
        return repo.findAllByDoctor(doctor);
    }

    @Override
    public List<Schedule> getSchedulesBySlot(Slot slot) {
        return repo.findAllBySlot(slot);
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
        schedule.setWeek(scheduleUpdates.getWeek());
        schedule.setYear(scheduleUpdates.getYear());

        return repo.save(schedule);
    }

}
