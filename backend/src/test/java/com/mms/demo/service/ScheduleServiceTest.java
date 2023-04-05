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

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.ScheduleDTO;
import com.mms.demo.transferobject.SpecialityDTO;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ScheduleServiceTest {
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

    static private LocalDateTime clock = LocalDateTime.now().plusDays(1);

    private LocalDateTime getNextTick() {
        LocalDateTime temp = clock;
        clock = clock.plusMinutes(30);
        return temp.truncatedTo(ChronoUnit.MINUTES);
    }

    @Autowired
    SpecialityService specialityService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    ScheduleService scheduleService;

    static ScheduleDTO scheduleDTO;
    static List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

    @Test
    void testCreate() {
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                        .isThrownBy(() -> scheduleService.create(null, null, null));

        assertThatIllegalArgumentException().isThrownBy(
                        () -> scheduleService.create(200L, LocalDateTime.now(), Optional.empty()));

        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(), Optional.empty());
        assertThat(scheduleDTOs).isNotEmpty();
        scheduleDTO = scheduleDTOs.get(0);
        assertThat(scheduleDTO.getEnd()).isEqualTo(clock.truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void testDeleteSchedule() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(), Optional.empty());
        assertThat(scheduleDTOs).isNotEmpty();
        scheduleDTO = scheduleDTOs.get(0);

        scheduleService.deleteSchedule(scheduleDTO.getId());

        assertThat(scheduleService.get(scheduleDTO.getId())).isEmpty();
    }

    @Test
    void testGet() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(), Optional.empty());
        assertThat(scheduleDTOs).isNotEmpty();
        scheduleDTO = scheduleDTOs.get(0);
        Long scheduleID = scheduleDTO.getId();

        assertThat(scheduleService.get(scheduleID)).contains(scheduleDTO);
        assertThat(scheduleService.get(scheduleID - 1)).isNotSameAs(scheduleDTO);
        assertThat(scheduleService.get(scheduleID + 1)).isEmpty();
    }

    @Test
    void testGetAll() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(),
                        Optional.of(getNextTick().plusHours(1)));
        assertThat(scheduleDTOs).isNotEmpty();
        assertThat(scheduleService.getAll()).containsAll(scheduleDTOs);

        clock = clock.plusHours(12);
    }

    @Test
    void testGetAllUnapproved() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(),
                        Optional.of(getNextTick().plusHours(1)));
        assertThat(scheduleDTOs).isNotEmpty();
        assertThat(scheduleService.getAllUnapproved()).containsAll(scheduleDTOs);

        clock = clock.plusHours(12);
    }

    @Test
    void testGetApprovedByDoctor() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(),
                        Optional.of(getNextTick().plusHours(1)));
        assertThat(scheduleDTOs).isNotEmpty();

        for (ScheduleDTO dto : scheduleDTOs) {
            scheduleService.markAsApproved(dto.getId());
        }

        assertThat(scheduleService.getApprovedByDoctor(doctorDTO.getId(), false, Optional.empty()))
                        .isEmpty();
        assertThat(scheduleService.getApprovedByDoctor(doctorDTO.getId(), true, Optional.empty())
                        .stream().map(s -> s.getId()).collect(Collectors.toList()))
                                        .containsAll(scheduleDTOs.stream().map(s -> s.getId())
                                                        .collect(Collectors.toList()));

        clock = clock.plusHours(12);

        assertThat(scheduleService.getApprovedByDoctor(doctorDTO.getId(), true,
                        Optional.of(getNextTick()))).isEmpty();
    }

    @Test
    void testGetByDoctor() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());
        assertThat(doctorDTO.getId()).isNotNull();

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(), Optional.empty());
        assertThat(scheduleDTOs).isNotEmpty();

        assertThat(scheduleService.getByDoctor(doctorDTO.getId())).containsAll(scheduleDTOs);
    }

    @Test
    void testGetByDoctorAfter() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(), Optional.empty());
        scheduleDTO = scheduleDTOs.get(0);

        assertThat(scheduleService.getByDoctorAfter(doctorDTO.getId(),
                        scheduleDTO.getStart().minusMinutes(1))).contains(scheduleDTO);
    }

    @Test
    void testGetByDoctorBetween() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(), Optional.empty());
        scheduleDTO = scheduleDTOs.get(0);

        assertThat(scheduleService.getByDoctorBetween(doctorDTO.getId(),
                        scheduleDTO.getStart().minusMinutes(1),
                        scheduleDTO.getStart().plusMinutes(1))).contains(scheduleDTO);
    }

    @Test
    void testMarkAsApproved() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(), Optional.empty());
        scheduleDTO = scheduleDTOs.get(0);

        scheduleService.markAsApproved(scheduleDTO.getId());
        assertThat(scheduleService.get(scheduleDTO.getId()).get().getApprovalStatus()).isTrue();
    }

    @Test
    void testGetAllAfter() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        scheduleDTOs = scheduleService.create(doctorDTO.getId(), getNextTick(),
                        Optional.of(getNextTick().plusHours(1)));
        assertThat(scheduleDTOs).isNotEmpty();

        scheduleDTO = scheduleDTOs.get(0);
        assertThat(scheduleService.getAllAfter(scheduleDTO.getStart().minusMinutes(1)))
                        .containsAll(scheduleDTOs);

        clock = clock.plusHours(12);
    }
}
