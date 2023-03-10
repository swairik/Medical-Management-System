package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;
import com.mms.demo.entity.Speciality;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ReportServiceTest {
    @Autowired
    ReportService impl;
    
    @Autowired
    PatientService patientImpl;

    @Autowired
    DoctorService doctorImpl;

    @Autowired
    SpecialityService specImpl;

    static Speciality spec;
    static Patient patient;
    static Doctor doctor;
    static Report report;

    @Test
    @Order(1)
    @DisplayName("Testing create on a single report")
    void testCreateReport() {
        patient = Patient.builder().age(25).email("temp@temp.com").gender("M").name("Jerry").phone("XYZ").build();
        patientImpl.createPatient(patient);
        assertThat(patientImpl.getPatientById(patient.getId())).isNotEmpty().contains(patient);
        
        spec = Speciality.builder().name("Dentist").build();
        specImpl.createSpeciality(spec);
        assertThat(specImpl.getSpecialityById(spec.getId())).isNotEmpty().contains(spec);

        doctor = Doctor.builder().age(40).email("abc@xyz.com").gender("M").name("Jerry").phone("123").speciality(spec).build();
        doctorImpl.createDoctor(doctor);
        assertThat(doctorImpl.getDoctortById(doctor.getId())).isNotEmpty().contains(doctor);

        report = Report.builder().doctor(doctor).patient(patient).reportText(null).stamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).build();
        assertThat(impl.createReport(report)).isEqualTo(report);
    }

    @Order(2)
    @Test
    @DisplayName("Testing fetch on all reports")
    void testGetReportByDoctor() {
        assertThat(impl.getReportByDoctor(doctor)).isNotEmpty().contains(report);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single report by id")
    void testGetReportById() {
        assertThat(impl.getReportById(report.getId())).isNotEmpty().contains(report);
    }

    @Order(4)
    @Test
    @DisplayName("(UNIMPLEMENTED) Testing fetch on a single report by stamp")
    void testGetReportByStamp() {
        assertThat(true).isTrue();
    }

    @Order(5)
    @Test
    @DisplayName("Testing fetch on all reports by patient")
    void testGetReportsByPatient() {
        assertThat(impl.getReportsByPatient(patient)).isNotEmpty().contains(report);
    }

    @Order(6)
    @Test
    @DisplayName("Testing update on a single report by id")
    void testUpdateReport() {
        Patient tempPatient = patient.toBuilder().name("Tom").build();
        assertThat(patientImpl.createPatient(tempPatient)).isEqualTo(tempPatient).isNotEqualTo(patient);

        Doctor tempDoctor = doctor.toBuilder().name("Bob").build();
        assertThat(doctorImpl.createDoctor(tempDoctor)).isEqualTo(tempDoctor).isNotEqualTo(doctor);

        Report tempReport = report.toBuilder().doctor(tempDoctor).patient(tempPatient).build();
        assertThat(impl.updateReport(report.getId(), tempReport)).isEqualTo(tempReport).isNotEqualTo(report);
    }

    @Order(7)
    @Test
    @DisplayName("Testing delete on a single report by id")
    void testDeleteReport() {
        impl.deleteReport(report.getId());

        assertThat(impl.getReportById(report.getId())).isEmpty();
    }
}
