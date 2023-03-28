package com.mms.demo.transferobject;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
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
    private final @NonNull ScheduleDTO schedule;
    private final @NonNull AppointmentDetailsDTO appointmentDetails;
}
