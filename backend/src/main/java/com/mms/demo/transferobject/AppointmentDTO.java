package com.mms.demo.transferobject;


import java.time.LocalDateTime;
import io.micrometer.common.lang.NonNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
@Accessors(fluent = true)
public final class AppointmentDTO {
    Long id;
    PatientDTO patient;
    DoctorDTO doctor;
    LocalDateTime start;
    AppointmentDetailsDTO appointmentDetails;
    Boolean attended;
}
