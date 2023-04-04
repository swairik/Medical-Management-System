package com.mms.demo.mapperimpl;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.AppointmentDTO;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.PatientDTO;
import com.mms.demo.transferobject.ReportDTO;
import com.mms.demo.transferobject.SpecialityDTO;
import static org.assertj.core.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ReportMapperImplTest {
    @Autowired
    private DataTransferObjectMapper<Report, ReportDTO> mapper;

    @Test
    void testEntityToDto() {
        Report report = Report.builder().contents("content".getBytes()).stamp(LocalDate.now()).build();

        assertThat(mapper.entityToDto(report)).isNotNull();
    }
}
