package com.mms.demo.service;

import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.AppointmentDetails;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDetailsService {
    Optional<AppointmentDetails> getById(Long id);

    List<AppointmentDetails> getAllByPatient(Patient patient);

    List<AppointmentDetails> getAllByDoctor(Doctor doctor);

    List<AppointmentDetails> getAllByPatientAndDoctor(Patient patient, Doctor doctor);

    Optional<AppointmentDetails> getByPatientAndStamp(Patient patient, LocalDateTime stamp);

    Optional<AppointmentDetails> create(AppointmentDetails appointmentDetails);

    Optional<AppointmentDetails> update(Long id, AppointmentDetails updates);

    void deleteById(Long id);
}
