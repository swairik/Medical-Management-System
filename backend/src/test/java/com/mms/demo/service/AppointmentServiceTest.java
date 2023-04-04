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
public class AppointmentServiceTest {
    @Test
    void testCreate() {

    }

    @Test
    void testDelete() {

    }

    @Test
    void testGet() {

    }

    @Test
    void testGetAll() {

    }

    @Test
    void testGetAllByDoctor() {

    }

    @Test
    void testGetAllByDoctorAfter() {

    }

    @Test
    void testGetAllByDoctorBetween() {

    }

    @Test
    void testGetAllByPatient() {

    }

    @Test
    void testGetAllByPatientAfter() {

    }

    @Test
    void testGetAllByPatientAndDoctor() {

    }

    @Test
    void testGetAllByPatientAndDoctorAfter() {

    }

    @Test
    void testGetAllByPatientAndDoctorBetween() {

    }

    @Test
    void testGetAllByPatientBetween() {

    }

    @Test
    void testMarkAsAttended() {

    }

    @Test
    void testUpdateSchedule() {

    }
}
