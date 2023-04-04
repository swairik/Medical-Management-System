package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.SpecialityDTO;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ScheduleServiceTest {
    @Test
    void testCreate() {

    }

    @Test
    void testDeleteSchedule() {

    }

    @Test
    void testGet() {

    }

    @Test
    void testGetAll() {

    }

    @Test
    void testGetAllUnapproved() {

    }

    @Test
    void testGetApprovedByDoctor() {

    }

    @Test
    void testGetBookedAndApprovedByDoctor() {

    }

    @Test
    void testGetByDoctor() {

    }

    @Test
    void testGetByDoctorAfter() {

    }

    @Test
    void testGetByDoctorBetween() {

    }

    @Test
    void testMarkAsApproved() {

    }

    @Test
    void testUpdate() {

    }
}
