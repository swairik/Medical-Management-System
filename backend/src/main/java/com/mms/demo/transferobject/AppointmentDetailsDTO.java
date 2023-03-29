package com.mms.demo.transferobject;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class AppointmentDetailsDTO {
    private final Long id;
    private final @NonNull String prescription;
    private final @NonNull String feedback;
}
