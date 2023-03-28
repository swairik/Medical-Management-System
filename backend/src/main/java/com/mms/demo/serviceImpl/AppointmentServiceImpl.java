package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Schedule;
import com.mms.demo.mapper.AppointmentDetailsMapper;
import com.mms.demo.mapper.AppointmentMapper;
import com.mms.demo.mapper.ScheduleMapper;
import com.mms.demo.repository.AppointmentDetailsRepository;
import com.mms.demo.repository.AppointmentRepository;
import com.mms.demo.repository.DoctorRepository;
import com.mms.demo.repository.PatientRepository;
import com.mms.demo.repository.ScheduleRepository;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.transferobject.AppointmentDTO;
import com.mms.demo.transferobject.AppointmentDetailsDTO;


@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private AppointmentDetailsRepository appointmentDetailsRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentMapper mapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private AppointmentDetailsMapper appointmentDetailsMapper;

    @Override
    public AppointmentDTO create(Long patientID, Long scheduleID,
                    AppointmentDetailsDTO appointmentDetailsDTO) throws IllegalArgumentException {

        Optional<Patient> fetchedPatientContainer = patientRepository.findById(patientID);
        if (fetchedPatientContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced patient does not exist");
        }
        Patient patient = fetchedPatientContainer.get();

        Optional<Schedule> fetchedScheduleContainer = scheduleRepository.findById(scheduleID);
        if (fetchedScheduleContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced schedule does not exist");
        }
        Schedule schedule = fetchedScheduleContainer.get();


        AppointmentDetails appointmentDetails =
                        appointmentDetailsMapper.dtoToEntity(appointmentDetailsDTO);
        appointmentDetails = appointmentDetailsRepository.save(appointmentDetails);

        Appointment appointment = Appointment.builder().appointmentDetails(appointmentDetails)
                        .doctor(schedule.getDoctor()).patient(patient).start(schedule.getStart())
                        .build();
        appointment = repository.save(appointment);

        schedule.setBooked(true);
        scheduleRepository.save(schedule);

        return mapper.entityToDto(appointment);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);

    }

    @Override
    public List<AppointmentDTO> getAll() {
        return repository.findAll().stream().map(a -> mapper.entityToDto(a))
                        .collect(Collectors.toList());
    }

    @Override
    public Optional<AppointmentDTO> get(Long id) {
        Optional<Appointment> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapper.entityToDto(fetchedContainer.get()));
    }

    @Override
    public List<AppointmentDTO> getAllByPatientBetween(Long patientID, LocalDateTime start,
                    LocalDateTime end) {
        Optional<Patient> fetchedContainer = patientRepository.findById(patientID);
        if (fetchedContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Patient patient = fetchedContainer.get();
        List<Appointment> patientAppointments = repository.findAllByPatientAndStartBetween(patient,
                        start.truncatedTo(ChronoUnit.SECONDS), end.truncatedTo(ChronoUnit.SECONDS));

        return patientAppointments.stream().map(a -> mapper.entityToDto(a))
                        .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAllByPatientAfter(Long patientID, LocalDateTime stamp) {
        Optional<Patient> fetchedContainer = patientRepository.findById(patientID);
        if (fetchedContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Patient patient = fetchedContainer.get();

        List<Appointment> patientAppointments =
                        repository.findAllByPatientAndStartGreaterThanEqual(patient, stamp);

        return patientAppointments.stream().map(a -> mapper.entityToDto(a))
                        .collect(Collectors.toList());

    }

    @Override
    public List<AppointmentDTO> getAllByPatient(Long patientID) {
        Optional<Patient> fetchedContainer = patientRepository.findById(patientID);
        if (fetchedContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Patient patient = fetchedContainer.get();

        List<Appointment> patientAppointments = repository.findAllByPatient(patient);

        return patientAppointments.stream().map(a -> mapper.entityToDto(a))
                        .collect(Collectors.toList());
    }


    @Override
    public Optional<AppointmentDTO> updateSchedule(Long id, Long scheduleID)
                    throws IllegalArgumentException {
        Optional<Appointment> fetchedContainer = repository.findById(id);

        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        Optional<Schedule> fetchedScheduleContainer = scheduleRepository.findById(scheduleID);
        if (fetchedScheduleContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced schedule does not exist");
        }
        Schedule schedule = fetchedScheduleContainer.get();


        Appointment appointment = fetchedContainer.get();
        fetchedScheduleContainer = scheduleRepository.findByDoctorAndStart(appointment.getDoctor(),
                        appointment.getStart());
        if (fetchedScheduleContainer.isEmpty()) {
            throw new IllegalArgumentException("No schedule with this time stamp exists");
        }

        appointment.setStart(schedule.getStart());
        appointment = repository.save(appointment);

        schedule.setBooked(true);
        scheduleRepository.save(schedule);

        Schedule oldSchedule = fetchedScheduleContainer.get();
        oldSchedule.setBooked(false);
        scheduleRepository.save(oldSchedule);

        return Optional.of(mapper.entityToDto(appointment));
    }

    @Override
    public List<AppointmentDTO> getAllByDoctor(Long doctorID) {
        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);
        if (fetchedContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Doctor doctor = fetchedContainer.get();

        List<Appointment> appointments = repository.findAllByDoctor(doctor);

        return appointments.stream().map(a -> mapper.entityToDto(a)).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAllByDoctorAfter(Long doctorID, LocalDateTime after) {
        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);
        if (fetchedContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Doctor doctor = fetchedContainer.get();

        List<Appointment> appointments = repository.findAllByDoctorAndStartGreaterThanEqual(doctor,
                        after.truncatedTo(ChronoUnit.SECONDS));

        return appointments.stream().map(a -> mapper.entityToDto(a)).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAllByDoctorBetween(Long doctorID, LocalDateTime from,
                    LocalDateTime to) {
        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);
        if (fetchedContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Doctor doctor = fetchedContainer.get();

        List<Appointment> appointments = repository.findAllByDoctorAndStartBetween(doctor,
                        from.truncatedTo(ChronoUnit.SECONDS), to.truncatedTo(ChronoUnit.SECONDS));

        return appointments.stream().map(a -> mapper.entityToDto(a)).collect(Collectors.toList());
    }

    @Override
    public void markAsAttended(Long id) {
        Optional<Appointment> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return;
        }

        Appointment appointment = fetchedContainer.get();
        appointment.setAttended(true);
        repository.save(appointment);

    }

    @Override
    public List<AppointmentDTO> getAllByPatientAndDoctor(Long patientID, Long doctorID) {
        Optional<Patient> fetchedPatientContainer = patientRepository.findById(patientID);
        if (fetchedPatientContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Patient patient = fetchedPatientContainer.get();

        Optional<Doctor> fetchedDoctorContainer = doctorRepository.findById(doctorID);
        if (fetchedDoctorContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Doctor doctor = fetchedDoctorContainer.get();

        List<Appointment> appointments = repository.findAllByPatientAndDoctor(patient, doctor);

        return appointments.stream().map(a -> mapper.entityToDto(a)).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAllByPatientAndDoctorAfter(Long patientID, Long doctorID,
                    LocalDateTime after) {
        Optional<Patient> fetchedPatientContainer = patientRepository.findById(patientID);
        if (fetchedPatientContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Patient patient = fetchedPatientContainer.get();

        Optional<Doctor> fetchedDoctorContainer = doctorRepository.findById(doctorID);
        if (fetchedDoctorContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Doctor doctor = fetchedDoctorContainer.get();

        List<Appointment> appointments =
                        repository.findAllByPatientAndDoctorAndStartGreaterThanEqual(patient,
                                        doctor, after.truncatedTo(ChronoUnit.SECONDS));

        return appointments.stream().map(a -> mapper.entityToDto(a)).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAllByPatientAndDoctorBetween(Long patientID, Long doctorID,
                    LocalDateTime from, LocalDateTime to) {
        Optional<Patient> fetchedPatientContainer = patientRepository.findById(patientID);
        if (fetchedPatientContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Patient patient = fetchedPatientContainer.get();

        Optional<Doctor> fetchedDoctorContainer = doctorRepository.findById(doctorID);
        if (fetchedDoctorContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Doctor doctor = fetchedDoctorContainer.get();

        List<Appointment> appointments = repository.findAllByPatientAndDoctorAndStartBetween(
                        patient, doctor, from.truncatedTo(ChronoUnit.SECONDS),
                        to.truncatedTo(ChronoUnit.SECONDS));

        return appointments.stream().map(a -> mapper.entityToDto(a)).collect(Collectors.toList());
    }


}
