package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Speciality;
import com.mms.demo.repository.SpecialityRepository;
import com.mms.demo.service.SpecialityService;

@Service
public class SpecialityServiceImpl implements SpecialityService {
    @Autowired
    private SpecialityRepository repository;

    @Override
    public Speciality createSpeciality(Speciality speciality) {
        return repository.save(speciality);
    }

    @Override
    public void deleteSpeciality(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Speciality> getAllSpecialities() {
        return repository.findAll();
    }

    @Override
    public Optional<Speciality> getSpecialityById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Speciality updateSpeciality(long id, Speciality specialityUpdates) {
        Optional<Speciality> temp = getSpecialityById(id);
        if (temp.isEmpty()) {
            return null;
        }

        Speciality speciality = temp.get();
        speciality.setName(specialityUpdates.getName());
        return repository.save(speciality);
    }
}
