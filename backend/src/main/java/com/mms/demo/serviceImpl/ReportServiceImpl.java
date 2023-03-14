package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.*;
import com.mms.demo.repository.*;
import com.mms.demo.service.*;

@Service
public class ReportServiceImpl implements ReportService{
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    @Override
    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    public List<Report> getReportByStamp(LocalDateTime stamp) {
        return reportRepository.findAllByStamp(stamp.toLocalDate());
    }


    @Override
    public Report updateReport(Long id, Report reportUpdates) {
        Optional<Report> temp = getReportById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Report report = temp.get();
        report.setReportText(reportUpdates.getReportText());
        report.setStamp(reportUpdates.getStamp());

        return reportRepository.save(report);
    }

    public byte[] XSSFWorkbooktoByteArray(XSSFWorkbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        outputStream.close();

        return outputStream.toByteArray();
    }

    public XSSFWorkbook ByteArrayToXSSFWorkbook(byte[] byteArray) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        inputStream.close();

        return workbook;
    }

    private byte[] reportListToZipByteArray(List<Report> reports) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
        final String fileExtension = ".xlsx";
        for (Report report : reports) {
            final String reportName = "Report-" + report.getStamp().format(formatter) + fileExtension;
            ZipEntry entry = new ZipEntry(reportName);
            entry.setSize(report.getReportText().length);
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(report.getReportText());
        }


        zipOutputStream.closeEntry();
        zipOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public List<Report> getAllReportsByStampBetween(LocalDateTime start, LocalDateTime end) {
        return reportRepository.findAllByStampBetween(start.toLocalDate(), end.toLocalDate());
    }

    @Override
    public Optional<byte[]> generateReports(LocalDateTime from, LocalDateTime to) {
        List<Report> reports = getAllReportsByStampBetween(from, to);
        if (reports.isEmpty()) {
            return Optional.empty();
        }

        byte[] reportsZipByteArray = null;
        try {
            reportsZipByteArray = reportListToZipByteArray(reports);
        } catch (IOException e) {
            System.out.println(e);
        }

        return Optional.ofNullable(reportsZipByteArray);
    }

    @Scheduled(cron = "${report.gen.interval}")
    public void generateAndClean() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        final LocalDateTime temporalTarget = LocalDateTime.now().minusDays(1);
        XSSFRow titlesRow = null;
        ArrayList<String> titles = null;
        
        // Append list of all new patients
        XSSFSheet patientsSheet = workbook.createSheet("New Patients");
        titlesRow = patientsSheet.createRow(0);
        titles = new ArrayList<>(Arrays.asList("ID", "Name", "Gender", "Age", "Email", "Phone Number"));
        for (String title : titles) {
            titlesRow.createCell(titlesRow.getLastCellNum()).setCellValue(title);
        }
        
        List<Patient> patients = patientRepository.findAllByStampBetween(temporalTarget, temporalTarget);
        for (Patient patient : patients) {
            XSSFRow currentPatient = patientsSheet.createRow(patientsSheet.getLastRowNum());
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(patient.getId());
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(patient.getName());
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(patient.getGender());
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(patient.getAge());
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(patient.getEmail());
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(patient.getPhone());
        }

        // Append list of all new doctors
        XSSFSheet doctorsSheet = workbook.createSheet("New Doctors");
        titlesRow = doctorsSheet.createRow(0);
        titles.add("Speciality");
        for (String title : titles) {
            titlesRow.createCell(titlesRow.getLastCellNum()).setCellValue(title);
        }
        
        List<Doctor> doctors = doctorRepository.findAllByStampBetween(temporalTarget, temporalTarget);
        for (Doctor doctor : doctors) {
            XSSFRow currentDoctor = doctorsSheet.createRow(doctorsSheet.getLastRowNum());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(doctor.getId());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(doctor.getName());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(doctor.getGender());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(doctor.getAge());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(doctor.getEmail());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(doctor.getPhone());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(doctor.getSpeciality().getName());
        }

        // Create a sheet for each doctor and append their schedules, appointments, and patients
        List<Doctor> allDoctors = doctorRepository.findAll();
        for (Doctor doctor : allDoctors) {
            XSSFSheet currentDoctorMetaSheet = workbook.createSheet(doctor.getId().toString());
            XSSFRow currentRow = currentDoctorMetaSheet.createRow(0);
            currentRow.createCell(0).setCellValue("Name");
            currentRow.createCell(1).setCellValue(doctor.getName());

            currentDoctorMetaSheet.createRow(1);

            List<Schedule> schedules = scheduleRepository.findAllByDoctorAndWeek(doctor, temporalTarget.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
            for (Schedule schedule : schedules) {
                if (schedule.getSlot().getWeekday() != temporalTarget.getDayOfWeek()) {
                    continue;
                }
                
                currentRow = currentDoctorMetaSheet.createRow(currentDoctorMetaSheet.getLastRowNum());
                currentRow.createCell(0).setCellValue("Slot Begin");
                currentRow.createCell(1).setCellValue(schedule.getSlot().getStart().toString());

                currentRow = currentDoctorMetaSheet.createRow(currentDoctorMetaSheet.getLastRowNum());
                currentRow.createCell(0).setCellValue("Slot End");
                currentRow.createCell(1).setCellValue(schedule.getSlot().getStart().toString());

                currentRow = currentDoctorMetaSheet.createRow(currentDoctorMetaSheet.getLastRowNum());  // blank

                List<Appointment> appointments = appointmentRepository.findAllBySlot(schedule.getSlot());
                titlesRow = currentDoctorMetaSheet.createRow(currentDoctorMetaSheet.getLastRowNum());
                titles = new ArrayList<>(Arrays.asList("ID", "Patient ID", "Patient Name", "Scheduled On", "Attended"));
                for (String title : titles) {
                    titlesRow.createCell(titlesRow.getLastCellNum()).setCellValue(title);
                }
                for (Appointment appointment : appointments) {
                    currentRow = currentDoctorMetaSheet.createRow(currentDoctorMetaSheet.getLastRowNum());
                    currentRow.createCell(currentRow.getLastCellNum()).setCellValue(appointment.getId());
                    currentRow.createCell(currentRow.getLastCellNum()).setCellValue(appointment.getPatient().getId());
                    currentRow.createCell(currentRow.getLastCellNum()).setCellValue(appointment.getPatient().getName());
                    currentRow.createCell(currentRow.getLastCellNum()).setCellValue(appointment.getStamp().toString());
                    currentRow.createCell(currentRow.getLastCellNum()).setCellValue(appointment.getAttended());
                }
                currentRow = currentDoctorMetaSheet.createRow(currentDoctorMetaSheet.getLastRowNum());  // blank
            }

        }
    }
}
