package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Speciality;

/**
 * SpecialityRepository defines an interface to generate JPA defined queries to interact with the
 * Speciality table.
 * 
 * @author Mananveer Singh
 */
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {

}
