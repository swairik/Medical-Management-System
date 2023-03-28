package com.mms.demo.mapper;

import org.mapstruct.Mapper;
import com.mms.demo.config.MapperConfiguration;
import com.mms.demo.entity.Patient;
import com.mms.demo.transferobject.PatientDTO;

@Mapper(config = MapperConfiguration.class)
public interface PatientMapper {
    PatientDTO entityToDto(Patient patient);

    Patient dtoToEntity(PatientDTO patientDTO);
}
