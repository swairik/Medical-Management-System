// package com.mms.demo.service;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Order;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;

// import com.mms.demo.entity.Patient;


// import static org.assertj.core.api.Assertions.assertThat;

// import java.util.Random;


// @SpringBootTest
// @TestMethodOrder(OrderAnnotation.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
// public class PatientServiceTest {

// @Autowired
// PatientService impl;

// static private String genAlnum(int targetStringLength) {
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

// static final Patient temp = Patient.builder().age(10).email(genAlnum(10) + "@xyz.com")
// .gender("M").phone("1010101010").name("Jerry")
// .build();

// @Order(1)
// @DisplayName("Testing create on a single patient")
// @Test
// void testCreatePatient() {
// assertThat(impl.createPatient(temp)).isEqualTo(temp);
// }

// @Order(2)
// @DisplayName("Testing fetch on all patients")
// @Test
// void testGetAllPatients() {
// impl.createPatient(temp);
// assertThat(impl.getAllPatients()).isNotEmpty().contains(temp);
// }

// @Order(3)
// @DisplayName("Testing fetch on a single patient by id")
// @Test
// void testGetPatientById() {
// impl.createPatient(temp);
// assertThat(impl.getPatientById(temp.getId())).isNotEmpty().contains(temp);
// }

// @Order(4)
// @DisplayName("Testing update on a single patient by id")
// @Test
// void testUpdatePatient() {
// impl.createPatient(temp);
// Patient tempUpdate = temp.toBuilder().build();
// tempUpdate.setName("Tom");
// assertThat(impl.updatePatient(temp.getId(),
// tempUpdate)).isNotNull().isEqualTo(tempUpdate).isNotEqualTo(temp);
// }

// @Order(5)
// @DisplayName("Testing delete on a single patient by id")
// @Test
// void testDeletePatient() {
// impl.createPatient(temp);
// impl.deletePatient(temp.getId());
// assertThat(impl.getPatientById(temp.getId())).isEmpty();
// }
// }
