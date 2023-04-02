package com.mms.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.exception.CustomException;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ReportService;
import com.mms.demo.transferobject.ReportDTO;

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

    @GetMapping("/display/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        ReportDTO report = reportService.get(id).orElseThrow(() -> new CustomException("Error while fetching report",
                "REPORT_NOT_FOUND", HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/display/day")
    public ResponseEntity<ReportDTO> getReportByDay(@RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter).truncatedTo(ChronoUnit.SECONDS);

        } catch (Exception e) {
            throw new CustomException("Wrong format of date & time", "WRONG_FROMAT", HttpStatus.BAD_REQUEST);
        }

        ReportDTO report = reportService.getByDay(dateTime)
                .orElseThrow(() -> new CustomException("Error while fetching report",
                        "REPORT_NOT_FOUND", HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/display/between")
    public ResponseEntity<ReportDTO> generateReportBetween(@PathVariable Long did,
            @RequestParam String from, @RequestParam String to) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime start, end;
        try {
            start = LocalDateTime.parse(from, formatter);
            end = LocalDateTime.parse(to, formatter);

        } catch (Exception e) {
            throw new CustomException("Wrong format of date & time", "WRONG_FROMAT", HttpStatus.BAD_REQUEST);
        }
        ReportDTO report = reportService.getAllByDayBetween(start, end)
                .orElseThrow(() -> new CustomException("Error while fetching report",
                        "REPORT_NOT_FOUND", HttpStatus.INTERNAL_SERVER_ERROR));
        
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<ReportDTO> generateDoctorReportBetween(@PathVariable Long did,
            @RequestParam String from, @RequestParam String to) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime start, end;
        try {
            start = LocalDateTime.parse(from, formatter);
            end = LocalDateTime.parse(to, formatter);

        } catch (Exception e) {
            throw new CustomException("Wrong format of date & time", "WRONG_FROMAT", HttpStatus.BAD_REQUEST);
        }

        ReportDTO report = reportService.generateForDoctor(did, start, end)
                .orElseThrow(() -> new CustomException("Error while fetching report",
                        "REPORT_NOT_FOUND", HttpStatus.INTERNAL_SERVER_ERROR));

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

}
