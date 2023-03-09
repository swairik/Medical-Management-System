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
    private DoctorRepository doctorRepo;
    
    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    @Override
    public List<Doctor> getDoctorBySpeciality(Speciality speciality) {
        return doctorRepo.findAllBySpeciality(speciality);
    }

    @Override
    public Optional<Doctor> getDoctortById(Long id) {
        return doctorRepo.findById(id);
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepo.save(doctor);
    }
    
    @Override
    public Doctor updateDoctor(Long id, Doctor doctorUpdates) {
        Optional<Doctor> temp = doctorRepo.findById(id);
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

        return doctorRepo.save(doctor);
    }
    
    @Override
    public void deleteDoctor(Long id) {
        doctorRepo.deleteById(id);
    }
}
