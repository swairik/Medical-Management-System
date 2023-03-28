package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Patient;
import com.mms.demo.mapper.PatientMapper;
import com.mms.demo.repository.PatientRepository;
import com.mms.demo.service.PatientService;
import com.mms.demo.transferobject.PatientDTO;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository repository;

    @Autowired
    private PatientMapper mapper;

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
    public PatientDTO create(PatientDTO patientDTO) {
        Patient patient = mapper.dtoToEntity(patientDTO);
        patient = repository.save(patient);

        return mapper.entityToDto(patient);
    }

    @Override
    public Optional<PatientDTO> update(Long id, PatientDTO patientUpdates) {
        Optional<Patient> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        Patient patient = fetchedContainer.get();
        patient.setAge(patientUpdates.age());
        patient.setEmail(patientUpdates.email());
        patient.setGender(patientUpdates.gender());
        patient.setName(patientUpdates.name());
        patient.setPhone(patientUpdates.phone());

        patient = repository.save(patient);
        return Optional.of(mapper.entityToDto(patient));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }


}
