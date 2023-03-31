package com.mms.demo.model;

import com.mms.demo.transferobject.AppointmentDetailsDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequest {

    @NotNull(message = "Patient cannot be null")
    private long patientId;

    @NotNull(message = "Schedule cannot be null")
    private long scheduleId;

    @Builder.Default
    private AppointmentDetailsDTO appointmentDetails;
}
