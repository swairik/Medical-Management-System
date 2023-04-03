package com.mms.demo.mapperimpl;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.SpecialityDTO;
import static org.assertj.core.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class SpecialityMapperImplTest {
    @Autowired
    private DataTransferObjectMapper<Speciality, SpecialityDTO> mapper;

    @Test
    void testDtoToEntity() {
        final SpecialityDTO specialityDTOtest = SpecialityDTO.builder().id(1L).name(null).build();

        assertThatIllegalArgumentException()
                        .isThrownBy(() -> mapper.dtoToEntity(specialityDTOtest));

        SpecialityDTO specialityDTO = SpecialityDTO.builder().name("Spec").build();
        Speciality speciality = mapper.dtoToEntity(specialityDTO);
        assertThat(speciality).isNotNull();

        assertThat(speciality.getId()).isEqualTo(specialityDTO.getId());
        assertThat(speciality.getName()).isEqualTo(specialityDTO.getName());
    }

    @Test
    void testEntityToDto() {
        Speciality speciality = Speciality.builder().id(1L).name("ABC").build();

        SpecialityDTO specialityDTO = mapper.entityToDto(speciality);

        assertThat(speciality).isNotNull();

        assertThat(speciality.getId()).isEqualTo(specialityDTO.getId());
        assertThat(speciality.getName()).isEqualTo(specialityDTO.getName());
    }

    @Test
    void testJsonConversions() {
        SpecialityDTO specialityDTO = SpecialityDTO.builder().id(1L).name("ABC").build();
        SpecialityDTO convertedDTO = null;
        try {
            String jsonString = mapper.dtoToJson(specialityDTO);

            convertedDTO = mapper.jsonToDto(jsonString, SpecialityDTO.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }

        System.out.println(convertedDTO);
    }
}
