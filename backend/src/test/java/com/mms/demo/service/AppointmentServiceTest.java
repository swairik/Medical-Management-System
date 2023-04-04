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
                        .rating(5L).build();
    }

    private AppointmentDTO generateRandomAppointmentDTO() {
        return AppointmentDTO.builder().appointmentDetails(generateRandomDetailsDTO())
                        .doctor(generateRandomDoctorDTO()).patient(generateRandomPatientDTO())
                        .start(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).build();
    }

    private AppointmentDTO generateRandomPersistentAppointmentDTO() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());
        ScheduleDTO scheduleDTO = scheduleService
                        .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);
        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
        AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                        scheduleDTO.getId(), appointmentDetailsDTO);

        return appointmentDTO;
    }

    static private LocalDateTime clock = LocalDateTime.now().plusDays(1);

    private LocalDateTime getNextTick() {
        LocalDateTime temp = clock;
        clock = clock.plusMinutes(30);
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


        appointmentDTO = generateRandomPersistentAppointmentDTO();
        assertThat(appointmentDTO.getId()).isNotNull();
        assertThat(appointmentDTO.getAppointmentDetails().getId()).isNotNull();
    }

    @Test
    void testDelete() {
        appointmentDTO = generateRandomPersistentAppointmentDTO();
        appointmentService.delete(appointmentDTO.getId());
        assertThat(appointmentService.get(appointmentDTO.getId())).isEmpty();
    }

    @Test
    void testGet() {
        appointmentDTO = generateRandomPersistentAppointmentDTO();
        Long id = appointmentDTO.getId();
        assertThat(appointmentService.get(id)).contains(appointmentDTO);
        assertThat(appointmentService.get(id - 1)).isNotSameAs(Optional.of(appointmentDTO));
        assertThat(appointmentService.get(id + 1)).isEmpty();
    }

    @Test
    void testGetAll() {
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            appointmentDTOs.add(generateRandomPersistentAppointmentDTO());
        }

        assertThat(appointmentService.getAll()).containsAll(appointmentDTOs);
    }

    @Test
    void testGetAllByDoctor() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);
            PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }

        assertThat(appointmentService.getAllByDoctor(doctorDTO.getId()))
                        .containsAll(appointmentDTOs);
    }

    @Test
    void testGetAllByDoctorAfter() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());
        LocalDateTime after = getNextTick();
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);
            PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }

        assertThat(appointmentService.getAllByDoctorAfter(doctorDTO.getId(), after))
                        .containsExactlyElementsOf(appointmentDTOs);
    }

    @Test
    void testGetAllByDoctorBetween() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());
        LocalDateTime after = getNextTick();
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);
            PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }
        LocalDateTime before = getNextTick();
        assertThat(appointmentService.getAllByDoctorBetween(doctorDTO.getId(), after, before))
                        .containsExactlyElementsOf(appointmentDTOs);
    }

    @Test
    void testGetAllByPatient() {

        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                            specialityService.create(generateRandomSpecialityDTO()).getId());
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);
            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }

        assertThat(appointmentService.getAllByPatient(patientDTO.getId()))
                        .containsAll(appointmentDTOs);
    }

    @Test
    void testGetAllByPatientAfter() {
        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        LocalDateTime after = getNextTick();
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                            specialityService.create(generateRandomSpecialityDTO()).getId());
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);

            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }

        assertThat(appointmentService.getAllByPatientAfter(patientDTO.getId(), after))
                        .containsExactlyElementsOf(appointmentDTOs);
    }

    @Test
    void testGetAllByPatientAndDoctor() {
        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);

            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }

        assertThat(appointmentService.getAllByPatientAndDoctor(patientDTO.getId(),
                        doctorDTO.getId())).containsAll(appointmentDTOs);
    }

    @Test
    void testGetAllByPatientAndDoctorAfter() {
        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        LocalDateTime after = getNextTick();
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);

            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }

        assertThat(appointmentService.getAllByPatientAndDoctorAfter(patientDTO.getId(),
                        doctorDTO.getId(), after)).containsExactlyElementsOf(appointmentDTOs);
    }

    @Test
    void testGetAllByPatientAndDoctorBetween() {
        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());

        LocalDateTime after = getNextTick();
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);

            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }
        LocalDateTime before = getNextTick();
        assertThat(appointmentService.getAllByPatientAndDoctorBetween(patientDTO.getId(),
                        doctorDTO.getId(), after, before))
                                        .containsExactlyElementsOf(appointmentDTOs);
    }

    @Test
    void testGetAllByPatientBetween() {
        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        LocalDateTime after = getNextTick();
        ArrayList<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                            specialityService.create(generateRandomSpecialityDTO()).getId());
            ScheduleDTO scheduleDTO = scheduleService
                            .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);

            AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
            AppointmentDTO appointmentDTO = appointmentService.create(patientDTO.getId(),
                            scheduleDTO.getId(), appointmentDetailsDTO);
            appointmentDTOs.add(appointmentDTO);
        }
        LocalDateTime before = getNextTick();
        assertThat(appointmentService.getAllByPatientBetween(patientDTO.getId(), after, before))
                        .containsExactlyElementsOf(appointmentDTOs);
    }

    @Test
    void testMarkAsAttended() {
        appointmentDTO = generateRandomPersistentAppointmentDTO();
        assertThat(appointmentDTO.getAttended()).isFalse();
        appointmentService.markAsAttended(appointmentDTO.getId());
        assertThat(appointmentService.get(appointmentDTO.getId()).get().getAttended()).isTrue();

    }

    @Test
    void testUpdateSchedule() {
        DoctorDTO doctorDTO = doctorService.create(generateRandomDoctorDTO(),
                        specialityService.create(generateRandomSpecialityDTO()).getId());
        ScheduleDTO scheduleDTO = scheduleService
                        .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);
        PatientDTO patientDTO = patientService.create(generateRandomPatientDTO());
        AppointmentDetailsDTO appointmentDetailsDTO = generateRandomDetailsDTO();
        appointmentDTO = appointmentService.create(patientDTO.getId(), scheduleDTO.getId(),
                        appointmentDetailsDTO);

        ScheduleDTO scheduleDTOtest = scheduleService
                        .create(doctorDTO.getId(), getNextTick(), Optional.empty()).get(0);

        appointmentService.updateSchedule(appointmentDTO.getId(), scheduleDTOtest.getId());
        scheduleDTO = scheduleService.get(scheduleDTO.getId()).get();
        scheduleDTOtest = scheduleService.get(scheduleDTOtest.getId()).get();
        assertThat(scheduleDTO.getBooked()).isFalse();
        assertThat(scheduleDTOtest.getBooked()).isTrue();
    }
}
