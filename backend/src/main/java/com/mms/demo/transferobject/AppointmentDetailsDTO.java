package com.mms.demo.transferobject;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder(toBuilder = true)
public class AppointmentDetailsDTO {
    Long id;
    String prescription;
    String feedback;
    Double rating;
}
