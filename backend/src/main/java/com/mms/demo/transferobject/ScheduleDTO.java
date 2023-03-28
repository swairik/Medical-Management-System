package com.mms.demo.transferobject;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@Builder
public class ScheduleDTO {
    private final @NonNull Long id;
    private final @NonNull DoctorDTO doctor;
    private final @NonNull LocalDateTime start;
    private final LocalDateTime end;
    private final @NonNull Boolean approvalStatus;
}
