package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;
import com.mms.demo.repository.AppointmentRepository;
import com.mms.demo.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository repo;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        return repo.save(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        repo.deleteById(id);

    }

    @Override
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Appointment> getAllByPatientBetween(Patient patient, LocalDateTime start, LocalDateTime end) {
        List<Appointment> patientAppointments = repo.findAllByPatient(patient);
        end.plusMonths(12);
        return patientAppointments.stream()
                .filter(a -> a.getSlot().getStart().isAfter(start.truncatedTo(ChronoUnit.SECONDS).toLocalTime()) &&
                        a.getSlot().getEnd().isBefore(end.truncatedTo(ChronoUnit.SECONDS).toLocalTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getAllByPatientAfter(Patient patient, LocalDateTime stamp) {
        return getAllByPatientBetween(patient, stamp, LocalDateTime.now());
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return repo.findAllByPatient(patient);
    }

    @Override
    public List<Appointment> getAppointmentsBySlot(Slot slot) {
        return repo.findAllBySlot(slot);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointmentUpdates) {
        Optional<Appointment> temp = getAppointmentById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Appointment appointment = temp.get();
        appointment.setPatient(appointmentUpdates.getPatient());
        appointment.setSlot(appointmentUpdates.getSlot());

        return repo.save(appointment);
    }

}
