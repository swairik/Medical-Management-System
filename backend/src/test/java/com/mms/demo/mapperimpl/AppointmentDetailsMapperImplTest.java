package com.mms.demo.mapperimpl;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import static org.assertj.core.api.Assertions.*;

import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AppointmentDetailsMapperImplTest {
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

    private AppointmentDetailsDTO generateRandomDetailsDTO() {
        return AppointmentDetailsDTO.builder().feedback(genAlnum(100)).prescription(genAlnum(100))
                        .build();
    }

    @Autowired
    DataTransferObjectMapper<AppointmentDetails, AppointmentDetailsDTO> mapper;

    @Test
    void testDtoToEntity() {
        AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();

        assertThat(mapper.dtoToEntity(appointmentDetailsDTO)).isNotNull();

    }

    @Test
    void testEntityToDto() {
        AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();

        assertThat(appointmentDetailsDTO)
                        .isEqualTo(mapper.entityToDto(mapper.dtoToEntity(appointmentDetailsDTO)));
    }

    @Test
    void testJsonConversions() {
        AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
        AppointmentDetailsDTO convertedDTO = null;
        try {
            String jsonString = mapper.dtoToJson(appointmentDetailsDTO);

            convertedDTO = mapper.jsonToDto(jsonString, AppointmentDetailsDTO.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }

        assertThat(convertedDTO).isNotNull();
    }
}
