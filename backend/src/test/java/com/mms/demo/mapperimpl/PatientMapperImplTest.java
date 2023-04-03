package com.mms.demo.mapperimpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Patient;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.PatientDTO;
import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;


@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class PatientMapperImplTest {
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

    @Autowired
    DataTransferObjectMapper<Patient, PatientDTO> mapper;

    @Test
    void testDtoToEntity() {
        PatientDTO patientDTOtest = PatientDTO.builder().build();
        assertThatIllegalArgumentException().isThrownBy(() -> mapper.dtoToEntity(patientDTOtest));


    }

    @Test
    void testEntityToDto() {
        PatientDTO patientDTO = generateRandomPatientDTO();
        Patient patient = mapper.dtoToEntity(patientDTO);
        assertThat(patient).isNotNull();

        assertThat(patientDTO).isEqualTo(mapper.entityToDto(patient));
    }
}
