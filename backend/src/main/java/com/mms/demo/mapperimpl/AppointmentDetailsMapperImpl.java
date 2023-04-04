package com.mms.demo.mapperimpl;

import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import org.springframework.stereotype.Component;

@Component
public class AppointmentDetailsMapperImpl
                implements DataTransferObjectMapper<AppointmentDetails, AppointmentDetailsDTO> {

    @Override
    public AppointmentDetailsDTO entityToDto(AppointmentDetails appointmentDetails) {
        if (appointmentDetails == null) {
            return null;
        }

        AppointmentDetailsDTO.AppointmentDetailsDTOBuilder appointmentDetailsDTO =
                        AppointmentDetailsDTO.builder();

        appointmentDetailsDTO.id(appointmentDetails.getId());
        String prescription = "";
        if (appointmentDetails.getPrescription() != null) {
            prescription = new String(appointmentDetails.getPrescription());
        }
        appointmentDetailsDTO.prescription(prescription);

        String feedback = "";
        if (appointmentDetails.getFeedback() != null) {
            feedback = new String(appointmentDetails.getFeedback());
        }
        appointmentDetailsDTO.feedback(feedback);
        appointmentDetailsDTO.rating(appointmentDetails.getRating());

        return appointmentDetailsDTO.build();
    }

    @Override
    public AppointmentDetails dtoToEntity(AppointmentDetailsDTO appointmentDetailsDTO)
                    throws IllegalArgumentException {
        if (appointmentDetailsDTO == null) {
            return null;
        }

        AppointmentDetails.AppointmentDetailsBuilder appointmentDetails =
                        AppointmentDetails.builder();


        try {
            if (appointmentDetailsDTO.getFeedback() != null) {
                appointmentDetails.feedback(appointmentDetailsDTO.getFeedback().getBytes());
            }
            if (appointmentDetailsDTO.getPrescription() != null) {
                appointmentDetails.prescription(appointmentDetailsDTO.getPrescription().getBytes());
            }
            appointmentDetails.rating(appointmentDetailsDTO.getRating());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                            "Expected required field in the DataTransferObject, found null", e);
        }

        return appointmentDetails.build();
    }
}
