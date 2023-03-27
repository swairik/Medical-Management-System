package com.mms.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Report;

/**
 * ReportRepository defines an interface to generate JPA defined queries to interact with the Report
 * table.
 * 
 * @author Mananveer Singh
 */
public interface ReportRepository extends JpaRepository<Report, Long> {
    /**
     * Finds all reports created on a given day.
     *
     * @param stamp the timestamp. Only the date is relevant.
     * @return the list of Reports issued on the same day.
     */
    List<Report> findAllByStamp(LocalDateTime stamp);

    /**
     * Find all reports issued between a given range of days.
     * 
     * @param start the start of the range.
     * @param end the end of the range. The report generated on the date specified by {@code end} is
     *        also included.
     * @return the list of reports issued in this range of days.
     */
    List<Report> findAllByStampBetween(LocalDateTime start, LocalDateTime end);
}
