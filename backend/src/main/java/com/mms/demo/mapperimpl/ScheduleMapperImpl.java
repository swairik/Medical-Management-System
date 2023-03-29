package com.mms.demo.mapperimpl;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.ScheduleDTO;
import com.mms.demo.transferobject.SpecialityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapperImpl implements DataTransferObjectMapper<Schedule, ScheduleDTO> {
    @Autowired
    DataTransferObjectMapper<Doctor, DoctorDTO> doctorMapper;

    @Override
    public ScheduleDTO entityToDto(Schedule schedule) {
        if (schedule == null) {
            return null;
        }

        ScheduleDTO.ScheduleDTOBuilder scheduleDTO = ScheduleDTO.builder();

        scheduleDTO.id(schedule.getId());
        scheduleDTO.doctor(doctorMapper.entityToDto(schedule.getDoctor()));
        scheduleDTO.start(schedule.getStart());
        scheduleDTO.end(schedule.getEnd());
        scheduleDTO.approvalStatus(schedule.getApprovalStatus());

        return scheduleDTO.build();
    }

    @Override
    public Schedule dtoToEntity(ScheduleDTO scheduleDTO) {
        if (scheduleDTO == null) {
            return null;
        }

        Schedule.ScheduleBuilder schedule = Schedule.builder()
                        .approvalStatus(scheduleDTO.approvalStatus()).booked(scheduleDTO.booked())
                        .doctor(doctorMapper.dtoToEntity(scheduleDTO.doctor()))
                        .end(scheduleDTO.end()).id(scheduleDTO.id()).start(scheduleDTO.start());

        return schedule.build();
    }
}
