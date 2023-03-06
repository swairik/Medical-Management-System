package com.mms.demo.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    @NotNull(message = "Patient Id cannot be null")
    private Long patientId;

    @NotNull(message = "Doctor Id cannot be null")
    private Long DoctorId;

    private byte[] reportText;
}
