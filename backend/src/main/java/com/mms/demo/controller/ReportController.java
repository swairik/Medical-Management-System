package com.mms.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.ReportRequest;
import com.mms.demo.model.ReportResponse;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ReportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/report")
class ReportController {

        @Autowired
        DoctorService doctorService;

        @Autowired
        PatientService patientService;

        @Autowired
        ReportService reportService;

        // @GetMapping("/display/{id}")
        // public ResponseEntity<ReportResponse> showReportById(@PathVariable Long id) {
        // Report report = reportService.getReportById(id)
        // .orElseThrow(() -> new CustomException("Report with given id not found",
        // "REPORT_NOT_FOUND"));
        // ReportResponse response = ReportResponse.createResponseFromReport(report);
        // return new ResponseEntity<>(response, HttpStatus.OK);
        // }

        // @GetMapping("/display/stamp")
        // public ResponseEntity<List<ReportResponse>> showReportById(@RequestParam
        // String stamp) {
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd
        // HH:mm:ss");
        // LocalDateTime dateTime = LocalDateTime.parse(stamp, formatter);
        // List<Report> reports = reportService.getReportByStamp(dateTime);
        // List<ReportResponse> response = reports.stream().map((r) ->
        // ReportResponse.createResponseFromReport(r))
        // .collect(Collectors.toList());
        // return new ResponseEntity<>(response, HttpStatus.OK);
        // }

        // @GetMapping("/display/stampBetween")
        // public ResponseEntity<List<ReportResponse>>
        // showReportByStampsBetween(@RequestParam String startTime,
        // @RequestParam String endTime) {
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd
        // HH:mm:ss");
        // LocalDateTime start, end;
        // try {
        // start = LocalDateTime.parse(startTime, formatter);
        // end = LocalDateTime.parse(endTime, formatter);

        // } catch (Exception e) {
        // throw new CustomException("Wrong format of date & time", "WRONG_FROMAT");
        // }
        // List<Report> reports = reportService.getAllReportsByStampBetween(start, end);
        // List<ReportResponse> response = reports.stream().map((r) ->
        // ReportResponse.createResponseFromReport(r))
        // .collect(Collectors.toList());
        // // HttpHeaders headers = new HttpHeaders();
        // // headers.setContentType(null);
        // return new ResponseEntity<>(response, HttpStatus.OK);
        // }

        @GetMapping("/display/generateReport")
        public ResponseEntity<Resource> generateReport(@RequestParam String from, @RequestParam String to) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime start, end;
                try {
                        start = LocalDateTime.parse(from, formatter);
                        end = LocalDateTime.parse(to, formatter);

                } catch (Exception e) {
                        throw new CustomException("Wrong format of date & time", "WRONG_FROMAT");
                }
                byte[] reports = reportService.generateReports(start, end).orElseThrow(
                                () -> new CustomException("Error while generating report", "REPORT_NOT_GENERATED"));
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
                                () -> new CustomException("Error while generating report", "REPORT_NOT_GENERATED"));
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

        // @PostMapping("/")
        // public ResponseEntity<ReportResponse> createReport(@Valid @RequestBody
        // ReportRequest reportRequest) {
        // Report report = createReportFromRequest(reportRequest);
        // Report createdReport = reportService.createReport(report);
        // ReportResponse reportResponse =
        // ReportResponse.createResponseFromReport(createdReport);
        // return new ResponseEntity<>(reportResponse, HttpStatus.CREATED);
        // }

        // @PutMapping("/{id}")
        // public ResponseEntity<ReportResponse> updateReport(@PathVariable Long id,
        // @Valid @RequestBody ReportRequest reportRequest) {
        // Report report = createReportFromRequest(reportRequest);
        // Report updatedReport = reportService.updateReport(id, report);
        // ReportResponse reportResponse =
        // ReportResponse.createResponseFromReport(updatedReport);
        // return new ResponseEntity<>(reportResponse, HttpStatus.OK);
        // }

        // @DeleteMapping("/{id}")
        // public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        // reportService.deleteReport(id);
        // return new ResponseEntity<>(HttpStatus.OK);
        // }

        public Report createReportFromRequest(ReportRequest reportRequest) {
                // Patient patient = patientService.getPatientById(reportRequest.getPatientId())
                // .orElseThrow(() -> new CustomException("Patient with given id not found",
                // "PATIENT_NOT_FOUND"));
                // Doctor doctor = doctorService.getDoctortById(reportRequest.getDoctorId())
                // .orElseThrow(() -> new CustomException("Doctor with given id not found",
                // "DOCTOR_NOT_FOUND"));
                LocalDateTime currentDateTime = LocalDateTime.now();
                Report report = Report.builder()
                                // .patient(patient)
                                // .doctor(doctor)
                                // .stamp(currentDateTime)
                                .contents(reportRequest.getContents())
                                .build();
                return report;
        }

}
