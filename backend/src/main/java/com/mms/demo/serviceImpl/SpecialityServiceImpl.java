package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.repository.SpecialityRepository;
import com.mms.demo.service.SpecialityService;
import com.mms.demo.transferobject.SpecialityDTO;

@Service
public class SpecialityServiceImpl implements SpecialityService {
    @Autowired
    private SpecialityRepository repository;

    @Autowired
    DataTransferObjectMapper<Speciality, SpecialityDTO> mapper;

    @Override
    public SpecialityDTO create(SpecialityDTO specialityDTO) {
        Speciality speciality = mapper.dtoToEntity(specialityDTO);
        speciality = repository.save(speciality);

        return mapper.entityToDto(speciality);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);

    }

    @Override
    public Optional<SpecialityDTO> get(Long id) {
        Optional<Speciality> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapper.entityToDto(fetchedContainer.get()));
    }

    @Override
    public List<SpecialityDTO> getAll() {
        return repository.findAll().stream().map(s -> mapper.entityToDto(s))
                        .collect(Collectors.toList());
    }

    @Override
    public Optional<SpecialityDTO> update(Long id, SpecialityDTO specialityUpdates)
                    throws IllegalArgumentException {
        Optional<Speciality> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced speciality does not exist");
        }

        Speciality speciality = fetchedContainer.get();
        speciality.setName(specialityUpdates.getName());
        speciality = repository.save(speciality);

        return Optional.of(mapper.entityToDto(speciality));
    }

}
