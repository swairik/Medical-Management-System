package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.repository.DoctorRepository;
import com.mms.demo.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository repository;
    
    @Override
    public List<Doctor> getAllDoctors() {
        return repository.findAll();
    }

    @Override
    public List<Doctor> getDoctorBySpeciality(Speciality speciality) {
        return repository.findAllBySpeciality(speciality);
    }

    @Override
    public Optional<Doctor> getDoctortById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return repository.save(doctor);
    }
    
    @Override
    public Doctor updateDoctor(Long id, Doctor doctorUpdates) {
        Optional<Doctor> temp = repository.findById(id);
        if (temp.isEmpty()) {
            return null;
        }

        Doctor doctor = temp.get();
        doctor.setAge(doctorUpdates.getAge());
        doctor.setEmail(doctorUpdates.getEmail());
        doctor.setGender(doctorUpdates.getGender());
        doctor.setName(doctorUpdates.getName());
        doctor.setPhone(doctorUpdates.getPhone());
        doctor.setSpeciality(doctorUpdates.getSpeciality());

        return repository.save(doctor);
    }
    
    @Override
    public void deleteDoctor(Long id) {
        repository.deleteById(id);
    }
}
