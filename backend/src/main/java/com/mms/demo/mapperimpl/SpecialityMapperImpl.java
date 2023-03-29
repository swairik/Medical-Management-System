package com.mms.demo.mapperimpl;

import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.SpecialityDTO;
import org.springframework.stereotype.Component;

@Component
public class SpecialityMapperImpl implements DataTransferObjectMapper<Speciality, SpecialityDTO> {

    @Override
    public SpecialityDTO entityToDto(Speciality speciality) {
        if (speciality == null) {
            return null;
        }

        SpecialityDTO.SpecialityDTOBuilder specialityDTO = SpecialityDTO.builder();

        specialityDTO.id(speciality.getId());
        specialityDTO.name(speciality.getName());

        return specialityDTO.build();
    }

    @Override
    public Speciality dtoToEntity(SpecialityDTO specialityDTO) {
        if (specialityDTO == null) {
            return null;
        }

        Speciality.SpecialityBuilder speciality =
                        Speciality.builder().id(specialityDTO.id()).name(specialityDTO.name());

        return speciality.build();
    }
}
