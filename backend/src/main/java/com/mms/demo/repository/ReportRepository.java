package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByStamp(LocalDate stamp);
    List<Report> findAllByStampBetween(LocalDate start, LocalDate end);
}
