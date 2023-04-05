package com.mms.demo.transferobject;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class DoctorAnalyticsDTO {
    Long upcomingAppointments;
    Long patientCount;
    Long unfilledPrescriptions;
    Long fulfilledAppointmentsThisWeek;

    Long totalSlotsToday;
    Long totalSlotsBookedToday;
    Long totalSlotsFreeToday;
    Double rating;
}
