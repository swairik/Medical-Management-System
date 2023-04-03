package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Patient;
import com.mms.demo.mapper.DataTransferObjectMapper;

import com.mms.demo.repository.PatientRepository;
import com.mms.demo.service.PatientService;
import com.mms.demo.transferobject.PatientDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository repository;

    @Autowired
    private DataTransferObjectMapper<Patient, PatientDTO> mapper;

    @Override
    public List<PatientDTO> getAll() {
        return repository.findAll().stream().map(p -> mapper.entityToDto(p))
                        .collect(Collectors.toList());

    }

    @Override
    public Optional<PatientDTO> get(Long id) {
        Optional<Patient> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.entityToDto(fetchedContainer.get()));
    }

    @Override
    public PatientDTO create(PatientDTO patientDTO) throws IllegalArgumentException {
        Patient patient;
        try {
            patient = mapper.dtoToEntity(patientDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to parse entity from data transfer object",
                            e);
        }
        patient = repository.save(patient);

        return mapper.entityToDto(patient);
    }

    @Override
    public Optional<PatientDTO> update(Long id, PatientDTO patientDTO)
                    throws IllegalArgumentException {
        Optional<Patient> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("No patient with this id exists");
        }
        Patient patientUpdates;
        try {
            patientUpdates = mapper.dtoToEntity(patientDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to parse entity from data transfer object",
                            e);
        }
        Patient patient = fetchedContainer.get();
        patient.setAge(patientUpdates.getAge());
        patient.setGender(patientUpdates.getGender());
        patient.setName(patientUpdates.getName());
        patient.setPhone(patientUpdates.getPhone());

        patient = repository.save(patient);
        return Optional.of(mapper.entityToDto(patient));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
