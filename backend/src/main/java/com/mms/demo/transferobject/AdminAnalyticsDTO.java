package com.mms.demo.transferobject;

import java.util.Map;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class AdminAnalyticsDTO {
    Map<SpecialityDTO, Integer> specialityDoctorCount;
    Map<SpecialityDTO, Integer> specialityPatientCount;
    Integer appointmentsToday;
    Integer appointmentsThisWeek;
    Integer appointmentsThisMonth;
    Integer activeDoctorsToday;
    Integer activeDoctorsThisWeek;
    Integer activeDoctorsThisMonth;


}
