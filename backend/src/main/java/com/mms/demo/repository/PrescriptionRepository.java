package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Prescription;

import java.util.List;
import java.time.LocalDate;


public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findAllByStamp(LocalDate stamp);
    List<Prescription> findAllByStampBetween(LocalDate start, LocalDate end);
    List<Prescription> findAllByDoctor(Doctor doctor);
    List<Prescription> findAllByPatient(Patient patient);
}
