package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Speciality;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    
}
