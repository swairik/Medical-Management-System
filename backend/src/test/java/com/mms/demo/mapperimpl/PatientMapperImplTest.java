package com.mms.demo.mapperimpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mms.demo.entity.Patient;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.PatientDTO;
import static org.assertj.core.api.Assertions.*;
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
        PatientDTO patientDTOtest = generateRandomPatientDTO().toBuilder().email("").build();

        assertThat(mapper.dtoToEntity(patientDTOtest)).isNotNull();
    }

    @Test
    void testEntityToDto() {
        PatientDTO patientDTO = generateRandomPatientDTO();

        assertThat(patientDTO).isEqualTo(mapper.entityToDto(mapper.dtoToEntity(patientDTO)));
    }

    @Test
    void testJsonConversions() {
        PatientDTO patientDTO = generateRandomPatientDTO();
        PatientDTO convertedDTO = null;
        try {
            String jsonString = mapper.dtoToJson(patientDTO);

            convertedDTO = mapper.jsonToDto(jsonString, PatientDTO.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }

        assertThat(convertedDTO).isNotNull();
    }
}
