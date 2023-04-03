// package com.mms.demo.service;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Order;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;

// import com.mms.demo.entity.Doctor;
// import com.mms.demo.entity.Speciality;
// import static org.assertj.core.api.Assertions.assertThat;

// import java.util.Random;


// @SpringBootTest
// @TestMethodOrder(OrderAnnotation.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
// public class DoctorServiceTest {
// @Autowired
// DoctorService impl;

// @Autowired
// SpecialityService specImpl;

// private String genAlnum(int targetStringLength) {
// int leftLimit = 48;
// int rightLimit = 122;
// Random random = new Random();

// String generatedString = random.ints(leftLimit, rightLimit + 1)
// .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
// .limit(targetStringLength)
// .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
// .toString();

// return generatedString;
// }

// static final Speciality spec = Speciality.builder().name("Dentist").build();
// static Doctor doctor;

// @Test
// @Order(1)
// @DisplayName("Testing create on a single doctor")
// void testCreateDoctor() {
// specImpl.createSpeciality(spec);
// assertThat(specImpl.getSpecialityById(spec.getId())).isNotEmpty().contains(spec);

// doctor = Doctor.builder().age(40).email(genAlnum(10) +
// "@xyz.com").gender("M").name("Jerry").phone("123").speciality(spec).build();
// assertThat(impl.createDoctor(doctor)).isEqualTo(doctor);
// }

// @Order(2)
// @Test
// @DisplayName("Testing fetch on all doctors")
// void testGetAllDoctors() {
// assertThat(impl.getAllDoctors()).isNotEmpty().contains(doctor);
// }

// @Order(3)
// @Test
// @DisplayName("Testing fetch on a single doctor by speciality")
// void testGetDoctorBySpeciality() {
// assertThat(impl.getDoctorBySpeciality(spec)).isNotEmpty().contains(doctor);
// }

// @Order(4)
// @Test
// @DisplayName("Testing on a single doctor by id")
// void testGetDoctortById() {
// assertThat(impl.getDoctortById(doctor.getId())).isNotEmpty().contains(doctor);
// }

// @Order(5)
// @Test
// @DisplayName("Testing update on a single doctor")
// void testUpdateDoctor() {
// Doctor tempDoctor = doctor.toBuilder().name("Bob").build();

// assertThat(impl.updateDoctor(doctor.getId(),
// tempDoctor)).isEqualTo(tempDoctor).isNotEqualTo(doctor);
// }

// @Order(6)
// @Test
// @DisplayName("Testing delete on a single doctor by id")
// void testDeleteDoctor() {
// impl.deleteDoctor(doctor.getId());

// assertThat(impl.getDoctortById(doctor.getId())).isEmpty();
// }
// }
