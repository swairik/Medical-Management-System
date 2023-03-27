package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public List<Prescription> getAllPrescriptions() {
        return repository.findAll();
    }

    @Override
    public List<Prescription> getAllPrescriptionsByStampBetween(LocalDateTime start,
                    LocalDateTime end) {
        // return repository.findAllByStampBetween(start.truncatedTo(ChronoUnit.MINUTES),
        // end.truncatedTo(ChronoUnit.MINUTES));

        return null;
    }

    @Override
    public Optional<Prescription> getPrescriptionById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Prescription> getPrescriptionsByStamp(LocalDateTime stamp) {
        // return repository.findAllByStamp(stamp.truncatedTo(ChronoUnit.SECONDS));
        return null;
    }

    @Override
    public Optional<Prescription> updatePrescription(Long id, Prescription update) {
        Optional<Prescription> temp = getPrescriptionById(id);
        if (temp.isEmpty()) {
            return Optional.empty();
        }

        Prescription prescription = temp.get();
        prescription.setContents(update.getContents());
        // prescription.setDoctor(update.getDoctor());
        // prescription.setPatient(update.getPatient());
        // prescription.setStamp(LocalDateTime.now());

        return Optional.of(repository.save(prescription));
    }

}
