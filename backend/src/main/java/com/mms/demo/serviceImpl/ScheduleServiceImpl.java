package com.mms.demo.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.repository.DoctorRepository;
import com.mms.demo.repository.ScheduleRepository;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.transferobject.ScheduleDTO;
import jakarta.transaction.Transactional;

@Service
public class ScheduleServiceImpl implements ScheduleService {


    @Override
    public List<ScheduleDTO> getAllAfter(LocalDateTime stamp) {
        return repository.findAllByStartGreaterThanEqual(stamp.truncatedTo(ChronoUnit.MINUTES))
                        .stream().map(s -> mapper.entityToDto(s)).collect(Collectors.toList());
    }

    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DataTransferObjectMapper<Schedule, ScheduleDTO> mapper;

    @Override
    public List<ScheduleDTO> create(Long doctorID, LocalDateTime start,
                    Optional<LocalDateTime> endContainer) throws IllegalArgumentException {
        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);

        if (start.isAfter(LocalDateTime.now().plusHours(2)) == false) {
            throw new IllegalArgumentException("Slots should be created minimum 2 hours from now");
        }

        start = start.truncatedTo(ChronoUnit.MINUTES);

        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced doctor does not exist");
        }
        Doctor doctor = fetchedContainer.get();



        LocalDateTime end = null;
        if (endContainer.isPresent()) {
            end = endContainer.get().truncatedTo(ChronoUnit.MINUTES);
            if (start.isAfter(end) || start.isEqual(end)) {
                throw new IllegalArgumentException(
                                "End of slot cannot be before or equal to start");
            }
        } else {
            end = start.plusMinutes(30);
        }



        List<Schedule> schedules = new ArrayList<>();
        for (LocalDateTime time = start; !time.plusMinutes(30).isAfter(end); time =
                        time.plusMinutes(30)) {
            List<Schedule> candidates = repository.findAllByDoctorAndStartBetween(doctor,
                            time.minusMinutes(29), time.plusMinutes(29));


            if (!candidates.isEmpty()) {
                throw new IllegalArgumentException(
                                "Slots cannot be within 30 minutes of each other");
            }

            Schedule schedule = Schedule.builder().doctor(doctor).start(time)
                            .end(time.plusMinutes(30)).build();
            schedules.add(schedule);

            if (repository.findByDoctorAndStart(doctor, time).isPresent()) {
                throw new IllegalArgumentException(
                                "A previous schedule with the specified fields already exists: Doctor ("
                                                + doctor.getName() + "), Time (" + time.toString()
                                                + ")");
            }
        }
        schedules = repository.saveAll(schedules);

        return schedules.stream().map(s -> mapper.entityToDto(s)).collect(Collectors.toList());

    }

    @Override
    public void deleteSchedule(Long id) throws IllegalArgumentException {
        Optional<Schedule> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return;
        }
        if(fetchedContainer.get().getBooked() == true) {
            throw new IllegalArgumentException("Booked schedules cannot be deleted");
        }
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
    public List<ScheduleDTO> getByDoctorAfter(Long doctorID, LocalDateTime stamp)
                    throws IllegalArgumentException {
        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced doctor does not exist");
        }
        Doctor doctor = fetchedContainer.get();

        return repository.findAllByDoctorAndStartGreaterThanEqual(doctor, stamp).stream()
                        .map(s -> mapper.entityToDto(s)).collect(Collectors.toList());
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
    public List<ScheduleDTO> getApprovedByDoctor(Long doctorID, Boolean approval,
                    Optional<LocalDateTime> after) {
        List<ScheduleDTO> qualifiers = getByDoctor(doctorID);

        if (after.isPresent()) {
            qualifiers = qualifiers.stream()
                            .filter(s -> !s.getStart()
                                            .isBefore(after.get().truncatedTo(ChronoUnit.MINUTES)))
                            .collect(Collectors.toList());
        }

        return qualifiers.stream().filter(s -> Objects.equals(s.getApprovalStatus(), approval))
                        .collect(Collectors.toList());

    }

    @Override
    public List<ScheduleDTO> getBookedAndApprovedByDoctor(Long doctorID, Boolean approval,
                    Boolean booked, Optional<LocalDateTime> after) {
        return getApprovedByDoctor(doctorID, approval, after).stream()
                        .filter(s -> Objects.equals(s.getBooked(), booked))
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

    @Override
    @Scheduled(cron = "${schedule.clean.interval}")
    @Transactional
    @Async
    public void scheduleCleanerScheduler() {
        System.out.println("Performing schedule cleanup");
        LocalDateTime temporalTarget = LocalDate.now().minusDays(1).atTime(LocalTime.MAX);
        List<Schedule> schedules = repository.findAllByStartLessThan(temporalTarget);

        repository.deleteAll(schedules);

    }


}
