// package com.mms.demo.service;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
// import org.apache.poi.ss.usermodel.Workbook;
// import org.apache.poi.xssf.usermodel.XSSFRow;
// import org.apache.poi.xssf.usermodel.XSSFSheet;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Order;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;

// import com.mms.demo.entity.Appointment;
// import com.mms.demo.entity.Doctor;
// import com.mms.demo.entity.Patient;
// import com.mms.demo.entity.Report;
// import com.mms.demo.entity.Schedule;
// import com.mms.demo.entity.Slot;
// import com.mms.demo.entity.Speciality;
// import com.mms.demo.serviceImpl.ReportServiceImpl;

// import static org.assertj.core.api.Assertions.assertThat;

// import java.io.ByteArrayInputStream;
// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.OutputStream;
// import java.time.LocalDateTime;
// import java.time.temporal.ChronoUnit;
// import java.time.LocalDateTime;
// import java.time.temporal.ChronoUnit;
// import java.time.LocalTime;
// import java.time.temporal.ChronoField;
// import java.time.temporal.ChronoUnit;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.ListIterator;
// import java.util.Optional;
// import java.util.Random;
// import java.util.zip.ZipInputStream;

// @SpringBootTest
// @TestMethodOrder(OrderAnnotation.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
// public class POIReportServiceTest {
// @Autowired
// ReportServiceImpl reportService;

// @Autowired
// DoctorService doctorService;

// @Autowired
// SpecialityService specialityService;

// @Autowired
// PatientService patientService;

// @Autowired
// ScheduleService scheduleService;

// @Autowired
// SlotService slotService;

// @Autowired
// AppointmentService appointmentService;

// private String genAlnum(int targetStringLength) {
// int leftLimit = 48;
// int rightLimit = 122;
// Random random = new Random();

// String generatedString = random.ints(leftLimit, rightLimit + 1)
// .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
// .limit(targetStringLength)
// .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
// .toString();

// return generatedString;
// }

// @Test
// @Order(1)
// void testByteArrayMethods() {
// XSSFWorkbook workbook = new XSSFWorkbook();
// XSSFSheet firstSheet = workbook.createSheet("Some Sheet");
// XSSFRow row = firstSheet.createRow(0);
// row.createCell(0).setCellValue("Jerry");

// byte[] byteArray = null;
// try {
// byteArray = reportService.XSSFWorkbooktoByteArray(workbook);
// } catch (IOException e) {
// System.out.println(e);
// }
// assertThat(byteArray).isNotNull();

// XSSFWorkbook test = null;
// try {
// test = reportService.ByteArrayToXSSFWorkbook(byteArray);
// } catch (IOException e) {
// System.out.println(e);
// }
// assertThat(test).isNotNull();

// File file = new File("workbook.xlsx");
// try (OutputStream fileOut = new FileOutputStream(file)) {
// test.write(fileOut);
// } catch (IOException e) {
// System.out.println("Empty" + e);
// }
// assertThat(file).exists();
// System.out.println(file.getAbsolutePath());
// file.deleteOnExit();
// }

// @Test
// @Order(2)
// @DisplayName("Test generation on an empty field")
// void testEmptyGen() {
// assertThat(reportService.generateReports(LocalDateTime.now(),
// LocalDateTime.now().plusDays(1))).isEmpty();
// }

// @Test
// @Order(3)
// @DisplayName("Testing create on a single report")
// void testCreateReportOne() {
// LocalDateTime temporalTarget = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS);

// // create patients
// for (int i = 0; i < 1000; i++) {
// Patient temp = Patient.builder()
// .email(genAlnum(14) + "@xyz.com")
// .name(genAlnum(10) + genAlnum(10))
// .phone(genAlnum(10))
// .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
// .build();

// patientService.createPatient(temp);
// }

// // create doctors and specialities
// for (int i = 0; i < 10; i++) {
// Speciality speciality = Speciality.builder().name(genAlnum(7)).build();
// specialityService.createSpeciality(speciality);
// }

// List<Speciality> specialityList = specialityService.getAllSpecialities();
// for (int i = 0; i < 100; i++) {
// Random rand = new Random();
// Doctor temp = Doctor.builder().age(25)
// .email(genAlnum(14) + "@xyz.com")
// .gender("M")
// .name(genAlnum(10) + genAlnum(10))
// .phone(genAlnum(10))
// .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
// .speciality(specialityList.get(rand.nextInt(specialityList.size())))
// .build();

// doctorService.createDoctor(temp);
// }

