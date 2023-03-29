package com.mms.demo.transferobject;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
@Accessors(fluent = true)
public class ScheduleDTO {
    Long id;
    DoctorDTO doctor;
    LocalDateTime start;
    LocalDateTime end;
    Boolean approvalStatus;
    Boolean booked;
}
