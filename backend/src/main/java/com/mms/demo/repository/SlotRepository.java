package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Slot;

public interface SlotRepository extends JpaRepository<Slot, Long> {

}