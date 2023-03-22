package com.mms.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;

public interface AppointmentService {
    Optional<Appointment> getAppointmentById(Long id);

    List<Appointment> getAllAppointments();

    List<Appointment> getAppointmentsByPatient(Patient patient);

    List<Appointment> getAppointmentsBySlot(Slot slot);

    List<Appointment> getAllByPatientBetween(Patient patient, LocalDateTime start,
                    LocalDateTime end);

    List<Appointment> getAllByPatientAfter(Patient patient, LocalDateTime stamp);

    Appointment createAppointment(Appointment appointment);

    Appointment updateAppointment(Long id, Appointment appointmentUpdates);

    void deleteAppointment(Long id);
}
