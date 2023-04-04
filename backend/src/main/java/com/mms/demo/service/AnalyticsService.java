package com.mms.demo.service;

import java.util.Optional;
import com.mms.demo.transferobject.AdminAnalyticsDTO;
import com.mms.demo.transferobject.DoctorAnalyticsDTO;

public interface AnalyticsService {
    Optional<DoctorAnalyticsDTO> getForDoctor(Long doctorID);

    AdminAnalyticsDTO getForAdmin();
}
