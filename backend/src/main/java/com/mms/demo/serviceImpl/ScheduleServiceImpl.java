package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.repository.DoctorRepository;
import com.mms.demo.repository.ScheduleRepository;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.transferobject.ScheduleDTO;

@Service
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DataTransferObjectMapper<Schedule, ScheduleDTO> mapper;

    @Override
    public ScheduleDTO create(Long doctorID, LocalDateTime start) throws IllegalArgumentException {
        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced doctor does not exist");
        }
        Doctor doctor = fetchedContainer.get();

        Schedule schedule = Schedule.builder().doctor(doctor).start(start)
                        .end(start.plusMinutes(30)).build();
        schedule = repository.save(schedule);

        return mapper.entityToDto(schedule);

    }

    @Override
    public void deleteSchedule(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ScheduleDTO> getAll() {
        return repository.findAll().stream().map(s -> mapper.entityToDto(s))
                        .collect(Collectors.toList());
    }

    @Override
    public Optional<ScheduleDTO> get(Long id) {
        Optional<Schedule> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapper.entityToDto(fetchedContainer.get()));
    }

    @Override
    public List<ScheduleDTO> getByDoctor(Long doctorID) throws IllegalArgumentException {
        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced doctor does not exist");
        }
        Doctor doctor = fetchedContainer.get();

        return repository.findAllByDoctor(doctor).stream().map(s -> mapper.entityToDto(s))
                        .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getByDoctorBetween(Long doctorID, LocalDateTime start,
                    LocalDateTime end) throws IllegalArgumentException {

        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced doctor does not exist");
        }
        Doctor doctor = fetchedContainer.get();

        return repository.findAllByDoctorAndStartBetween(doctor,
                        start.truncatedTo(ChronoUnit.SECONDS), end.truncatedTo(ChronoUnit.SECONDS))
                        .stream().map(s -> mapper.entityToDto(s)).collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getAllUnapproved() {
        return repository.findAllByApprovalStatus(false).stream().map(s -> mapper.entityToDto(s))
                        .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getApprovedByDoctor(Long doctorID) {
        return getByDoctor(doctorID).stream().filter(s -> s.getApprovalStatus())
                        .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getBookedAndApprovedByDoctor(Long doctorID) {
        return getApprovedByDoctor(doctorID).stream().filter(s -> s.getBooked())
                        .collect(Collectors.toList());
    }

    @Override
    public Optional<ScheduleDTO> update(Long id, ScheduleDTO scheduleDTO)
                    throws IllegalArgumentException {
        Optional<Schedule> fetchedContainer = repository.findById(id);

        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced schedule does not exist");
        }

        Schedule scheduleUpdates;
        try {
            scheduleUpdates = mapper.dtoToEntity(scheduleDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to parse entity from data transfer object",
                            e);
        }
        Schedule schedule = fetchedContainer.get();
        schedule.setApprovalStatus(scheduleUpdates.getApprovalStatus());
        schedule.setStart(scheduleUpdates.getStart());
        schedule.setEnd(scheduleUpdates.getStart().plusMinutes(30));

        schedule = repository.save(schedule);

        return Optional.of(mapper.entityToDto(schedule));
    }

    @Override
    public void markAsApproved(Long id) throws IllegalArgumentException {
        Optional<Schedule> fetchedContainer = repository.findById(id);

        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced schedule does not exist");
        }

        Schedule schedule = fetchedContainer.get();
        schedule.setApprovalStatus(true);
        repository.save(schedule);

    }

}
