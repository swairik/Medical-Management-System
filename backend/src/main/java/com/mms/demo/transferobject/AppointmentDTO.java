package com.mms.demo.transferobject;


import java.time.LocalDateTime;
import io.micrometer.common.lang.NonNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@Builder
public final class AppointmentDTO {
    private final @NonNull Long id;
    private final @NonNull PatientDTO patient;
    private final @NonNull DoctorDTO doctor;
    private final @NonNull LocalDateTime start;
    private final @NonNull AppointmentDetailsDTO appointmentDetails;
}
