package com.mms.demo.mapperimpl;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Schedule;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.ScheduleDTO;
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
        scheduleDTO.booked(schedule.getBooked());

        return scheduleDTO.build();
    }

    @Override
    public Schedule dtoToEntity(ScheduleDTO scheduleDTO) throws IllegalArgumentException {
        if (scheduleDTO == null) {
            return null;
        }

        Schedule.ScheduleBuilder schedule = Schedule.builder();

        try {
            schedule.approvalStatus(scheduleDTO.getApprovalStatus()).booked(scheduleDTO.getBooked())
                            .doctor(doctorMapper.dtoToEntity(scheduleDTO.getDoctor()))
                            .end(scheduleDTO.getEnd()).id(scheduleDTO.getId())
                            .start(scheduleDTO.getStart()).booked(scheduleDTO.getBooked());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                            "Expected required field in the DataTransferObject, found null", e);
        }

        return schedule.build();
    }
}
