package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Speciality;
import com.mms.demo.transferobject.SpecialityDTO;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class SpecialityServiceTest {
    @Autowired
    SpecialityService specialityService;

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

    @Order(1)
    @Test
    @DisplayName("Testing create on a single speciality")
    void testCreateSpeciality() {
        SpecialityDTO specialityDTO = generateRandomSpecialityDTO();
        assertThat(specialityService.create(specialityDTO).getName())
                        .isEqualTo(specialityDTO.getName());
    }

    @Order(2)
    @Test
    @DisplayName("Testing fetch on all specialities")
    void testGetAllSpecialities() {
        SpecialityDTO specialityDTO = specialityService.create(generateRandomSpecialityDTO());
        assertThat(specialityService.getAll()).contains(specialityDTO);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single speciality by id")
    void testGetSpecialityById() {
        SpecialityDTO specialityDTO = specialityService.create(generateRandomSpecialityDTO());
        assertThat(specialityService.get(specialityDTO.getId())).contains(specialityDTO);
    }

    @Order(4)
    @Test
    @DisplayName("Testing update on a single speciality by id")
    void testUpdateSpeciality() {
        SpecialityDTO specialityDTO = specialityService.create(generateRandomSpecialityDTO());
        Optional<SpecialityDTO> temp = specialityService.update(specialityDTO.getId(),
                        generateRandomSpecialityDTO());
        assertThat(temp.get().getId()).isEqualTo(specialityDTO.getId());
        assertThat(temp.get().getName()).isNotEqualTo(specialityDTO.getName());
    }

    @Order(5)
    @Test
    @DisplayName("Testing delete on a single speciality by id")
    void testDeleteSpeciality() {
        SpecialityDTO specialityDTO = specialityService.create(generateRandomSpecialityDTO());

        specialityService.delete(specialityDTO.getId());
        assertThat(specialityService.get(specialityDTO.getId())).isEmpty();
    }
}
