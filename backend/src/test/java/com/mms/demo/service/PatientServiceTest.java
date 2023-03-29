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
// import com.mms.demo.mapper.PatientMapper;
// import com.mms.demo.transferobject.PatientDTO;
// import static org.assertj.core.api.Assertions.assertThat;
// import java.util.ArrayList;
// import java.util.Optional;
// import java.util.Random;


// @SpringBootTest
// @TestMethodOrder(OrderAnnotation.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
// public class PatientServiceTest {

// private String genAlnum(int targetStringLength) {
// int leftLimit = 48;
// int rightLimit = 122;
// Random random = new Random();

// String generatedString = random.ints(leftLimit, rightLimit + 1)
// .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
// .limit(targetStringLength).collect(StringBuilder::new,
// StringBuilder::appendCodePoint, StringBuilder::append)
// .toString();

// return generatedString;
// }

// private PatientDTO generateRandomPatientDTO() {
// Random rng = new Random();
// return PatientDTO.builder().age(rng.nextInt(90)).email(genAlnum(14) + "@xyz.com")
// .name(genAlnum(14)).phone(genAlnum(10)).build();
// }

// @Autowired
// PatientService patientService;
// @Autowired
// PatientMapper patientMapper;

// @Test
// void testCreate() {
// PatientDTO patientDTO = generateRandomPatientDTO();
// Patient patient = patientMapper.dtoToEntity(patientDTO);

// assertThat(patient.getName()).isEqualTo(patientDTO.name());

// assertThat(patientDTO.id()).isNotNull();
// }



// @Test
// void testGet() {
// PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
// assertThat(patientService.get(patientDTO.id())).contains(patientDTO);
// assertThat(patientService.get(patientDTO.id() - 1)).isNotSameAs(Optional.of(patientDTO));
// assertThat(patientService.get(patientDTO.id() + 1)).isEmpty();
// }

// @Test
// void testDelete() {
// PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
// patientService.delete(patientDTO.id());
// assertThat(patientService.get(patientDTO.id())).isEmpty();
// }

// @Test
// void testGetAll() {
// ArrayList<PatientDTO> patientDTOs = new ArrayList<>();
// for (int i = 0; i < 5; i++) {
// patientDTOs.add(patientService.create(generateRandomPatientDTO()));
// }

// ArrayList<PatientDTO> excludedPatientDTOs = new ArrayList<>();
// for (int i = 0; i < 5; i++) {
// excludedPatientDTOs.add(patientService.create(generateRandomPatientDTO()));
// }

// assertThat(patientService.getAll()).containsAll(patientDTOs);
// assertThat(patientService.getAll()).doesNotContainAnyElementsOf(excludedPatientDTOs);
// }

// @Test
// void testUpdate() {
// PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
// PatientDTO updates = patientDTO.toBuilder().name(genAlnum(10)).build();

// assertThat(patientService.update(patientDTO.id(), updates)).contains(updates);
// assertThat(patientService.update(100L, updates)).isEmpty();
// }
// }
