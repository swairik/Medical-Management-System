package com.mms.demo.mapperimpl;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.AppointmentDTO;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapperImpl
                implements DataTransferObjectMapper<Appointment, AppointmentDTO> {

    @Autowired
    DataTransferObjectMapper<AppointmentDetails, AppointmentDetailsDTO> appointmentDetailsMapper;

    @Autowired
    DataTransferObjectMapper<Patient, PatientDTO> patientMapper;

    @Autowired
    DataTransferObjectMapper<Doctor, DoctorDTO> doctorMapper;

    @Override
    public AppointmentDTO entityToDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentDTO.AppointmentDTOBuilder appointmentDTO = AppointmentDTO.builder();

        appointmentDTO.id(appointment.getId());
        appointmentDTO.patient(patientMapper.entityToDto(appointment.getPatient()));
        appointmentDTO.doctor(doctorMapper.entityToDto(appointment.getDoctor()));
        appointmentDTO.start(appointment.getStart());
        appointmentDTO.appointmentDetails(
                        appointmentDetailsMapper.entityToDto(appointment.getAppointmentDetails()));
        appointmentDTO.attended(appointment.getAttended());

        return appointmentDTO.build();
    }

    @Override
    public Appointment dtoToEntity(AppointmentDTO appointmentDTO) throws IllegalArgumentException {
        if (appointmentDTO == null) {
            return null;
        }

        Appointment.AppointmentBuilder appointment = Appointment.builder();

        try {
            appointment.appointmentDetails(appointmentDetailsMapper
                            .dtoToEntity(appointmentDTO.getAppointmentDetails()))
                            .attended(appointmentDTO.getAttended())
                            .doctor(doctorMapper.dtoToEntity(appointmentDTO.getDoctor()))
                            .id(appointmentDTO.getId())
                            .patient(patientMapper.dtoToEntity(appointmentDTO.getPatient()))
                            .start(appointmentDTO.getStart());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                            "Expected required field in the DataTransferObject, found null", e);
        }

        return appointment.build();
    }

}
