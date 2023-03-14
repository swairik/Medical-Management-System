package com.mms.demo.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mms.demo.entity.Report;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private Long id;
    private PatientResponse patientResponse;
    private DoctorResponse doctorResponse;
    private LocalDateTime stamp;
    private byte[] reportText;

    public static ReportResponse createResponseFromReport(Report report) {
        PatientResponse patientResponse = PatientResponse.createResponseFromPatient(report.getPatient());

        DoctorResponse doctorResponse = DoctorResponse.createResponseFromDoctor(report.getDoctor());

        ReportResponse reportResponse = ReportResponse.builder()
                .id(report.getId())
                .patientResponse(patientResponse)
                .doctorResponse(doctorResponse)
                .stamp(report.getStamp())
                .reportText(report.getReportText())
                .build();

        return reportResponse;
    }

}
