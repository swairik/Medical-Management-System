package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
