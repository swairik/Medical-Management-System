package com.mms.demo.mapperimpl;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.mms.demo.entity.Report;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.transferobject.ReportDTO;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ReportMapperImplTest {
    @Autowired
    private DataTransferObjectMapper<Report, ReportDTO> mapper;

    @Test
    void testEntityToDto() {
        Report report = Report.builder().contents("content".getBytes()).stamp(LocalDate.now())
                        .build();

        assertThat(mapper.entityToDto(report)).isNotNull();
    }
}
