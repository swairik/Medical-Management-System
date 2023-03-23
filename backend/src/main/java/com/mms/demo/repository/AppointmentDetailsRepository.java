package com.mms.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;

public interface AppointmentDetailsRepository extends JpaRepository<AppointmentDetails, Long> {
    List<AppointmentDetails> findAllByPatient(Patient patient);

    List<AppointmentDetails> findAllByPatientAndDoctor(Patient patient, Doctor doctor);

    Optional<AppointmentDetails> findByPatientAndStamp(Patient patient, LocalDateTime stamp);

}
