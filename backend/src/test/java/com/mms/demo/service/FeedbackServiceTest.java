package com.mms.demo.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Feedback;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class FeedbackServiceTest {
    @Autowired
    FeedbackService feedbackService;

    static Feedback feedback;

    @Test
    @Order(1)
    void testCreate() {
        feedback = Feedback.builder().contents("contents".getBytes()).build();
        assertThat(feedbackService.create(feedback)).isNotEmpty().contains(feedback);
    }

    @Test
    @Order(2)
    void testGetById() {
        assertThat(feedbackService.getById(feedback.getId())).contains(feedback);
    }

    @Test
    @Order(3)
    void testUpdate() {
        Feedback updates = feedback.toBuilder().contents("newcontents".getBytes()).build();
        assertThat(feedbackService.update(feedback.getId(), updates)).contains(updates);
    }

    @Test
    void testDelete() {
        feedbackService.delete(feedback.getId());
        assertThat(feedbackService.getById(feedback.getId())).isEmpty();
    }


}
