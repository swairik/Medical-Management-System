package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.SpecialityDTO;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class DoctorServiceTest {
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
        Random rng = new Random();
        return SpecialityDTO.builder().name(genAlnum(6)).build();
    }

    private DoctorDTO generateRandomDoctorDTO() {
        Random rng = new Random();
        return DoctorDTO.builder().age(rng.nextInt(90)).email(genAlnum(14) + "@xyz.com")
                .name(genAlnum(14)).phone(genAlnum(10)).speciality(generateRandomSpecialityDTO()).build();
    }

    @Autowired
    DoctorService doctorService;

    @Autowired
    SpecialityService specialityService;

    @Autowired
    DataTransferObjectMapper<Doctor, DoctorDTO> mapper;

    static DoctorDTO doctorDTO;

    @Test
    void testCreate() {
        assertThatIllegalArgumentException().isThrownBy(() -> doctorService.create(null, null));

        doctorDTO = generateRandomDoctorDTO();
        assertThatIllegalArgumentException().isThrownBy(() -> doctorService.create(doctorDTO, null));
        assertThatIllegalArgumentException().isThrownBy(() -> doctorService.create(doctorDTO, 1L));

        doctorDTO = doctorDTO.toBuilder().speciality(specialityService.create(doctorDTO.getSpeciality())).build();
        assertThat(doctorService.create(doctorDTO, doctorDTO.getSpeciality().getId())).extracting("id").isNotNull();
    }

    @Test
    void testDelete() {
        doctorDTO = generateRandomDoctorDTO();
        doctorDTO = doctorDTO.toBuilder().speciality(specialityService.create(doctorDTO.getSpeciality())).build();
        doctorDTO = doctorService.create(doctorDTO, doctorDTO.getSpeciality().getId());
        doctorService.delete(doctorDTO.getId());
        assertThat(doctorService.get(doctorDTO.getId())).isEmpty();
    }

    @Test
    void testGet() {
        doctorDTO = generateRandomDoctorDTO();
        doctorDTO = doctorDTO.toBuilder().speciality(specialityService.create(doctorDTO.getSpeciality())).build();
        doctorDTO = doctorService.create(doctorDTO, doctorDTO.getSpeciality().getId());
        assertThat(doctorService.get(doctorDTO.getId())).contains(doctorDTO);
        assertThat(doctorService.get(doctorDTO.getId() - 1)).isNotSameAs(Optional.of(doctorDTO));
        assertThat(doctorService.get(doctorDTO.getId() + 1)).isEmpty();
    }

    @Test
    void testGetAll() {
        ArrayList<DoctorDTO> doctorDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            doctorDTO = generateRandomDoctorDTO();
            doctorDTO = doctorDTO.toBuilder().speciality(specialityService.create(doctorDTO.getSpeciality())).build();
            doctorDTO = doctorService.create(doctorDTO, doctorDTO.getSpeciality().getId());
            doctorDTOs.add(doctorDTO);
        }

        assertThat(doctorService.getAll()).containsAll(doctorDTOs);
    }

    @Test
    void testGetAllBySpeciality() {
        ArrayList<DoctorDTO> doctorDTOs = new ArrayList<>();
        SpecialityDTO specialityDTO = specialityService.create(generateRandomSpecialityDTO());
        for (int i = 0; i < 5; i++) {
            doctorDTO = generateRandomDoctorDTO();
            doctorDTO = doctorDTO.toBuilder().speciality(specialityDTO).build();
            doctorDTO = doctorService.create(doctorDTO, doctorDTO.getSpeciality().getId());
            doctorDTOs.add(doctorDTO);
        }

        assertThat(doctorService.getAll()).containsAll(doctorDTOs);
    }

    @Test
    void testUpdate() {
        doctorDTO = generateRandomDoctorDTO();
        doctorDTO = doctorDTO.toBuilder().speciality(specialityService.create(doctorDTO.getSpeciality())).build();
        doctorDTO = doctorService.create(doctorDTO, doctorDTO.getSpeciality().getId());

        DoctorDTO updates = doctorDTO.toBuilder().name(genAlnum(10)).build();
        assertThat(doctorService.update(doctorDTO.getId(), updates)).contains(updates);

        assertThatIllegalArgumentException().isThrownBy(() -> doctorService.update(doctorDTO.getId() + 20, updates));
    }

    @Test
    void testUpdateSpeciality() {
        doctorDTO = generateRandomDoctorDTO();
        doctorDTO = doctorDTO.toBuilder().speciality(specialityService.create(doctorDTO.getSpeciality())).build();
        doctorDTO = doctorService.create(doctorDTO, doctorDTO.getSpeciality().getId());

        SpecialityDTO specialityDTO = specialityService.create(generateRandomSpecialityDTO());

        DoctorDTO updatedDTO = doctorDTO.toBuilder().speciality(specialityDTO).build();
        assertThat(doctorService.updateSpeciality(doctorDTO.getId(), specialityDTO.getId())).contains(updatedDTO);
    }

}
