package com.mms.demo.service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Report;

public interface ReportService {
    Optional<Report> getReportById(Long id);
    List<Report> getReportByStamp(LocalDateTime stamp);
    List<Report> getAllReportsByStampBetween(LocalDateTime start, LocalDateTime end);

    void forceRunReportGenerator(LocalDateTime when);

    Optional<byte[]> generateReports(LocalDateTime from, LocalDateTime to);
    Optional<byte[]> generateScheduleReportForDoctor(LocalDateTime from, LocalDateTime to, Doctor doctor);
    Report createReport(Report report);
    void deleteReport(Long id);
    Report updateReport(Long id, Report update);

    void reportGenerationScheduler();
}
