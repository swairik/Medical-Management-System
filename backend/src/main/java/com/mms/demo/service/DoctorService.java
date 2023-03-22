package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;

/**
 * The Interface DoctorService defines all the interactions needed between a high level controller
 * and the corresponding repository
 * 
 * @author Mananveer Singh
 */
public interface DoctorService {
    /**
     * Gets all doctors.
     *
     * @return all the doctors
     */
    List<Doctor> getAllDoctors();

    /**
     * Gets the doctor by id.
     *
     * @param id the id
     * @return the doctor by id
     */
    Optional<Doctor> getDoctortById(Long id);

    /**
     * Gets all doctors by a shared speciality.
     *
     * @param speciality the speciality
     * @return the list of doctors by speciality
     */
    List<Doctor> getDoctorBySpeciality(Speciality speciality);

    /**
     * Creates the doctor.
     *
     * @param doctor the doctor
     * @return the doctor
     */
    Doctor createDoctor(Doctor doctor);

    /**
     * Update doctor.
     *
     * @param id the id
     * @param doctor the doctor
     * @return the doctor
     */
    Doctor updateDoctor(Long id, Doctor doctor);

    /**
     * Delete doctor.
     *
     * @param id the id
     */
    void deleteDoctor(Long id);
}
