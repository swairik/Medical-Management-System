package com.mms.demo.transferobject;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class DoctorAnalyticsDTO {
    Integer upcomingAppointments;
    Integer patientCount;
    Integer unfilledPrescriptions;
    Integer fulfilledAppointmentsThisWeek;
}
