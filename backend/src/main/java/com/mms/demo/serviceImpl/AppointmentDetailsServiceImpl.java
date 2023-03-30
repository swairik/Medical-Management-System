package com.mms.demo.serviceImpl;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mms.demo.entity.AppointmentDetails;

import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.repository.AppointmentDetailsRepository;
import com.mms.demo.service.AppointmentDetailsService;
import com.mms.demo.transferobject.AppointmentDetailsDTO;

@Service
public class AppointmentDetailsServiceImpl implements AppointmentDetailsService {

    @Autowired
    AppointmentDetailsRepository repository;

    @Autowired
    DataTransferObjectMapper<AppointmentDetails, AppointmentDetailsDTO> mapper;

    @Override
    public Optional<AppointmentDetailsDTO> get(Long id) {
        Optional<AppointmentDetails> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapper.entityToDto(fetchedContainer.get()));
    }


    @Override
    public Optional<AppointmentDetailsDTO> update(Long id, AppointmentDetailsDTO updatesDto)
                    throws IllegalArgumentException {

        Optional<AppointmentDetails> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        AppointmentDetails updates;
        try {
            updates = mapper.dtoToEntity(updatesDto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to parse entity from data transfer object",
                            e);
        }

        AppointmentDetails appointmentDetails = fetchedContainer.get();

        appointmentDetails.setFeedback(updates.getFeedback());
        appointmentDetails.setPrescription(updates.getPrescription());
        appointmentDetails = repository.save(appointmentDetails);

        return Optional.of(mapper.entityToDto(appointmentDetails));
    }

}
