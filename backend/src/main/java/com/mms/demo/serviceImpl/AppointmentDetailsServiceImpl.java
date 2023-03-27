package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.repository.AppointmentDetailsRepository;
import com.mms.demo.service.AppointmentDetailsService;

@Service
public class AppointmentDetailsServiceImpl implements AppointmentDetailsService {

    @Autowired
    AppointmentDetailsRepository repository;

    @Override
    public Optional<AppointmentDetails> create(AppointmentDetails appointmentDetails) {
        if (appointmentDetails == null) {
            return Optional.empty();
        }

        return Optional.of(repository.save(appointmentDetails));
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }

        repository.deleteById(id);
    }

    @Override
    public List<AppointmentDetails> getAllByPatient(Patient patient) {
        if (patient == null) {
            return null;
        }

        return repository.findAllByPatient(patient);
    }

    public List<AppointmentDetails> getAllByDoctor(Doctor doctor) {
        if(doctor == null) {
            return null;
        }
        return repository.findAllByDoctor(doctor);
    }

    @Override
    public List<AppointmentDetails> getAllByPatientAndDoctor(Patient patient, Doctor doctor) {
        if (patient == null || doctor == null) {
            return null;
        }

        return repository.findAllByPatientAndDoctor(patient, doctor);
    }

    @Override
    public Optional<AppointmentDetails> getById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return repository.findById(id);
    }

    @Override
    public Optional<AppointmentDetails> getByPatientAndStamp(Patient patient, LocalDateTime stamp) {
        if (patient == null || stamp == null) {
            return null;
        }

        return repository.findByPatientAndStamp(patient, stamp);
    }

    @Override
    public Optional<AppointmentDetails> update(Long id, AppointmentDetails updates) {
        if (id == null || updates == null) {
            return Optional.empty();
        }

        Optional<AppointmentDetails> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        AppointmentDetails appointmentDetails = fetchedContainer.get();
        appointmentDetails.setDoctor(updates.getDoctor());
        appointmentDetails.setFeedback(updates.getFeedback());
        appointmentDetails.setPatient(updates.getPatient());
        appointmentDetails.setPrescription(updates.getPrescription());
        appointmentDetails.setStamp(updates.getStamp());

        return Optional.of(repository.save(appointmentDetails));
    }

}
