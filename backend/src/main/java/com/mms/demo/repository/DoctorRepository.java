package com.mms.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllBySpeciality(Speciality speciality);

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findAllByStampBetween(LocalDateTime start, LocalDateTime end);
}
