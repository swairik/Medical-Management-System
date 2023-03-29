package com.mms.demo.mapper;

import com.mms.demo.config.MapperConfiguration;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface AppointmentDetailsMapper {
    AppointmentDetailsDTO entityToDto(AppointmentDetails appointmentDetails);

    AppointmentDetails dtoToEntity(AppointmentDetailsDTO appointmentDetailsDTO);

    default String map(byte[] value) {
        return new String(value);
    }
}
