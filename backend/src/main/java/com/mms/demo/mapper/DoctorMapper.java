package com.mms.demo.mapper;

import org.mapstruct.Mapper;
import com.mms.demo.config.MapperConfiguration;
import com.mms.demo.entity.Doctor;
import com.mms.demo.transferobject.DoctorDTO;

@Mapper(config = MapperConfiguration.class)
public interface DoctorMapper {
    DoctorDTO entityToDto(Doctor doctor);

    Doctor dtoToEntity(DoctorDTO doctorDTO);
}
