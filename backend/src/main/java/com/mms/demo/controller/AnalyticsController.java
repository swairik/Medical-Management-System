// package com.mms.demo.controller;

// import java.time.LocalDate;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
// import java.util.HashSet;
// import java.util.stream.Collectors;

// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.mms.demo.entity.Appointment;
// import com.mms.demo.entity.Doctor;
// import com.mms.demo.entity.Schedule;
// import com.mms.demo.entity.Speciality;
// import com.mms.demo.service.AppointmentService;
// import com.mms.demo.service.DoctorService;
// import com.mms.demo.service.PatientService;
// import com.mms.demo.service.ScheduleService;
// import com.mms.demo.service.SpecialityService;

// @CrossOrigin("*")
// @RestController
// @RequestMapping("/analytics")
// public class AnalyticsController {

// @Autowired
// PatientService patientservice;

// @Autowired
// DoctorService doctorService;

// @Autowired
// SpecialityService specialityService;

// @Autowired
// AppointmentService appointmentService;

// @Autowired
// ScheduleService scheduleService;

// @ResponseBody
// @GetMapping("/display/specialityDoctorCount")
// public ResponseEntity<Map<String, Integer>> specialityDoctorCount() {
// List<Doctor> doctors = doctorService.getAllDoctors();
// List<Speciality> specialities = specialityService.getAllSpecialities();
// Map<String, Integer> specialitySet = new HashMap<>();
// specialities.stream().forEach((s) -> specialitySet.put(s.getName(), 0));
// doctors.stream().forEach((d) ->
// specialitySet.put(d.getSpeciality().getName(),
// specialitySet.get(d.getSpeciality().getName()) + 1));
// System.out.println(specialitySet);
// return new ResponseEntity<>(specialitySet, HttpStatus.OK);
// }

// @GetMapping("/display/patients/speciality")
// public ResponseEntity<Map<String, Integer>> specialityPatientCount() {
// List<Appointment> appointments = appointmentService.getAllAppointments();
// List<Speciality> specialities = specialityService.getAllSpecialities();
// List<Schedule> schedules = scheduleService.getAllSchedules();
// Map<String, Integer> specialitySet = new HashMap<>();
// specialities.stream().forEach((s) -> specialitySet.put(s.getName(), 0));
// appointments.stream().forEach((a) -> {
// Schedule schedule = schedules.stream()
// .filter((s) -> s.getSlot().getId() == a.getSlot().getId()).findFirst()
// .orElse(null);
// if (schedule != null) {
// String specialityName = schedule.getDoctor().getSpeciality().getName();
// specialitySet.put(specialityName, specialitySet.get(specialityName) + 1);
// }
// });
// return new ResponseEntity<>(specialitySet, HttpStatus.OK);
// }

// @GetMapping("/display/appointment/doctor")
// public ResponseEntity<Map<String, Integer>> appointmentDoctorCount() {
// List<Appointment> appointments = appointmentService.getAllAppointments();
// List<Schedule> schedules = scheduleService.getAllSchedules();
// List<Appointment> todayAppointments = appointments.stream().filter((a) -> {
// Schedule schedule = schedules.stream()
// .filter((s) -> s.getSlot().getId() == a.getSlot().getId()).findFirst()
// .orElse(null);
// if (schedule == null) {
// return false;
// }
// LocalDate dateOfAppointment =
// schedule.getWeekDate().plusDays(a.getSlot().getWeekday().getValue());
// return dateOfAppointment.equals(LocalDate.now());

// }).collect(Collectors.toList());
// Set<Doctor> doctors = new HashSet<>();
// todayAppointments.stream().forEach((a) -> {
// Schedule schedule = schedules.stream()
// .filter((s) -> s.getSlot().getId() == a.getSlot().getId()).findFirst()
// .orElse(null);
// if (schedule != null) {
// doctors.add(schedule.getDoctor());
// }
// });
// Map<String, Integer> response = new HashMap<>();
// response.put("Appointments", todayAppointments.size());
// response.put("Doctors", doctors.size());
// return new ResponseEntity<>(response, HttpStatus.OK);
// }

// }
