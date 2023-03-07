package com.mms.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.ReportRequest;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ReportService;

import jakarta.validation.Valid;

import com.mms.demo.model.ReportResponse;

@RestController("/report")
class ReportController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    ReportService reportService;

    @GetMapping("/display/{id}")
    public ResponseEntity<ReportResponse> showReportById(@PathVariable Long id) {
        Report report = reportService.getReportById(id)
                .orElseThrow(() -> new CustomException("Report with given id not found", "REPORT_NOT_FOUND"));
        ReportResponse response = createResponseFromReport(report);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/patient/{pid}")
    public ResponseEntity<List<ReportResponse>> showReportByPid(@PathVariable Long pid) {
        Patient patient = patientService.getPatientById(pid)
                .orElseThrow(() -> new CustomException("Patient with given id not found", "PATIENT_NOT_FOUND"));
        List<Report> reports = reportService.getReportsByPatient(patient);
        List<ReportResponse> response = reports.stream().map((r) -> createResponseFromReport(r))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<List<ReportResponse>> showReportByDid(@PathVariable Long did) {
        Doctor doctor = doctorService.getDoctortById(did)
                .orElseThrow(() -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND"));
        List<Report> reports = reportService.getReportByDoctor(doctor);
        List<ReportResponse> response = reports.stream().map((r) -> createResponseFromReport(r))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/stamp")
    public ResponseEntity<List<ReportResponse>> showReportById(@RequestBody LocalDateTime stamp) {
        List<Report> reports = reportService.getReportByStamp(stamp);
        List<ReportResponse> response = reports.stream().map((r) -> createResponseFromReport(r))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ReportResponse> createReport(@Valid @RequestBody ReportRequest reportRequest) {
        Report report = createReportFromRequest(reportRequest);
        Report createdReport = reportService.createReport(report);
        ReportResponse reportResponse = createResponseFromReport(createdReport);
        return new ResponseEntity<>(reportResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportResponse> updateReport(@PathVariable Long id,
            @Valid @RequestBody ReportRequest reportRequest) {
        Report report = createReportFromRequest(reportRequest);
        Report updatedReport = reportService.updateReport(id, report);
        ReportResponse reportResponse = createResponseFromReport(updatedReport);
        return new ResponseEntity<>(reportResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Report createReportFromRequest(ReportRequest reportRequest) {
        Patient patient = patientService.getPatientById(reportRequest.getPatientId())
                .orElseThrow(() -> new CustomException("Patient with given id not found", "PATIENT_NOT_FOUND"));
        Doctor doctor = doctorService.getDoctortById(reportRequest.getDoctorId())
                .orElseThrow(() -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND"));
        LocalDateTime currentDateTime = LocalDateTime.now();
        Report report = Report.builder()
                .patient(patient)
                .doctor(doctor)
                .stamp(currentDateTime)
                .reportText(reportRequest.getReportText())
                .build();
        return report;
    }

    public ReportResponse createResponseFromReport(Report report) {
        ReportResponse reportResponse = new ReportResponse();
        BeanUtils.copyProperties(report, reportResponse);
        return reportResponse;
    }

}
