package com.mms.demo.mapperimpl;

import com.mms.demo.entity.Patient;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.PatientDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Component
public class PatientMapperImpl implements DataTransferObjectMapper<Patient, PatientDTO> {

    @Override
    public PatientDTO entityToDto(Patient patient) {
        if (patient == null) {
            return null;
        }

        PatientDTO.PatientDTOBuilder patientDTO = PatientDTO.builder();

        patientDTO.id(patient.getId());
        patientDTO.name(patient.getName());
        patientDTO.gender(patient.getGender());
        patientDTO.age(patient.getAge());
        patientDTO.email(patient.getEmail());
        patientDTO.phone(patient.getPhone());

        return patientDTO.build();
    }

    @Override
    public Patient dtoToEntity(PatientDTO patientDTO) throws IllegalArgumentException {
        if (patientDTO == null) {
            return null;
        }

        Patient.PatientBuilder patient = Patient.builder();

        try {
            patient.age(patientDTO.getAge()).email(patientDTO.getEmail())
                            .gender(patientDTO.getGender()).id(patientDTO.getId())
                            .name(patientDTO.getName()).phone(patientDTO.getPhone());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                            "Expected required field in the DataTransferObject, found null", e);
        }
        return patient.build();
    }
}
