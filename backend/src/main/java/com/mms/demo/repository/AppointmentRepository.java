package com.mms.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByPatient(Patient patient);
    List<Appointment> findAllBySlot(Slot slot);
}
