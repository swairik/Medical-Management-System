package com.mms.demo.mapper;

import org.mapstruct.Mapper;
import com.mms.demo.config.MapperConfiguration;
import com.mms.demo.entity.Schedule;
import com.mms.demo.transferobject.ScheduleDTO;

@Mapper(config = MapperConfiguration.class)
public interface ScheduleMapper {
    ScheduleDTO entityToDto(Schedule schedule);

    Schedule dtoToEntity(ScheduleDTO scheduleDTO);
}
