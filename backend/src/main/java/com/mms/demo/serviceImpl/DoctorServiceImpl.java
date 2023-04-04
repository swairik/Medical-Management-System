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
    public List<DoctorDTO> getAllBySpeciality(Long specialityID) throws IllegalArgumentException {
        Optional<Speciality> fetchedContainer = specialityRepository.findById(specialityID);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced speciality does not exist");
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
    public DoctorDTO create(DoctorDTO doctorDTO, Long specialityID)
                    throws IllegalArgumentException {
        Optional<Speciality> fetchedContainer = specialityRepository.findById(specialityID);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced speciality does not exist");
        }
        Doctor doctor = mapper.dtoToEntity(doctorDTO).toBuilder().speciality(fetchedContainer.get())
                        .build();
        doctor = repository.save(doctor);

        return mapper.entityToDto(doctor);
    }

    @Override
    public Optional<DoctorDTO> update(Long id, DoctorDTO doctorDTO)
                    throws IllegalArgumentException {
        Optional<Doctor> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("No doctor with this id exists");
        }

        Doctor doctor = fetchedContainer.get();
        Doctor doctorUpdates;
        try {
            doctorUpdates = mapper.dtoToEntity(doctorDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to parse entity from data transfer object",
                            e);
        }
        doctor.setAge(doctorUpdates.getAge());
        doctor.setGender(doctorUpdates.getGender());
        doctor.setName(doctorUpdates.getName());
        doctor.setPhone(doctorUpdates.getPhone());

        doctor = repository.save(doctor);
        return Optional.of(mapper.entityToDto(doctor));
    }

    @Override
    public Optional<DoctorDTO> updateSpeciality(Long id, Long specialityID)
                    throws IllegalArgumentException {
        Optional<Doctor> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("No doctor with this id exists");
        }

        Optional<Speciality> fetchedSpecialityContainer =
                        specialityRepository.findById(specialityID);
        if (fetchedSpecialityContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced speciality does not exist");
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
