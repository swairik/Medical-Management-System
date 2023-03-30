package com.mms.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Report;
import com.mms.demo.transferobject.ReportDTO;
import java.io.IOException;

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
    Optional<ReportDTO> get(Long id);


    /**
     * Gets all the reports by a shared time stamp.
     *
     * @param stamp the stamp
     * @return the list of reports by stamp
     */
    Optional<ReportDTO> getByDay(LocalDateTime stamp);

    /**
     * Gets all existing reports in a given range of time and packages them inside a ZIP archive.
     * 
     * @param from start of the range
     * @param to end of the range
     * @return the resultant ZIP archive converted to a byte array
     */
    Optional<ReportDTO> getAllByDayBetween(LocalDateTime start, LocalDateTime end)
                    throws IOException;

    /**
     * Forcefully generates a report for a given day. If run for the current day, prematurely stores
     * all the qualifying data. Used frequently by the report generation scheduler.
     * 
     * @param forDay the timestamp used to find the day of report generation
     */
    void forceRunReportGenerator(LocalDateTime forDay) throws IOException;


    Optional<ReportDTO> generateForDoctor(Long doctorID, LocalDateTime from, LocalDateTime to)
                    throws IllegalArgumentException, IOException;



    /**
     * Declares the scheduled job that periodically generates reports
     */
    void reportGenerationScheduler();
}
