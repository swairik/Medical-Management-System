package com.mms.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Report;

/**
 * The Interface ReportService defines all the interactions needed between a high level controller
 * and the corresponding repository
 * 
 * @author Mananveer Singh
 */
public interface ReportService {
    /**
     * Gets the report by id.
     *
     * @param id the id
     * @return the report by id
     */
    Optional<Report> getReportById(Long id);


    /**
     * Gets all the reports by a shared time stamp.
     *
     * @param stamp the stamp
     * @return the list of reports by stamp
     */
    List<Report> getReportByStamp(LocalDateTime stamp);

    /**
     * Gets all the reports by stamp between a given range of time.
     *
     * @param start the start of the range
     * @param end the end of the range
     * @return the list of all reports in the time range
     */
    List<Report> getAllReportsByStampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Forcefully generates a report for a given day. If run for the current day, prematurely stores
     * all the qualifying data. Used frequently by the report generation scheduler.
     * 
     * @param when the timestamp used to find the day of report generation
     */
    void forceRunReportGenerator(LocalDateTime when);

    /**
     * Gets all existing reports in a given range of time and packages them inside a ZIP archive.
     * 
     * @param from start of the range
     * @param to end of the range
     * @return the resultant ZIP archive converted to a byte array
     */
    Optional<byte[]> generateReports(LocalDateTime from, LocalDateTime to);

    /**
     * Goes through all reports in a given range and combines the qualifying details for a doctor
     * into a single file.
     * 
     * @param from start of the range
     * @param to end of the range
     * @param doctor the doctor
     * @return the resultant ZIP archive converted to a byte array
     */
    Optional<byte[]> generateScheduleReportForDoctor(LocalDateTime from, LocalDateTime to,
                    Doctor doctor);

    /**
     * Creates the report. Not intended to be used through an endpoint at the Controller layer.
     *
     * @param report the report
     * @return the report
     */
    Report createReport(Report report);

    /**
     * Delete report. Not intended to be used through an endpoint at the Controller layer.
     *
     * @param id the id
     */
    void deleteReport(Long id);

    /**
     * Update report. Not intended to be used through an endpoint at the Controller layer.
     *
     * @param id the id
     * @param update the update
     * @return the report
     */
    Report updateReport(Long id, Report update);

    /**
     * Declares the scheduled job that periodically generates reports
     */
    void reportGenerationScheduler();
}
