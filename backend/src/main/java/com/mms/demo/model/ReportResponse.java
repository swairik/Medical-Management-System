// package com.mms.demo.model;

// import java.time.LocalDateTime;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// import com.mms.demo.entity.Report;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private Long id;
    private LocalDateTime stamp;
    private byte[] contents;

    public static ReportResponse createResponseFromReport(Report report) {

        ReportResponse reportResponse = ReportResponse.builder()
                .id(report.getId())
                .stamp(report.getStamp())
                .contents(report.getContents())
                .build();

//         return reportResponse;
//     }

// }
