package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllBySpeciality(Speciality speciality);
}
