package com.mms.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.exception.CustomException;
import com.mms.demo.service.AnalyticsService;
import com.mms.demo.transferobject.AdminAnalyticsDTO;
import com.mms.demo.transferobject.DoctorAnalyticsDTO;

@CrossOrigin("*")
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    AnalyticsService analyticsService;

    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<DoctorAnalyticsDTO> showDoctorAnalytics(@PathVariable Long did) {
        DoctorAnalyticsDTO doctorAnalyticsDTO = analyticsService.getForDoctor(did)
                .orElseThrow(() -> new CustomException("Error while generating analytics", "ANALYTICS_NOT_GENERATED",
                        HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(doctorAnalyticsDTO, HttpStatus.OK);
    }

    @GetMapping("/display/admin")
    public ResponseEntity<AdminAnalyticsDTO> showAdminAnalytics() {
        AdminAnalyticsDTO adminAnalyticsDTO = analyticsService.getForAdmin();
        return new ResponseEntity<>(adminAnalyticsDTO, HttpStatus.OK);
    }

}
