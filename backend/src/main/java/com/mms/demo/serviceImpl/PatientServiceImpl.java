package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Patient;
import com.mms.demo.repository.PatientRepository;
import com.mms.demo.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository repository;

    @Override
    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Patient createPatient(Patient patient) {
        return repository.save(patient);
    }

    @Override
    public Patient updatePatient(Long id, Patient patientUpdates) {
        Optional<Patient> temp = repository.findById(id);
        if (temp.isEmpty()) {
            return null;
        }

        Patient patient = temp.get();
        patient.setAge(patientUpdates.getAge());
        patient.setEmail(patientUpdates.getEmail());
        patient.setGender(patientUpdates.getGender());
        patient.setName(patientUpdates.getName());
        patient.setPhone(patientUpdates.getPhone());

        return repository.save(patient);
    }

    @Override
    public void deletePatient(Long id) {
        repository.deleteById(id);
    }


}
