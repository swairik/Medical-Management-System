package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;
import com.mms.demo.entity.Speciality;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.DayOfWeek;
import java.time.LocalTime;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ScheduleServiceTest {
    @Autowired
    ScheduleService impl;

    @Autowired
    SlotService slotImpl;

    @Autowired
    DoctorService doctorImpl;

    @Autowired
    SpecialityService specImpl;

    static Doctor doctor;
    static Slot slot;
    static Schedule sched;
    static Speciality spec;

    @Order(1)
    @Test
    @DisplayName("Testing on a single schedule by")
    void testCreateSchedule() {
        spec = Speciality.builder().name("Dentist").build();
        specImpl.createSpeciality(spec);
        assertThat(specImpl.getSpecialityById(spec.getId())).isNotEmpty().contains(spec);

        doctor = Doctor.builder().age(40).email("abc@xyz.com").gender("M").name("Jerry").phone("123").speciality(spec)
                .build();
        doctorImpl.createDoctor(doctor);
        assertThat(doctorImpl.getDoctortById(doctor.getId())).isNotEmpty().contains(doctor);

        slot = Slot.builder().start(LocalTime.of(0, 0, 0)).end(LocalTime.of(0, 0, 1)).weekday(DayOfWeek.MONDAY)
                .capacity(5).build();
        slotImpl.createSlot(slot);
        assertThat(slotImpl.getSlotById(slot.getId())).isNotEmpty().contains(slot);

        sched = Schedule.builder().doctor(doctor).slot(slot).year(2023).week(10).build();
        assertThat(impl.createSchedule(sched)).isEqualTo(sched);
    }

    @Order(2)
    @Test
    @DisplayName("Testing fetch on all schedules")
    void testGetAllSchedules() {
        assertThat(impl.getAllSchedules()).isNotEmpty().contains(sched);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single schedule by id")
    void testGetScheduleById() {
        assertThat(impl.getScheduleById(sched.getId())).isNotEmpty().contains(sched);
    }

    @Order(4)
    @Test
    @DisplayName("Testing fetch on a single schedule by doctor")
    void testGetSchedulesByDoctor() {
        assertThat(impl.getSchedulesByDoctor(doctor)).isNotEmpty().contains(sched);
    }

    @Order(5)
    @Test
    @DisplayName("Testing fetch on a single schedule by slot")
    void testGetSchedulesBySlot() {
        assertThat(impl.getSchedulesBySlot(slot)).isNotEmpty().contains(sched);
    }

    @Order(6)
    @Test
    @DisplayName("Testing update on a single schedule by id")
    void testUpdateSchedule() {
        Doctor tempDoctor = doctor.toBuilder().name("Bob").build();
        assertThat(doctorImpl.createDoctor(tempDoctor)).isEqualTo(tempDoctor).isNotEqualTo(doctor);

        Schedule tempSched = sched.toBuilder().doctor(tempDoctor).build();
        assertThat(impl.updateSchedule(sched.getId(), tempSched)).isEqualTo(tempSched).isNotEqualTo(sched);
    }

    @Order(7)
    @Test
    @DisplayName("Testing delete on a single schedule by di")
    void testDeleteSchedule() {
        impl.deleteSchedule(sched.getId());
        assertThat(impl.getScheduleById(sched.getId())).isEmpty();
    }
}
