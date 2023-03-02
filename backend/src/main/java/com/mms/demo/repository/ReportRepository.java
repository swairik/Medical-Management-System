package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDateTime;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByPatient(Patient patient);
    List<Report> findAllByStamp(LocalDateTime stamp);
    List<Report> findAllByDoctor(Doctor doctor);
}