// // create slots, schedules, and appointments
// List<Doctor> doctorList = doctorService.getAllDoctors();
// List<Patient> patientList = patientService.getAllPatients();
// for ( ListIterator doctors = doctorList.listIterator(), patients = patientList.listIterator();
// doctors.hasNext() && patients.hasNext();
// ) {

// Doctor doctor = (Doctor)doctors.next();
// LocalTime incrementalStart = temporalTarget.toLocalTime()
// .withHour(9)
// .withMinute(0)
// .withSecond(0)
// .withNano(0);
// for (int i = 0; i < 10 && patients.hasNext(); i++) {
// Patient patient = (Patient)patients.next();
// Slot slot = Slot.builder().start(incrementalStart)
// .end(incrementalStart.plusMinutes(30))
// .weekday(temporalTarget.getDayOfWeek())
// .build();
// slotService.createSlot(slot);

// Schedule schedule = Schedule.builder().doctor(doctor)
// .slot(slot)
// .weekDate(temporalTarget.toLocalDate())
// .build();
// scheduleService.createSchedule(schedule);

// Appointment appointment = Appointment.builder()
// .patient(patient)
// .slot(slot)
// .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
// .build();
// appointmentService.createAppointment(appointment);

// incrementalStart = incrementalStart.plusMinutes(30);
// }
// }

// reportService.forceRunReportGenerator(temporalTarget);
// }

// @Test
// @Order(4)
// @DisplayName("Testing create on a single report")
// void testCreateReportTwo() {
// LocalDateTime temporalTarget = LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.DAYS);

// // create patients
// for (int i = 0; i < 1000; i++) {
// Patient temp = Patient.builder().age(25)
// .email(genAlnum(14) + "@xyz.com")
// .gender("M")
// .name(genAlnum(10) + genAlnum(10))
// .phone(genAlnum(10))
// .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
// .build();

// patientService.createPatient(temp);
// }

// // create doctors and specialities
// for (int i = 0; i < 10; i++) {
// Speciality speciality = Speciality.builder().name(genAlnum(7)).build();
// specialityService.createSpeciality(speciality);
// }

// List<Speciality> specialityList = specialityService.getAllSpecialities();
// for (int i = 0; i < 100; i++) {
// Random rand = new Random();
// Doctor temp = Doctor.builder().age(25)
// .email(genAlnum(14) + "@xyz.com")
// .gender("M")
// .name(genAlnum(10) + genAlnum(10))
// .phone(genAlnum(10))
// .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
// .speciality(specialityList.get(rand.nextInt(specialityList.size())))
// .build();

// doctorService.createDoctor(temp);
// }

// // create slots, schedules, and appointments
// List<Doctor> doctorList = doctorService.getAllDoctors();
// List<Patient> patientList = patientService.getAllPatients();
// for ( ListIterator doctors = doctorList.listIterator(), patients = patientList.listIterator();
// doctors.hasNext() && patients.hasNext();
// ) {

// Doctor doctor = (Doctor)doctors.next();
// LocalTime incrementalStart = temporalTarget.toLocalTime()
// .withHour(9)
// .withMinute(0)
// .withSecond(0)
// .withNano(0);
// for (int i = 0; i < 10 && patients.hasNext(); i++) {
// Patient patient = (Patient)patients.next();
// Slot slot = Slot.builder().start(incrementalStart)
// .end(incrementalStart.plusMinutes(30))
// .weekday(temporalTarget.getDayOfWeek())
// .build();
// slotService.createSlot(slot);

// Schedule schedule = Schedule.builder().doctor(doctor)
// .slot(slot)
// .weekDate(temporalTarget.toLocalDate())
// .build();
// scheduleService.createSchedule(schedule);

// Appointment appointment = Appointment.builder()
// .patient(patient)
// .slot(slot)
// .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
// .build();
// appointmentService.createAppointment(appointment);

// incrementalStart = incrementalStart.plusMinutes(30);
// }
// }

// reportService.forceRunReportGenerator(temporalTarget);

// Optional<byte[]> reportByteArray = reportService.generateReports(temporalTarget,
// temporalTarget.plusDays(3).minusSeconds(1).truncatedTo(ChronoUnit.SECONDS));
// assertThat(reportByteArray).isNotEmpty();

// try (OutputStream fileOut = new FileOutputStream("workbook.zip")) {
// fileOut.write(reportByteArray.get());

// } catch (IOException e) {
// System.out.println("Empty" + e);
// }

// }

// }
