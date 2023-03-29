package com.mms.demo.mapperimpl;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.SpecialityDTO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapperImpl implements DataTransferObjectMapper<Doctor, DoctorDTO> {
    @Autowired
    DataTransferObjectMapper<Speciality, SpecialityDTO> specialityMapper;

    @Override
    public DoctorDTO entityToDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        DoctorDTO.DoctorDTOBuilder doctorDTO = DoctorDTO.builder();

        doctorDTO.id(doctor.getId());
        doctorDTO.name(doctor.getName());
        doctorDTO.gender(doctor.getGender());
        doctorDTO.age(doctor.getAge());
        doctorDTO.email(doctor.getEmail());
        doctorDTO.phone(doctor.getPhone());
        doctorDTO.speciality(specialityMapper.entityToDto(doctor.getSpeciality()));

        return doctorDTO.build();
    }

    @Override
    public Doctor dtoToEntity(DoctorDTO doctorDTO) {
        if (doctorDTO == null) {
            return null;
        }

        Doctor.DoctorBuilder doctor = Doctor.builder().age(doctorDTO.age()).email(doctorDTO.email())
                        .gender(doctorDTO.gender()).id(doctorDTO.id()).name(doctorDTO.name())
                        .phone(doctorDTO.phone())
                        .speciality(specialityMapper.dtoToEntity(doctorDTO.speciality()));

        return doctor.build();
    }
}
