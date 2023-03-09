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
    private PatientRepository patientRepo;

    @Override
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return patientRepo.findById(id);
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepo.save(patient);
    }

    @Override
    public Patient updatePatient(Long id, Patient patientUpdates) {
        Optional<Patient> temp = patientRepo.findById(id);
        if (temp.isEmpty()) {
            return null;
        }

        Patient patient = temp.get();
        patient.setAge(patientUpdates.getAge());
        patient.setEmail(patientUpdates.getEmail());
        patient.setGender(patientUpdates.getGender());
        patient.setName(patientUpdates.getName());
        patient.setPhone(patientUpdates.getPhone());

        return patientRepo.save(patient);
    }

    @Override
    public void deletePatient(Long id) {
        patientRepo.deleteById(id);
    }


}
