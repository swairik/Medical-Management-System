package com.mms.demo.model;

import java.time.LocalDate;

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
    private LocalDate stamp;
    private byte[] reportText;

    public static ReportResponse createResponseFromReport(Report report) {

        ReportResponse reportResponse = ReportResponse.builder()
                .id(report.getId())
                .stamp(report.getStamp())
                .reportText(report.getReportText())
                .build();

        return reportResponse;
    }

}
