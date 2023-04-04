package com.mms.demo.mapperimpl;

import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import com.mms.demo.entity.Report;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.ReportDTO;

@Component
public class ReportMapperImpl implements DataTransferObjectMapper<Report, ReportDTO> {

    @Override
    public Report dtoToEntity(ReportDTO dataTransferObject) {

        return null;
    }

    @Override
    public ReportDTO entityToDto(Report entity) throws IllegalArgumentException {
        if (entity == null) {
            return null;
        }


        ReportDTO.ReportDTOBuilder report = ReportDTO.builder();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d_MMM_uuuu");
        String filename = String.format("Report_For_%s", entity.getStamp().format(formatter));
        try {
            report.contentLength(entity.getContents().length).contents(entity.getContents())
                            .filename(filename).id(entity.getId()).build();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                            "Expected required field in the DataTransferObject, found null", e);
        }

        return report.build();
    }

}
