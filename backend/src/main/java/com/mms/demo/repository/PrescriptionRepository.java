package com.mms.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Prescription;

/**
 * PrescriptionRepository defines an interface to generate JPA defined queries to interact with the
 * Prescription table.
 * 
 * @author Mananveer Singh
 */
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

}
