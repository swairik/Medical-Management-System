package com.mms.demo.mapper;

import org.mapstruct.Mapper;
import com.mms.demo.config.MapperConfiguration;
import com.mms.demo.entity.Speciality;
import com.mms.demo.transferobject.SpecialityDTO;

@Mapper(config = MapperConfiguration.class)
public interface SpecialityMapper {
    SpecialityDTO entityToDto(Speciality speciality);

    Speciality dtoToEntity(SpecialityDTO specialityDTO);
}
