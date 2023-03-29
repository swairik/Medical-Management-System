package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Doctor;

import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.repository.DoctorRepository;
import com.mms.demo.repository.SpecialityRepository;
import com.mms.demo.service.DoctorService;
import com.mms.demo.transferobject.DoctorDTO;
import java.util.Collections;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository repository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private DataTransferObjectMapper<Doctor, DoctorDTO> mapper;

    @Override
    public List<DoctorDTO> getAll() {
        return repository.findAll().stream().map(d -> mapper.entityToDto(d))
                        .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDTO> getAllBySpeciality(Long specialityID) {
        Optional<Speciality> fetchedContainer = specialityRepository.findById(specialityID);
        if (fetchedContainer.isEmpty()) {
            return Collections.emptyList();
        }
        Speciality speciality = fetchedContainer.get();

        return repository.findAllBySpeciality(speciality).stream().map(d -> mapper.entityToDto(d))
                        .collect(Collectors.toList());
    }

    @Override
    public Optional<DoctorDTO> get(Long id) {
        Optional<Doctor> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapper.entityToDto(fetchedContainer.get()));
    }

    @Override
    public DoctorDTO create(DoctorDTO doctorDTO) {
        Doctor doctor = mapper.dtoToEntity(doctorDTO);
        doctor = repository.save(doctor);

        return mapper.entityToDto(doctor);
    }

    @Override
    public Optional<DoctorDTO> update(Long id, DoctorDTO doctorUpdates) {
        Optional<Doctor> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        Doctor doctor = fetchedContainer.get();
        doctor.setAge(doctorUpdates.age());
        doctor.setEmail(doctorUpdates.email());
        doctor.setGender(doctorUpdates.gender());
        doctor.setName(doctorUpdates.name());
        doctor.setPhone(doctorUpdates.phone());

        doctor = repository.save(doctor);
        return Optional.of(mapper.entityToDto(doctor));
    }

    @Override
    public Optional<DoctorDTO> updateSpeciality(Long id, Long specialityID) {
        Optional<Doctor> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        Optional<Speciality> fetchedSpecialityContainer =
                        specialityRepository.findById(specialityID);
        if (fetchedSpecialityContainer.isEmpty()) {
            return Optional.empty();
        }

        Doctor doctor = fetchedContainer.get();
        Speciality speciality = fetchedSpecialityContainer.get();

        doctor.setSpeciality(speciality);
        doctor = repository.save(doctor);

        return Optional.of(mapper.entityToDto(doctor));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
