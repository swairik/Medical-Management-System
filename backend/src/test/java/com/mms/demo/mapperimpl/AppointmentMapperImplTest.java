package com.mms.demo.mapperimpl;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mms.demo.entity.Appointment;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.AppointmentDTO;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.PatientDTO;
import com.mms.demo.transferobject.SpecialityDTO;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AppointmentMapperImplTest {
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
                        .speciality(generateRandomSpecialityDTO()).ratingCount(0L)
                        .ratingAverage(0.0).build();
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

    @Autowired
    private DataTransferObjectMapper<Appointment, AppointmentDTO> mapper;

    static AppointmentDTO appointmentDTOtest;

    @Test
    void testDtoToEntity() {
        AppointmentDTO appointmentDTO = generateRandomAppointmentDTO();

        assertThat(mapper.dtoToEntity(appointmentDTO)).isNotNull();

    }

    @Test
    void testEntityToDto() {
        AppointmentDTO appointmentDTO = generateRandomAppointmentDTO();

        assertThat(appointmentDTO)
                        .isEqualTo(mapper.entityToDto(mapper.dtoToEntity(appointmentDTO)));
    }

    @Test
    void testJsonConversions() {
        AppointmentDTO appointmentDTO = generateRandomAppointmentDTO();
        AppointmentDTO convertedDTO = null;
        try {
            String jsonString = mapper.dtoToJson(appointmentDTO);

            convertedDTO = mapper.jsonToDto(jsonString, AppointmentDTO.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }

        assertThat(convertedDTO).isNotNull();
    }
}
