package com.mms.demo.mapperimpl;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mms.demo.entity.Doctor;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.SpecialityDTO;
import static org.assertj.core.api.Assertions.*;

import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class DoctorMapperImplTest {

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

    private SpecialityDTO generateRandomSpecialityDTO() {
        return SpecialityDTO.builder().name(genAlnum(6)).build();
    }

    private DoctorDTO generateRandomDoctorDTO() {
        Random rng = new Random();
        return DoctorDTO.builder().age(rng.nextInt(90)).email(genAlnum(14) + "@xyz.com")
                        .name(genAlnum(14)).phone(genAlnum(10))
                        .speciality(generateRandomSpecialityDTO()).build();
    }

    @Autowired
    DataTransferObjectMapper<Doctor, DoctorDTO> mapper;

    static DoctorDTO doctorDTOtest;

    @Test
    void testDtoToEntity() {
        doctorDTOtest = generateRandomDoctorDTO().toBuilder().speciality(null).build();
        assertThatIllegalArgumentException().isThrownBy(() -> mapper.dtoToEntity(doctorDTOtest));

        doctorDTOtest = generateRandomDoctorDTO().toBuilder().email(null).build();
        assertThatIllegalArgumentException().isThrownBy(() -> mapper.dtoToEntity(doctorDTOtest));

        doctorDTOtest = generateRandomDoctorDTO().toBuilder().phone(null).build();
        assertThatIllegalArgumentException().isThrownBy(() -> mapper.dtoToEntity(doctorDTOtest));

        DoctorDTO doctorDTO = generateRandomDoctorDTO();

        assertThat(mapper.dtoToEntity(doctorDTO)).isNotNull();
    }

    @Test
    void testEntityToDto() {
        DoctorDTO doctorDTO = generateRandomDoctorDTO();

        assertThat(doctorDTO).isEqualTo(mapper.entityToDto(mapper.dtoToEntity(doctorDTO)));
    }

    @Test
    void testJsonConversions() {
        DoctorDTO doctorDTO = generateRandomDoctorDTO();
        DoctorDTO convertedDTO = null;
        try {
            String jsonString = mapper.dtoToJson(doctorDTO);

            convertedDTO = mapper.jsonToDto(jsonString, DoctorDTO.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }

        assertThat(convertedDTO).isNotNull();
    }
}
