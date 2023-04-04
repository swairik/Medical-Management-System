package com.mms.demo.serviceImpl;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.repository.AppointmentDetailsRepository;
import com.mms.demo.repository.AppointmentRepository;
import com.mms.demo.repository.DoctorRepository;
import com.mms.demo.service.AppointmentDetailsService;
import com.mms.demo.transferobject.AppointmentDetailsDTO;

@Service
public class AppointmentDetailsServiceImpl implements AppointmentDetailsService {

    @Autowired
    AppointmentDetailsRepository repository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DoctorRepository doctorRepository;

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

        if (fetchedContainer.get().getRating() != null) {
            Optional<Appointment> fetchedAppointmentContainer =
                            appointmentRepository.findByAppointmentDetails(fetchedContainer.get());
            if (fetchedAppointmentContainer.isEmpty()) {
                throw new IllegalArgumentException(
                                "No parent appointment with the given details found");
            }

            Optional<Doctor> fetchedDoctorContainer = doctorRepository
                            .findById(fetchedAppointmentContainer.get().getDoctor().getId());
            if (fetchedDoctorContainer.isEmpty()) {
                throw new IllegalArgumentException(
                                "Unexpected Error: No doctor associated with related appointment found. Please contact the admin");
            }
            Doctor doctor = fetchedDoctorContainer.get();

            Double adjustedRating = doctor.getRatingAverage() * doctor.getRatingCount();
            adjustedRating += fetchedContainer.get().getRating();
            doctor.setRatingCount(doctor.getRatingCount() + 1);
            doctor.setRatingAverage(adjustedRating / doctor.getRatingCount());

            doctorRepository.save(doctor);
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
