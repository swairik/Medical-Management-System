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
    public Optional<AppointmentDetailsDTO> update(Long id, AppointmentDetailsDTO updates) {

        Optional<AppointmentDetails> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        AppointmentDetails appointmentDetails = fetchedContainer.get();

        appointmentDetails.setFeedback(updates.getFeedback().getBytes());
        appointmentDetails.setPrescription(updates.getPrescription().getBytes());
        appointmentDetails = repository.save(appointmentDetails);

        return Optional.of(mapper.entityToDto(appointmentDetails));
    }

}
