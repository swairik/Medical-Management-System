package com.mms.demo.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Prescription;
import com.mms.demo.repository.PrescriptionRepository;
import com.mms.demo.service.PrescriptionService;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    @Autowired
    PrescriptionRepository repository;

    @Override
    public Prescription createPrescription(Prescription prescription) {
        return repository.save(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        repository.deleteById(id);
        
    }

    @Override
    public List<Prescription> getAllPrescriptionsByStampBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findAllByStampBetween(start.toLocalDate(), end.toLocalDate());
    }

    @Override
    public Optional<Prescription> getPrescriptionById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Prescription> getPrescriptionsByStamp(LocalDateTime stamp) {
        return repository.findAllByStamp(stamp.toLocalDate());
    }

    @Override
    public Optional<Prescription> updatePrescription(Long id, Prescription update) {
        Optional<Prescription> temp = getPrescriptionById(id);
        if (temp.isEmpty()) {
            return Optional.empty();
        }

        Prescription prescription = temp.get();
        prescription.setContents(update.getContents());
        prescription.setDoctor(update.getDoctor());
        prescription.setPatient(update.getPatient());
        prescription.setStamp(LocalDate.now());

        return Optional.of(repository.save(prescription));
    }
    
}
