package com.mms.demo.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;
import com.mms.demo.entity.Speciality;
import com.mms.demo.exception.CustomException;
import com.mms.demo.service.AppointmentDetailsService;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.service.SlotService;
import com.mms.demo.service.SpecialityService;

@CrossOrigin("*")
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    PatientService patientservice;

    @Autowired
    DoctorService doctorService;

    @Autowired
    SpecialityService specialityService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    SlotService slotService;

    @Autowired
    AppointmentDetailsService appointmentDetailsService;

    /*
     * shows the number of doctors in each speciality
     * ADMIN
     */
    @ResponseBody
    @GetMapping("/display/specialityDoctorCount")
    public ResponseEntity<Map<String, Integer>> specialityDoctorCount() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Speciality> specialities = specialityService.getAllSpecialities();
        Map<String, Integer> specialitySet = new HashMap<>();
        specialities.stream().forEach((s) -> specialitySet.put(s.getName(), 0));
        doctors.stream().forEach((d) -> specialitySet.put(d.getSpeciality().getName(),
                specialitySet.get(d.getSpeciality().getName()) + 1));
        System.out.println(specialitySet);
        return new ResponseEntity<>(specialitySet, HttpStatus.OK);
    }

    /*
     * shows the numbers of patient booked in each speciality
     * ADMIN
     */
    @GetMapping("/display/patients/speciality")
    public ResponseEntity<Map<String, Integer>> specialityPatientCount() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        List<Speciality> specialities = specialityService.getAllSpecialities();
        List<Schedule> schedules = scheduleService.getAllSchedules();
        Map<String, Integer> specialitySet = new HashMap<>();
        specialities.stream().forEach((s) -> specialitySet.put(s.getName(), 0));
        appointments.stream().forEach((a) -> {
            Schedule schedule = schedules.stream()
                    .filter((s) -> s.getSlot().getId() == a.getSlot().getId()).findFirst()
                    .orElse(null);
            if (schedule != null) {
                String specialityName = schedule.getDoctor().getSpeciality().getName();
                specialitySet.put(specialityName, specialitySet.get(specialityName) + 1);
            }
        });
        return new ResponseEntity<>(specialitySet, HttpStatus.OK);
    }

    /*
     * shows the number of appointments today
     * shows the number of active doctors today
     * ADMIN
     */
    @GetMapping("/display/appointment/doctor")
    public ResponseEntity<Map<String, Integer>> appointmentDoctorCount() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        List<Schedule> schedules = scheduleService.getAllSchedules();
        List<Appointment> todayAppointments = appointments.stream().filter((a) -> {
            Schedule schedule = schedules.stream()
                    .filter((s) -> s.getSlot().getId() == a.getSlot().getId()).findFirst()
                    .orElse(null);
            if (schedule == null) {
                return false;
            }
            LocalDate dateOfAppointment = schedule.getWeekDate().plusDays(a.getSlot().getWeekday().getValue());
            return dateOfAppointment.equals(LocalDate.now());

        }).collect(Collectors.toList());
        Set<Doctor> doctors = new HashSet<>();
        todayAppointments.stream().forEach((a) -> {
            Schedule schedule = schedules.stream()
                    .filter((s) -> s.getSlot().getId() == a.getSlot().getId()).findFirst()
                    .orElse(null);
            if (schedule != null) {
                doctors.add(schedule.getDoctor());
            }
        });
        Map<String, Integer> response = new HashMap<>();
        response.put("Appointments", todayAppointments.size());
        response.put("Doctors", doctors.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
     * shows the number of upcoming appointments for a particular dr
     * shows the total number of patients in all the upcoming appointments for a particular dr
     * DOCTOR
     */
    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<Map<String, Integer>> appointmentDoctorDetails(@PathVariable Long did) {
        doctorService.getDoctortById(did)
                .orElseThrow(() -> new CustomException("Doctor with given id does not exist", "DOCTOR_NOT_FOUND"));
        LocalDateTime dateTime = LocalDateTime.now();
        List<Appointment> appointments = appointmentService.getAllAppointments();
        List<Appointment> filteredUpcomingAppointments = appointments.stream().filter((a) -> {
            List<Schedule> schedules = scheduleService.getSchedulesBySlot(a.getSlot());
            List<Schedule> filteredSchedules = schedules.stream()
                    .filter((s) -> s.getSlot().getId() == a.getSlot().getId()
                            && s.getDoctor().getId() == did)
                    .collect(Collectors.toList());

            if (filteredSchedules.size() == 0) {
                throw new CustomException("Invalid schedule id", "SCHEDULE_NOT_FOUND");
            }

            Slot slot = slotService.getSlotById(a.getSlot().getId()).orElse(null);

            if (slot == null) {
                return false;
            }

            LocalDateTime appointmentDateTime = filteredSchedules.get(0).getWeekDate()
                    .plusDays(slot.getWeekday().getValue())
                    .atTime(slot.getStart());

            return appointmentDateTime.isAfter(dateTime);
        }).collect(Collectors.toList());

        Set<Patient> patients = new HashSet<>();
        appointments.stream().forEach((a) -> patients.add(a.getPatient()));

        Map<String, Integer> response = new HashMap<>();
        response.put("Number of Upcoming appointments", filteredUpcomingAppointments.size());
        response.put("Patient count", patients.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
     * shows the number of appointments in the current week
     * shows the number of doctors who have appointments in the current week
     * ADMIN
     */
    @ResponseBody
    @GetMapping("/display/appointment/weekly")
    public ResponseEntity<Map<String, Integer>> appointmentWeeklyDetails(@RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate currentWeekDate;
        try {
            currentWeekDate = LocalDate.parse(stamp, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT");
        }
        currentWeekDate = currentWeekDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        Integer currentWeek = currentWeekDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        List<Schedule> allSchedules = scheduleService.getAllSchedules();

        List<Appointment> filteredAppointments = allAppointments.stream().filter((a) -> {
            Long appointmentSlotId = a.getSlot().getId();
            Schedule appointmentSchedule = allSchedules.stream()
                    .filter((sched) -> sched.getSlot().getId() == appointmentSlotId).findFirst().orElse(null);
            if (appointmentSchedule == null) {
                return false;
            }
            return appointmentSchedule.getWeek() == currentWeek;
        }).collect(Collectors.toList());

        Set<Long> doctorIds = new HashSet<>();
        filteredAppointments.stream().forEach((a) -> {
            Slot appointmentSlot = a.getSlot();
            List<Schedule> scheduleWithSlot = scheduleService.getSchedulesBySlot(appointmentSlot);
            if (scheduleWithSlot.size() != 0) {
                doctorIds.add(scheduleWithSlot.get(0).getDoctor().getId());
            }
        });

        Map<String, Integer> response = new HashMap<>();
        response.put("Number of appointments in the current week", filteredAppointments.size());
        response.put("Number of doctors with appointments in current week", doctorIds.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
     * shows the number of appointments in the current month
     * shows the number of doctors who have appointments in the current month
     * ADMIN
     */
    @GetMapping("/display/appointment/monthly")
    public ResponseEntity<Map<String, Integer>> appointmentMonthlyDetails(@RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate currentWeekDate;
        try {
            currentWeekDate = LocalDate.parse(stamp, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT");
        }

        Integer currentMonth = currentWeekDate.getMonthValue();

        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        List<Schedule> allSchedules = scheduleService.getAllSchedules();

        List<Appointment> filteredAppointments = allAppointments.stream().filter((a) -> {
            Long appointmentSlotId = a.getSlot().getId();
            Schedule appointmentSchedule = allSchedules.stream()
                    .filter((sched) -> sched.getSlot().getId() == appointmentSlotId).findFirst().orElse(null);
            if (appointmentSchedule == null) {
                return false;
            }
            return appointmentSchedule.getWeekDate().getMonthValue() == currentMonth;
        }).collect(Collectors.toList());

        Set<Long> doctorIds = new HashSet<>();
        filteredAppointments.stream().forEach((a) -> {
            Slot appointmentSlot = a.getSlot();
            List<Schedule> scheduleWithSlot = scheduleService.getSchedulesBySlot(appointmentSlot);
            if (scheduleWithSlot.size() != 0) {
                doctorIds.add(scheduleWithSlot.get(0).getDoctor().getId());
            }
        });

        Map<String, Integer> response = new HashMap<>();
        response.put("Number of appointments in the current month", filteredAppointments.size());
        response.put("Number of doctors with appointments in current month", doctorIds.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
     * shows the number of unfilled prescriptions by doctor - assumes each doctor
     * has only 1 appointment at a specific time & date
     * DOCTOR
     */
    @ResponseBody
    @GetMapping("/display/doctor/{id}/unfilledPrescription")
    public ResponseEntity<Map<String, Integer>> doctorUnfilledPrescriptions(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctortById(id)
                .orElseThrow(() -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND"));
        List<AppointmentDetails> allAppointmentDetails = appointmentDetailsService.getAllByDoctor(doctor);

        Set<LocalDateTime> filteredAppointmentDetails = new HashSet<>();
        String delimiter = ":,-";
        String unfilledContent = delimiter + delimiter;

        allAppointmentDetails.stream().forEach((a) -> {
            String prescriptionContent = new String(Base64.getDecoder().decode(a.getPrescription().getContents()));
            if (prescriptionContent.equals(unfilledContent)) {
                filteredAppointmentDetails.add(a.getStamp());
            }
        });

        Map<String, Integer> response = new HashMap<>();
        response.put("Number of unfilled prescriptions", filteredAppointmentDetails.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
     * shows the number of appointments already seen in the current week
     * DOCTOR
    */
    @ResponseBody
    @GetMapping("/display/doctor/{id}/alreadySeenWeek")
    public ResponseEntity<Map<String, Integer>> doctorAlreadySeenThisWeek(@PathVariable Long id,
            @RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentDateTime;
        try {
            currentDateTime = LocalDateTime.parse(stamp, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT");
        }
        LocalDate currentWeekDate = currentDateTime.toLocalDate();
        LocalDate startDate = currentWeekDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        List<Appointment> appointments = appointmentService.getAllAppointments();
        List<Schedule> schedules = scheduleService.getAllSchedules();

        Doctor doctor = doctorService.getDoctortById(id)
                .orElseThrow(() -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND"));

        Map<String, Integer> response = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            long diff = date.until(currentWeekDate, ChronoUnit.DAYS);
            if (diff > 0) {
                List<Appointment> thisDaysAppointments = appointments.stream().filter((a) -> {
                    Schedule schedule = schedules.stream()
                            .filter((s) -> s.getSlot().getId() == a.getSlot().getId()).findFirst()
                            .orElse(null);
                    if (schedule == null || schedule.getDoctor().getId() != doctor.getId()) {
                        return false;
                    }
                    LocalDate dateOfAppointment = schedule.getWeekDate().plusDays(a.getSlot().getWeekday().getValue());
                    return dateOfAppointment.equals(date);
                }).collect(Collectors.toList());
                response.put(date.getDayOfWeek().toString(), thisDaysAppointments.size());
            } else if (diff == 0) {
                List<Appointment> thisDaysAppointmentsUptoNow = appointments.stream().filter((a) -> {
                    Schedule schedule = schedules.stream()
                            .filter((s) -> s.getSlot().getId() == a.getSlot().getId()).findFirst()
                            .orElse(null);
                    if (schedule == null || schedule.getDoctor().getId() != doctor.getId()) {
                        return false;
                    }
                    Slot slot = slotService.getSlotById(a.getSlot().getId()).orElse(null);
                    if (slot == null) {
                        return false;
                    }
                    LocalDateTime appointmentDateTime = schedule.getWeekDate()
                            .plusDays(slot.getWeekday().getValue())
                            .atTime(slot.getStart());
                    LocalDate dateOfAppointment = schedule.getWeekDate().plusDays(a.getSlot().getWeekday().getValue());
                    return dateOfAppointment.equals(date) && appointmentDateTime.isBefore(currentDateTime);
                }).collect(Collectors.toList());
                response.put(date.getDayOfWeek().toString(), thisDaysAppointmentsUptoNow.size());
            } else {
                response.put(date.getDayOfWeek().toString(), 0);
            }
        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
