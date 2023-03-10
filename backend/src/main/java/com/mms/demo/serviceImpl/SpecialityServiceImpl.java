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
    private SpecialityRepository repo;

    @Override
    public Speciality createSpeciality(Speciality speciality) {
        return repo.save(speciality);
    }

    @Override
    public void deleteSpeciality(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Speciality> getAllSpecialities() {
        return repo.findAll();
    }

    @Override
    public Optional<Speciality> getSpecialityById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Speciality updateSpeciality(long id, Speciality specialityUpdates) {
        Optional<Speciality> temp = getSpecialityById(id);
        if (temp.isEmpty()) {
            return null;
        }

        Speciality speciality = temp.get();
        speciality.setName(specialityUpdates.getName());
        return repo.save(speciality);
    }
}
