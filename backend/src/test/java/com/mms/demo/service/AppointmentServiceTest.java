package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;
import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.AppointmentDTO;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.PatientDTO;
import com.mms.demo.transferobject.ScheduleDTO;
import com.mms.demo.transferobject.SpecialityDTO;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AppointmentServiceTest {
    private String genAlnum(int targetStringLength) {
        int leftLimit = 48;
        int rightLimit = 122;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(targetStringLength).collect(StringBuilder::new,
                                        StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();

        return generatedString;
    }

    private PatientDTO generateRandomPatientDTO() {
        Random rng = new Random();
        return PatientDTO.builder().age(rng.nextInt(90)).email(genAlnum(14) + "@xyz.com")
                        .name(genAlnum(14)).phone(genAlnum(10)).build();
    }

    private SpecialityDTO generateRandomSpecialityDTO() {
        return SpecialityDTO.builder().name(genAlnum(6)).build();
    }

    private DoctorDTO generateRandomDoctorDTO() {
        Random rng = new Random();
        return DoctorDTO.builder().age(rng.nextInt(90)).email(genAlnum(14) + "@xyz.com")
                        .name(genAlnum(14)).phone(genAlnum(10))
                        .speciality(generateRandomSpecialityDTO()).build();
    }

    private AppointmentDetailsDTO generateRandomDetailsDTO() {
        return AppointmentDetailsDTO.builder().feedback(genAlnum(100)).prescription(genAlnum(100))
                        .build();
    }

    private AppointmentDTO generateRandomAppointmentDTO() {
        return AppointmentDTO.builder().appointmentDetails(generateRandomDetailsDTO())
                        .doctor(generateRandomDoctorDTO()).patient(generateRandomPatientDTO())
                        .start(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).build();
    }

    static private LocalDateTime clock = LocalDateTime.now();

    private LocalDateTime getNextTick() {
        LocalDateTime temp = clock;
        clock = clock.plusMinutes(5);
        return temp;
    }

    @Autowired
    private DataTransferObjectMapper<Appointment, AppointmentDTO> mapper;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    SpecialityService specialityService;

    @Autowired
    PatientService patientService;

    @Autowired
    ScheduleService scheduleService;

    static AppointmentDTO appointmentDTO;

    @Test
    void testCreate() {
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                        .isThrownBy(() -> appointmentService.create(null, null, null));
        appointmentDTO = generateRandomAppointmentDTO();
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(
                        () -> appointmentService.create(appointmentDTO.getPatient().getId(), null,
                                        appointmentDTO.getAppointmentDetails()));

        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());
        ScheduleDTO scheduleDTO = scheduleService
                        .create(doctorDTO.getId(), getNextTick(), Optional.of(getNextTick()))
                        .get(0);
        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        appointmentDTO = appointmentService.create(patientDTO.getId(), scheduleDTO.getId(),
                        generateRandomDetailsDTO());
        assertThat(appointmentDTO.getId()).isNotNull();



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
