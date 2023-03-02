package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Speciality;

public interface SpecialityService {
    Optional<Speciality> getSpecialityById(Long id);
    List<Speciality> getAllSpecialities();

    Speciality createSpeciality(Speciality speciality);
    void deleteSpeciality(Long id);
    Speciality updateSpeciality(long id, Speciality specialityUpdates);
}
