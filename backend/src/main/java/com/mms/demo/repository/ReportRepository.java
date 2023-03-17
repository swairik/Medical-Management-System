package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.mms.demo.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByStamp(LocalDateTime stamp);
    List<Report> findAllByStampBetween(LocalDateTime start, LocalDateTime end);
}
