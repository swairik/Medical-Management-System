package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.serviceImpl.ReportServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class POIReportServiceTest {
    private ReportServiceImpl reportService;


    void testByteArrayMethods() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet firstSheet = workbook.createSheet("Some Sheet");
        XSSFRow row = firstSheet.createRow(0);
        row.createCell(0).setCellValue("Jerry");

        byte[] byteArray = null;
        try {
            byteArray = reportService.XSSFWorkbooktoByteArray(workbook);
        } catch (IOException e) {
            System.out.println(e);
        }
        assertThat(byteArray).isNotNull();

        XSSFWorkbook test = null;
        try {
            test = reportService.ByteArrayToXSSFWorkbook(byteArray);
        } catch (IOException e) {
            System.out.println(e);
        }
        assertThat(test).isNotNull();

        File file = new File("workbook.xlsx");
        try (OutputStream fileOut = new FileOutputStream(file)) {
            test.write(fileOut);
        } catch (IOException e) {
            System.out.println("Empty" + e);
        }
        assertThat(file).exists();
        System.out.println(file.getAbsolutePath());
        file.deleteOnExit();
    }

}
