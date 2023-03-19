package com.mms.demo.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Prescription;

public interface PrescriptionService {
    Optional<Prescription> getPrescriptionById(Long id);
    List<Prescription> getPrescriptionsByStamp(LocalDateTime stamp);
    List<Prescription> getAllPrescriptionsByStampBetween(LocalDateTime start, LocalDateTime end);
    List<Prescription> getAllPrescriptions();

    Prescription createPrescription(Prescription prescription);
    void deletePrescription(Long id);
    Optional<Prescription> updatePrescription(Long id, Prescription update);
}
