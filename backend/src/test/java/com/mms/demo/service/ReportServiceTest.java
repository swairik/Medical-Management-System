package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;
import com.mms.demo.entity.Speciality;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ReportServiceTest {
    @Autowired
    ReportService impl;

    static Report report;

    @Test
    @Order(1)
    @DisplayName("Testing create on a single report")
    void testCreateReport() {
        report = Report.builder().reportText(null).stamp(LocalDateTime.now().toLocalDate()).build();
        assertThat(impl.createReport(report)).isEqualTo(report);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single report by id")
    void testGetReportById() {
        assertThat(impl.getReportById(report.getId())).isNotEmpty().contains(report);
    }

    @Order(4)
    @Test
    @DisplayName("(UNIMPLEMENTED) Testing fetch on a single report by stamp")
    void testGetReportByStamp() {
        ArrayList<Report> reports = new ArrayList<Report>();
        reports.add(Report.builder().reportText(null).build());
        reports.add(Report.builder().reportText(null).build());
        reports.add(Report.builder().reportText(null).build());
        reports.add(Report.builder().reportText(null).build());

        for (Report rep : reports) {
            impl.createReport(rep);
        }

        assertThat(impl.getReportByStamp(LocalDateTime.now())).containsAll(reports);
    }

    @Order(5)
    @Test
    void testGetAllReportsByStampBetween() {
        ArrayList<Report> reports = new ArrayList<Report>();
        reports.add(Report.builder().reportText(null).build());
        reports.add(Report.builder().reportText(null).build());
        reports.add(Report.builder().reportText(null).build());
        for (Report rep : reports) {
            impl.createReport(rep);
        }
        Report temp = Report.builder().reportText(null).stamp(LocalDate.now().plusDays(1)).build();
        impl.createReport(temp);

        assertThat(impl.getAllReportsByStampBetween(LocalDateTime.now(), LocalDateTime.now())).containsAll(reports).doesNotContain(temp);
    }

    @Order(6)
    @Test
    @DisplayName("Testing update on a single report by id")
    void testUpdateReport() {
        Report tempReport = report.toBuilder().build();
        assertThat(impl.updateReport(report.getId(), tempReport)).isEqualTo(tempReport);
    }

    @Order(7)
    @Test
    @DisplayName("Testing delete on a single report by id")
    void testDeleteReport() {
        impl.deleteReport(report.getId());

        assertThat(impl.getReportById(report.getId())).isEmpty();
    }
}
