package com.mms.demo.appointment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.SlotService;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class AppointmentServiceTest {
    @Autowired
    AppointmentService impl;

    @Autowired
    PatientService patientImpl;

    @Autowired
    SlotService slotImpl;

    static final Patient patient = Patient.builder().age(25).email("temp@temp.com").gender("M").name("Jerry").phone("XYZ").build();
    static final Slot slot = Slot.builder().start(LocalTime.of(0, 0, 0)).end(LocalTime.of(0, 0, 1)).weekday(DayOfWeek.MONDAY).capacity(5).build();
    static Appointment appt;

    @Order(1)
    @Test
    @DisplayName("Testing creation of an appointment")
    void testCreateAppointment() {
        patientImpl.createPatient(patient);
        slotImpl.createSlot(slot);

        assertThat(patientImpl.getPatientById(patient.getId())).isNotEmpty().contains(patient);
        assertThat(slotImpl.getSlotById(slot.getId())).isNotEmpty().contains(slot);

        appt = Appointment.builder().patient(patient).slot(slot).build();

        assertThat(impl.createAppointment(appt)).isEqualTo(appt);
    }

    @Order(2)
    @Test
    @DisplayName("Testing fetch on all appointments")
    void testGetAllAppointments() {
        assertThat(impl.getAllAppointments()).isNotEmpty().contains(appt);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on single appointment by id")
    void testGetAppointmentById() {
        assertThat(impl.getAppointmentById(appt.getId())).isNotEmpty().contains(appt);
    }

    @Order(4)
    @Test
    @DisplayName("Testing fetch on single appointment by patient")
    void testGetAppointmentsByPatient() {
        assertThat(impl.getAppointmentsByPatient(patient)).isNotEmpty().contains(appt);
    }

    @Order(5)
    @Test
    @DisplayName("Testing fetch on single appointment by slot")
    void testGetAppointmentsBySlot() {
        assertThat(impl.getAppointmentsBySlot(slot)).isNotEmpty().contains(appt);
    }

    @Order(6)
    @Test
    @DisplayName("Testing updating an aoopintment")
    void testUpdateAppointment() {
        Patient tempPatient = patient.toBuilder().name("Tom").build();
        assertThat(patientImpl.createPatient(tempPatient)).isEqualTo(tempPatient).isNotEqualTo(patient);

        Slot tempSlot = slot.toBuilder().capacity(1).build();
        assertThat(slotImpl.createSlot(tempSlot)).isEqualTo(tempSlot).isNotEqualTo(slot);

        Appointment temp = appt.toBuilder().patient(tempPatient).slot(tempSlot).build();

        assertThat(impl.updateAppointment(appt.getId(), temp)).isEqualTo(temp).isNotEqualTo(appt);
    }

    @Order(7)
    @Test
    @DisplayName("Testing delete on single appointment by id")
    void testDeleteAppointment() {
        impl.deleteAppointment(appt.getId());
        assertThat(impl.getAppointmentById(appt.getId())).isEmpty();
    }
}
