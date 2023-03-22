package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;

public interface DoctorService {
    List<Doctor> getAllDoctors();

    Optional<Doctor> getDoctortById(Long id);

    List<Doctor> getDoctorBySpeciality(Speciality speciality);

    Doctor createDoctor(Doctor doctor);

    Doctor updateDoctor(Long id, Doctor doctor);

    void deleteDoctor(Long id);
}
