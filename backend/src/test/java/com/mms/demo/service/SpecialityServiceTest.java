// package com.mms.demo.service;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Order;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;

// import com.mms.demo.entity.Speciality;
// import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest
// @TestMethodOrder(OrderAnnotation.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
// public class SpecialityServiceTest {
// @Autowired
// SpecialityService impl;

// static final Speciality spec = Speciality.builder().name("Spec").build();

// @Order(1)
// @Test
// @DisplayName("Testing create on a single speciality")
// void testCreateSpeciality() {
// assertThat(impl.createSpeciality(spec)).isEqualTo(spec);
// }

// @Order(2)
// @Test
// @DisplayName("Testing fetch on all specialities")
// void testGetAllSpecialities() {
// assertThat(impl.getAllSpecialities()).isNotEmpty();
// }

// @Order(3)
// @Test
// @DisplayName("Testing fetch on a single speciality by id")
// void testGetSpecialityById() {
// assertThat(impl.getSpecialityById(spec.getId())).isNotEmpty().contains(spec);
// }

// @Order(4)
// @Test
// @DisplayName("Testing update on a single speciality by id")
// void testUpdateSpeciality() {
// Speciality temp = Speciality.builder().name("Spec 2").build();
// impl.createSpeciality(temp);
// Speciality tempUpdate = temp.toBuilder().name("Spec 2.1").build();
// assertThat(impl.updateSpeciality(temp.getId(),
// tempUpdate)).isEqualTo(tempUpdate).isNotEqualTo(temp);
// }

// @Order(5)
// @Test
// @DisplayName("Testing delete on a single speciality by id")
// void testDeleteSpeciality() {
// Speciality temp = Speciality.builder().name("Spec 3").build();
// assertThat(impl.createSpeciality(temp)).isEqualTo(temp);

// impl.deleteSpeciality(temp.getId());
// assertThat(impl.getSpecialityById(temp.getId())).isEmpty();
// }
// }
