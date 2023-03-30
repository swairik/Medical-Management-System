package com.mms.demo.mapperimpl;

import java.time.format.DateTimeFormatter;
import com.mms.demo.entity.Report;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.ReportDTO;

public class ReportMapperImpl implements DataTransferObjectMapper<Report, ReportDTO> {

    @Override
    public Report dtoToEntity(ReportDTO dataTransferObject) {

        return null;
    }

    @Override
    public ReportDTO entityToDto(Report entity) {
        if (entity == null) {
            return null;
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d_MMM_uuuu");
        String filename = String.format("Report_For_%s", entity.getStamp().format(formatter));
        return ReportDTO.builder().contentLength(entity.getContents().length)
                        .contents(entity.getContents()).filename(filename).id(entity.getId())
                        .build();
    }

}
