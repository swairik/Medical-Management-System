package com.mms.demo.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;
import com.mms.demo.entity.Schedule;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.repository.AppointmentRepository;
import com.mms.demo.repository.DoctorRepository;
import com.mms.demo.repository.PatientRepository;
import com.mms.demo.repository.ReportRepository;
import com.mms.demo.repository.ScheduleRepository;
import com.mms.demo.service.ReportService;
import com.mms.demo.transferobject.ReportDTO;

@Service
public class ReportServiceImpl implements ReportService {
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

    @Autowired
    private DataTransferObjectMapper<Report, ReportDTO> mapper;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d_MMM_uuuu");


    @Override
    public Optional<ReportDTO> get(Long id) {
        Optional<Report> fetchedContainer = reportRepository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapper.entityToDto(fetchedContainer.get()));
    }

    @Override
    public Optional<ReportDTO> getByDay(LocalDateTime stamp) {
        Optional<Report> fetchedContainer = reportRepository.findByStamp(stamp.toLocalDate());
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        Report report = fetchedContainer.get();
        ReportDTO reportDTO = mapper.entityToDto(report);

        return Optional.of(
                        reportDTO.toBuilder().filename(reportDTO.getFilename() + ".xlsx").build());

    }

    public byte[] XSSFWorkbooktoByteArray(XSSFWorkbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] workbookByteArray = outputStream.toByteArray();
        outputStream.close();

        return workbookByteArray;
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

        final String fileExtension = ".xlsx";
        for (Report report : reports) {
            final String reportName =
                            "Report For " + report.getStamp().format(formatter) + fileExtension;
            ZipEntry entry = new ZipEntry(reportName);
            entry.setSize(report.getContents().length);
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(report.getContents());
        }

        zipOutputStream.closeEntry();
        zipOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Optional<ReportDTO> getAllByDayBetween(LocalDateTime start, LocalDateTime end)
                    throws IOException {
        List<Report> reports = reportRepository
                        .findAllByStampBetween(start.toLocalDate(), end.toLocalDate()).stream()
                        .filter(r -> r.getContents() != null).collect(Collectors.toList());

        if (reports.isEmpty()) {
            return Optional.empty();
        }

        byte[] reportsZipByteArray = null;
        try {
            reportsZipByteArray = reportListToZipByteArray(reports);
        } catch (IOException e) {
            throw new IOException("Failed to write report entry to a zip file", e);
        }

        if (reportsZipByteArray == null) {
            return Optional.empty();
        }

        final String fileExtension = "zip";
        String filename = String.format("Reports_From_%s_to_%s.%s",
                        start.toLocalDate().format(formatter), end.toLocalDate().format(formatter),
                        fileExtension);

        ReportDTO reportDTO = ReportDTO.builder().contentLength(reportsZipByteArray.length)
                        .contents(reportsZipByteArray).filename(filename).id(null).build();
        return Optional.of(reportDTO);
    }



    @Override
    public Optional<ReportDTO> generateForDoctor(Long doctorID, LocalDateTime from,
                    LocalDateTime to) throws IllegalArgumentException, IOException {
        LocalDate fromDate = from.toLocalDate();
        LocalDate toDate = to.toLocalDate();

        List<Report> reports = reportRepository.findAllByStampBetween(fromDate, toDate).stream()
                        .filter(r -> r.getContents() != null).collect(Collectors.toList());


        Optional<Doctor> fetchedDoctorContainer = doctorRepository.findById(doctorID);
        if (fetchedDoctorContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced doctor does not exist");
        }
        Doctor doctor = fetchedDoctorContainer.get();

        XSSFWorkbook workbook = new XSSFWorkbook();
        for (Report report : reports) {
            XSSFWorkbook reportWorkbook = null;
            try {
                reportWorkbook = ByteArrayToXSSFWorkbook(report.getContents());
            } catch (IOException e) {
                throw new IOException("Unaable to convert report to workbook", e);
            }

            XSSFSheet sheet = reportWorkbook.getSheet(Long.toString(doctor.getId()));
            if (sheet == null) {
                continue;
            }

            XSSFSheet doctorSheetForDay = workbook.createSheet(report.getStamp().format(formatter));

            ArrayList<XSSFRow> rowList = new ArrayList<>();
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                rowList.add(sheet.getRow(i));
            }

            doctorSheetForDay.copyRows(rowList, 0, new CellCopyPolicy());
        }

        byte[] doctorWorkbookByteArray = null;
        try {
            doctorWorkbookByteArray = XSSFWorkbooktoByteArray(workbook);
        } catch (IOException e) {
            throw new IOException("Unable to convert workbook to byte array", e);
        }

        if (doctorWorkbookByteArray == null) {
            return Optional.empty();
        }

        final String fileExtension = "xlsx";
        String filename = String.format("Reports_From_%s_to_%s.%s", fromDate.format(formatter),
                        toDate.format(formatter), fileExtension);
        ReportDTO reportDTO = ReportDTO.builder().contentLength(doctorWorkbookByteArray.length)
                        .contents(doctorWorkbookByteArray).filename(filename).id(null).build();

        return Optional.of(reportDTO);

    }

    private String extractNullableValue(Optional<Object> optional) {
        if (optional.isEmpty()) {
            return "";
        }

        return optional.get().toString();
    }

    @Override
    public void forceRunReportGenerator(LocalDateTime forDay) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        final LocalDateTime temporalTarget = forDay.toLocalDate().atStartOfDay();
        System.out.println("Started generating report for " + temporalTarget);
        XSSFRow titlesRow = null;
        ArrayList<String> titles = null;

        // Append list of all new patients
        XSSFSheet patientsSheet = workbook.createSheet("New Patients");
        titlesRow = patientsSheet.createRow(0);
        titles = new ArrayList<>(
                        Arrays.asList("ID", "Name", "Gender", "Age", "Email", "Phone Number"));
        for (int i = 0; i < titles.size(); i++) {
            titlesRow.createCell(i).setCellValue(titles.get(i));
        }

        List<Patient> patients = patientRepository.findAllByStampBetween(temporalTarget,
                        temporalTarget.toLocalDate().atTime(LocalTime.MAX));
        for (Patient patient : patients) {
            XSSFRow currentPatient = patientsSheet.createRow(patientsSheet.getLastRowNum() + 1);
            currentPatient.createCell(0).setCellValue(patient.getId());
            currentPatient.createCell(currentPatient.getLastCellNum())
                            .setCellValue(patient.getName());
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(
                            extractNullableValue(Optional.ofNullable(patient.getGender())));
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(
                            extractNullableValue(Optional.ofNullable(patient.getAge())));
            currentPatient.createCell(currentPatient.getLastCellNum())
                            .setCellValue(patient.getEmail());
            currentPatient.createCell(currentPatient.getLastCellNum()).setCellValue(
                            extractNullableValue(Optional.ofNullable(patient.getPhone())));
        }

        // Append list of all new doctors
        XSSFSheet doctorsSheet = workbook.createSheet("New Doctors");
        titlesRow = doctorsSheet.createRow(0);
        titles.add("Speciality");
        for (int i = 0; i < titles.size(); i++) {
            titlesRow.createCell(i).setCellValue(titles.get(i));
        }

        List<Doctor> doctors = doctorRepository.findAllByStampBetween(temporalTarget,
                        temporalTarget.toLocalDate().atTime(LocalTime.MAX));
        for (Doctor doctor : doctors) {
            XSSFRow currentDoctor = doctorsSheet.createRow(doctorsSheet.getLastRowNum() + 1);
            currentDoctor.createCell(0).setCellValue(doctor.getId());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(doctor.getName());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(
                            extractNullableValue(Optional.ofNullable(doctor.getGender())));
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(
                            extractNullableValue(Optional.ofNullable(doctor.getAge())));
            currentDoctor.createCell(currentDoctor.getLastCellNum())
                            .setCellValue(doctor.getEmail());
            currentDoctor.createCell(currentDoctor.getLastCellNum()).setCellValue(
                            extractNullableValue(Optional.ofNullable(doctor.getPhone())));
            currentDoctor.createCell(currentDoctor.getLastCellNum())
                            .setCellValue(doctor.getSpeciality().getName());
        }

        // Create a sheet for each doctor and append their schedules, appointments, and
        // patients
        List<Doctor> allDoctors = doctorRepository.findAll();
        for (Doctor doctor : allDoctors) {
            XSSFSheet currentDoctorMetaSheet = workbook.createSheet(doctor.getId().toString());
            XSSFRow currentRow = currentDoctorMetaSheet.createRow(0);
            currentRow.createCell(0).setCellValue("Name");
            currentRow.createCell(1).setCellValue(doctor.getName());

            currentDoctorMetaSheet.createRow(1);

            titlesRow = currentDoctorMetaSheet
                            .createRow(currentDoctorMetaSheet.getLastRowNum() + 1);
            titles = new ArrayList<>(Arrays.asList("Appointment Time", "Appointment ID",
                            "Patient ID", "Patient Name", "Scheduled On", "Attended"));
            for (int i = 0; i < titles.size(); i++) {
                titlesRow.createCell(i).setCellValue(titles.get(i));
            }

            List<Appointment> appointments = appointmentRepository.findAllByDoctorAndStartBetween(
                            doctor, temporalTarget,
                            temporalTarget.toLocalDate().atTime(LocalTime.MAX));
            for (Appointment appointment : appointments) {
                currentRow = currentDoctorMetaSheet
                                .createRow(currentDoctorMetaSheet.getLastRowNum() + 1);
                currentRow.createCell(0).setCellValue(appointment.getStart());
                currentRow.createCell(currentRow.getLastCellNum())
                                .setCellValue(appointment.getId());
                currentRow.createCell(currentRow.getLastCellNum())
                                .setCellValue(appointment.getPatient().getId());
                currentRow.createCell(currentRow.getLastCellNum())
                                .setCellValue(appointment.getPatient().getName());
                currentRow.createCell(currentRow.getLastCellNum())
                                .setCellValue(appointment.getStamp().toString());
                currentRow.createCell(currentRow.getLastCellNum())
                                .setCellValue(appointment.getAttended());
            }
        }

        // Save into the repository
        byte[] workbookByteArray = null;
        try {
            workbookByteArray = XSSFWorkbooktoByteArray(workbook);
            if (workbookByteArray.length == 0) {
                throw new IOException("Empty byte array received");
            }
        } catch (IOException e) {
            throw new IOException("Could not create a byte array for the excel workbook", e);
        }
        Report newReport = Report.builder().contents(workbookByteArray)
                        .stamp(temporalTarget.toLocalDate()).build();
        reportRepository.save(newReport);
        System.out.println("Saved report for " + temporalTarget.truncatedTo(ChronoUnit.SECONDS));
    }

    @Scheduled(cron = "${report.gen.interval}")
    @Async
    public void reportGenerationScheduler() {
        try {
            forceRunReportGenerator(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        } catch (IOException e) {
            System.out.println("Scheduled report generation failed\n" + e);
        }

    }
}
