package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Prescription;

import java.util.List;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findAllByStamp(LocalDateTime stamp);
    List<Prescription> findAllByStampBetween(LocalDateTime start, LocalDateTime end);
    List<Prescription> findAllByDoctor(Doctor doctor);
    List<Prescription> findAllByPatient(Patient patient);
}
