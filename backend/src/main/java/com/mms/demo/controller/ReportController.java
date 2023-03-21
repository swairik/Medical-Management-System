package com.mms.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Doctor;
import com.mms.demo.exception.CustomException;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ReportService;

@CrossOrigin("*")
@RestController
@RequestMapping("/report")
class ReportController {

        @Autowired
        DoctorService doctorService;

        @Autowired
        PatientService patientService;

        @Autowired
        ReportService reportService;

        @GetMapping("/display/generateReport")
        public ResponseEntity<Resource> generateReport(@RequestParam String from, @RequestParam String to) {
                // reportService.forceRunReportGenerator(LocalDateTime.now());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime start, end;
                try {
                        start = LocalDateTime.parse(from, formatter);
                        end = LocalDateTime.parse(to, formatter);

                } catch (Exception e) {
                        throw new CustomException("Wrong format of date & time", "WRONG_FROMAT");
                }
                byte[] reports = reportService.generateReports(start, end).orElseThrow(
                                () -> new CustomException("Error while generating report", "REPORT_NOT_AVAILABLE_FOR_TIME_PERIOD"));
                ByteArrayResource response = new ByteArrayResource(reports);
                String filename = String.format("Report_%s_%s.zip", start.toString(), end.toString());
                return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .contentLength(response.contentLength())
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                ContentDisposition.attachment()
                                                                .filename(filename)
                                                                .build().toString())
                                .body(response);
        }

        @GetMapping("/display/generateDoctorReport/{id}")
        public ResponseEntity<Resource> generateDoctorReport(@PathVariable Long id, @RequestParam String from,
                        @RequestParam String to) {
                Doctor doctor = doctorService.getDoctortById(id).orElseThrow(
                                () -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime start, end;
                try {
                        start = LocalDateTime.parse(from, formatter);
                        end = LocalDateTime.parse(to, formatter);

                } catch (Exception e) {
                        throw new CustomException("Wrong format of date & time", "WRONG_FROMAT");
                }

                byte[] reports = reportService.generateScheduleReportForDoctor(start, end, doctor).orElseThrow(
                                () -> new CustomException("Error while generating report",
                                                "REPORT_NOT_AVAILABLE_FOR_TIME_PERIOD"));
                ByteArrayResource response = new ByteArrayResource(reports);
                String filename = String.format("Report_Doctor_%s_%s.zip", start.toString(), end.toString());
                return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .contentLength(response.contentLength())
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                ContentDisposition.attachment()
                                                                .filename(filename)
                                                                .build().toString())
                                .body(response);
        }

}
