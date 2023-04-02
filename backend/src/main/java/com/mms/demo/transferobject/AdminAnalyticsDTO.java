package com.mms.demo.transferobject;

import java.util.Map;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class AdminAnalyticsDTO {
    Map<SpecialityDTO, Long> specialityDoctorCount;
    Map<SpecialityDTO, Long> specialityPatientCount;
    Long appointmentsToday;
    Long appointmentsThisWeek;
    Long appointmentsThisMonth;
    Long activeDoctorsToday;
    Long activeDoctorsThisWeek;
    Long activeDoctorsThisMonth;


}
