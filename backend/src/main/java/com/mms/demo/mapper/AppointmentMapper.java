package com.mms.demo.mapper;

import org.mapstruct.Mapper;
import com.mms.demo.config.MapperConfiguration;
import com.mms.demo.entity.Appointment;
import com.mms.demo.transferobject.AppointmentDTO;

@Mapper(config = MapperConfiguration.class)
public interface AppointmentMapper {
    AppointmentDTO entityToDto(Appointment appointment);

    Appointment dtoToEntity(AppointmentDTO appointmentDTO);

    default String map(byte[] value) {
        return new String(value);
    }
}
