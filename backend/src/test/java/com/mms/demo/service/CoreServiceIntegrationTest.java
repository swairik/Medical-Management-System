// package com.mms.demo.service;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
// import org.junit.jupiter.api.Order;
// import org.junit.jupiter.api.DisplayName;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;

// import com.mms.demo.entity.Appointment;
// import com.mms.demo.entity.Doctor;
// import com.mms.demo.entity.Patient;
// import com.mms.demo.mapper.AppointmentMapper;
// import com.mms.demo.mapper.PatientMapper;
// import com.mms.demo.transferobject.PatientDTO;
// import static org.assertj.core.api.Assertions.assertThat;

// import java.time.DayOfWeek;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;

// @SpringBootTest
// @TestMethodOrder(OrderAnnotation.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
// public final class CoreServiceIntegrationTest {
//     private String genAlnum(int targetStringLength) {
//         int leftLimit = 48;
//         int rightLimit = 122;
//         Random random = new Random();

//         String generatedString = random.ints(leftLimit, rightLimit + 1)
//                         .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                         .limit(targetStringLength).collect(StringBuilder::new,
//                                         StringBuilder::appendCodePoint, StringBuilder::append)
//                         .toString();

//         return generatedString;
//     }

//     @Autowired
//     AppointmentDetailsService detailsService;

//     @Autowired
//     AppointmentService appointmentService;

//     @Autowired
//     DoctorService doctorService;

//     @Autowired
//     PatientService patientService;
//     @Autowired
//     PatientMapper patientMapper;

//     @Autowired
//     ReportService reportService;

//     @Autowired
//     ScheduleService scheduleService;

//     @Autowired
//     SpecialityService specialityService;


//     @Order(1)
//     @DisplayName("Test the creation of patients")
//     @Test
//     void testCreatePatient() {
//         PatientDTO patientDTO = PatientDTO.builder().age(10).email(genAlnum(10) + "@xyz.com")
//                         .gender("M").phone(genAlnum(10)).name(genAlnum(14)).build();

//         patientDTO = patientService.create(patientDTO);
//         assertThat(patientService.get(patientDTO.id())).contains(patientDTO);
//     }

//     @Order(1)
//     @DisplayName("Test the creation of doctors")
//     @Test
//     void testCreateDoctor() {
//         ArrayList<Doctor> doctors = new ArrayList<Doctor>();

//     }

// }
