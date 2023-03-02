package com.mms.demo.doctor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.SpecialityService;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class DoctorServiceTest {
    @Autowired
    DoctorService impl;

    @Autowired
    SpecialityService specImpl;

    static final Speciality spec = Speciality.builder().name("Dentist").build();
    static Doctor doctor;

    @Test
    @Order(1)
    @DisplayName("Testing create on a single doctor")
    void testCreateDoctor() {
        specImpl.createSpeciality(spec);
        assertThat(specImpl.getSpecialityById(spec.getId())).isNotEmpty().contains(spec);

        doctor = Doctor.builder().age(40).email("abc@xyz.com").gender("M").name("Jerry").phone("123").speciality(spec).build();
        assertThat(impl.createDoctor(doctor)).isEqualTo(doctor);
    }

    @Order(2)
    @Test
    @DisplayName("Testing fetch on all doctors")
    void testGetAllDoctors() {
        assertThat(impl.getAllDoctors()).isNotEmpty().contains(doctor);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single doctor by speciality")
    void testGetDoctorBySpeciality() {
        assertThat(impl.getDoctorBySpeciality(spec)).isNotEmpty().contains(doctor);
    }

    @Order(4)
    @Test
    @DisplayName("Testing on a single doctor by id")
    void testGetDoctortById() {
        assertThat(impl.getDoctortById(doctor.getId())).isNotEmpty().contains(doctor);
    }

    @Order(5)
    @Test
    @DisplayName("Testing update on a single doctor")
    void testUpdateDoctor() {
        Doctor tempDoctor = doctor.toBuilder().name("Bob").build();

        assertThat(impl.updateDoctor(doctor.getId(), tempDoctor)).isEqualTo(tempDoctor).isNotEqualTo(doctor);
    }

    @Order(6)
    @Test
    @DisplayName("Testing delete on a single doctor by id")
    void testDeleteDoctor() {
        impl.deleteDoctor(doctor.getId());

        assertThat(impl.getDoctortById(doctor.getId())).isEmpty();
    }
}
