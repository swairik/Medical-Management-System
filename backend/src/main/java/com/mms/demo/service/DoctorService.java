package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.SpecialityDTO;

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
    List<DoctorDTO> getAll();

    /**
     * Gets the doctor by id.
     *
     * @param id the id
     * @return the doctor by id
     */
    Optional<DoctorDTO> get(Long id);

    /**
     * Gets all doctors by a shared speciality.
     *
     * @param speciality the speciality
     * @return the list of doctors by speciality
     */
    List<DoctorDTO> getAllBySpeciality(Long specialityID);

    /**
     * Creates the doctor.
     *
     * @param doctor the doctor
     * @return the doctor
     */
    DoctorDTO create(DoctorDTO doctorDTO);

    /**
     * Update doctor details.
     *
     * @param id the id
     * @param doctor the doctor
     * @return the doctor
     */
    Optional<DoctorDTO> update(Long id, DoctorDTO doctorUpdates);

    /**
     * Update doctor speciality.
     *
     * @param id the id
     * @param doctor the doctor
     * @return the doctor
     */
    Optional<DoctorDTO> updateSpeciality(Long id, Long specialityID);

    /**
     * Delete doctor.
     *
     * @param id the id
     */
    void delete(Long id);
}
