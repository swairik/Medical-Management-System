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

        AppointmentDetails appointmentDetails = fetchedContainer.get();

        if (updatesDto.getRating() != 0) {
            Optional<Appointment> fetchedAppointmentContainer =
                            appointmentRepository.findByAppointmentDetails(appointmentDetails);
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

            Long currentRating = appointmentDetails.getRating();
            System.out.println(currentRating);
            Double adjustedRating = doctor.getRatingAverage() * doctor.getRatingCount();
            adjustedRating += updatesDto.getRating() - currentRating;
            System.out.println(adjustedRating);
            Long ratingCount = doctor.getRatingCount();
            if (currentRating == 0) {
                ratingCount += 1;
            }
            doctor.setRatingCount(ratingCount);
            if (ratingCount > 0) {
                doctor.setRatingAverage(adjustedRating / ratingCount);
            }
            

            doctorRepository.save(doctor);
        }


        AppointmentDetails updates;
        try {
            updates = mapper.dtoToEntity(updatesDto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to parse entity from data transfer object",
                            e);
        }

        appointmentDetails.setFeedback(updates.getFeedback());
        appointmentDetails.setPrescription(updates.getPrescription());
        if (updatesDto.getRating() > 0) {
            appointmentDetails.setRating(updatesDto.getRating());
        }
        
        appointmentDetails = repository.save(appointmentDetails);

        return Optional.of(mapper.entityToDto(appointmentDetails));
    }

}
