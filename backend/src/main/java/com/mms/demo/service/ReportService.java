package com.mms.demo.service;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;

public interface ReportService {
    Optional<Report> getReportById(Long id);
    List<Report> getReportByStamp(LocalDateTime stamp);
    List<Report> getAllReportsByStampBetween(LocalDateTime start, LocalDateTime end);

    Optional<byte[]> generateReports(LocalDateTime from, LocalDateTime to);
    Report createReport(Report report);
    void deleteReport(Long id);
    Report updateReport(Long id, Report update);

}
