package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Slot;

/**
 * SlotRepository defines an interface to generate JPA defined queries to interact with the Slot
 * table.
 * 
 * @author Mananveer Singh
 */
public interface SlotRepository extends JpaRepository<Slot, Long> {

}
