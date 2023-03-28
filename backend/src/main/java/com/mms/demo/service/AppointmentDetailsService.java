package com.mms.demo.service;

import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.AppointmentDetails;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDetailsService {

    Optional<AppointmentDetails> getById(Long id);

    @Deprecated
    List<AppointmentDetails> getAllByPatient(Patient patient);

    @Deprecated
    List<AppointmentDetails> getAllByPatientAndDoctor(Patient patient, Doctor doctor);

    @Deprecated
    Optional<AppointmentDetails> getByPatientAndStamp(Patient patient, LocalDateTime stamp);

    @Deprecated
    Optional<AppointmentDetails> create(AppointmentDetails appointmentDetails);


    Optional<AppointmentDetails> update(Long id, AppointmentDetails updates);
}
