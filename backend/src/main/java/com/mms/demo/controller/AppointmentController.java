package com.mms.demo.controller;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Role;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.AppointmentRequest;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.transferobject.AppointmentDTO;
import com.mms.demo.transferobject.PatientDTO;

@CrossOrigin("*")
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("/display")
    public ResponseEntity<List<AppointmentDTO>> displayAllAppointments() {
        List<AppointmentDTO> appointmentsList = appointmentService.getAll();
        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<AppointmentDTO> displayAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointment = appointmentService.get(id)
                .orElseThrow(() -> new CustomException(
                        "Appointment with given id not found",
                        "APPOINTMENT_NOT_FOUND", HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/display/patient/{pid}")
    public ResponseEntity<List<AppointmentDTO>> displayAppointmentsByPatient(
            @PathVariable Long pid) {
        patientService.get(pid)
                .orElseThrow(() -> new CustomException("Patient with given id not found",
                        "PATIENT_NOT_FOUND", HttpStatus.NOT_FOUND));
        List<AppointmentDTO> appointmentsList = appointmentService.getAllByPatient(pid);
        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    @GetMapping("/display/patient/{pid}/upcoming")
    public ResponseEntity<List<AppointmentDTO>> showAllPatientUpcoming(@PathVariable Long pid,
            @RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT", HttpStatus.BAD_REQUEST);
        }
        patientService.get(pid)
                .orElseThrow(() -> new CustomException("Patient with given id not found",
                        "PATIENT_NOT_FOUND", HttpStatus.NOT_FOUND));
        List<AppointmentDTO> appointmentsList = appointmentService.getAllByPatientAfter(pid, dateTime);
        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    @GetMapping("/display/patient/{pid}/between")
    public ResponseEntity<List<AppointmentDTO>> showAllPatientBetween(@PathVariable Long pid,
            @RequestParam String start,
            @RequestParam String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startTime, endTime;
        try {
            startTime = LocalDateTime.parse(start, formatter);
            endTime = LocalDateTime.parse(end, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of timestamp", "WRONG_FORMAT", HttpStatus.BAD_REQUEST);
        }
        patientService.get(pid)
                .orElseThrow(() -> new CustomException("Patient with given id not found",
                        "PATIENT_NOT_FOUND", HttpStatus.NOT_FOUND));
        List<AppointmentDTO> appointmentsList = appointmentService.getAllByPatientBetween(pid, startTime, endTime);
        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    /*
     * deprecated endpoint
     */
    // @GetMapping("/display/slot/{id}")
    // public ResponseEntity<List<AppointmentDTO>> displayAppointmentsBySlot(
    // @PathVariable Long id) {
    // List<AppointmentDTO> appointmentsList =
    // appointmentService.getAppointmentsBySlot(slot);
    // return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    // }

    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<List<AppointmentDTO>> displayAppointmentsByDoctor(@PathVariable Long did) {
        doctorService.get(did).orElseThrow(
                () -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND", HttpStatus.NOT_FOUND));
        List<AppointmentDTO> appointmentsList = appointmentService.getAllByDoctor(did);
        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    @GetMapping("/display/doctor/{did}/upcoming")
    public ResponseEntity<List<AppointmentDTO>> showUpcomingAppointmentsByDoctor(@PathVariable Long did,
            @RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT", HttpStatus.BAD_REQUEST);
        }

        doctorService.get(did).orElseThrow(
                () -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND", HttpStatus.NOT_FOUND));

        List<AppointmentDTO> appointmentsList = appointmentService.getAllByDoctorAfter(did, dateTime);

        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    @GetMapping("/display/doctor/{did}/between")
    public ResponseEntity<List<AppointmentDTO>> showAllDoctorAppointmentBetween(@PathVariable Long did,
            @RequestParam String start, @RequestParam String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startTime, endTime;
        try {
            startTime = LocalDateTime.parse(start, formatter);
            endTime = LocalDateTime.parse(end, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of timestamp", "WRONG_FORMAT", HttpStatus.BAD_REQUEST);
        }
        doctorService.get(did).orElseThrow(
                () -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND", HttpStatus.NOT_FOUND));
        List<AppointmentDTO> appointmentsList = appointmentService.getAllByDoctorBetween(did, startTime, endTime);
        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    @GetMapping("/display/{pid}/{did}")
    public ResponseEntity<AppointmentDTO> showAppointmentByDoctorPatientStamp(@PathVariable Long did,
            @PathVariable Long pid, @RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT", HttpStatus.BAD_REQUEST);
        }
        List<AppointmentDTO> appointment = appointmentService.getAllByPatientAndDoctorBetween(pid, did, dateTime,
                dateTime);
        if (appointment.isEmpty()) {
            throw new CustomException("Appointment with given details not found", "APPOINTMENT_NOT_FOUND",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointment.get(0), HttpStatus.OK);
    }

    @GetMapping("/display/{did}/{pid}/between")
    public ResponseEntity<List<AppointmentDTO>> showAppointmentByDoctorPatientBetween(@PathVariable Long did,
            @PathVariable Long pid, @RequestParam String start, @RequestParam String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startTime, endTime;
        try {
            startTime = LocalDateTime.parse(start, formatter);
            endTime = LocalDateTime.parse(end, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT", HttpStatus.BAD_REQUEST);
        }
        List<AppointmentDTO> appointment = appointmentService.getAllByPatientAndDoctorBetween(did, pid, startTime,
                endTime);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/display/{did}/{pid}/upcoming")
    public ResponseEntity<List<AppointmentDTO>> showAppointmentByDoctorPatientUpcoming(@PathVariable Long did,
            @PathVariable Long pid, @RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT", HttpStatus.BAD_REQUEST);
        }
        List<AppointmentDTO> appointment = appointmentService.getAllByPatientAndDoctorAfter(did, pid, dateTime);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<AppointmentDTO> createAppointment(
            @RequestBody AppointmentRequest appointmentRequest,
            @AuthenticationPrincipal Credential user) {

        PatientDTO patient = patientService.get(appointmentRequest.getPatientId())
                .orElseThrow(() -> new CustomException("Patient with given id not found", "PATIENT_NOT_FOUND",
                        HttpStatus.NOT_FOUND));

        if (checkPermissions(user, patient.getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to create another user's appointment",
                    "APPOINTMENT_CREATION_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }

        if (patient.getGender() == null || patient.getAge() == null || patient.getPhone() == null) {
            throw new CustomException("Patient with given id has incomplete details", "PATIENT_DETAILS_INCOMPLETE",
                    HttpStatus.BAD_REQUEST);
        }

        AppointmentDTO createdAppointment = appointmentService.create(appointmentRequest.getPatientId(), appointmentRequest.getScheduleId(), appointmentRequest.getAppointmentDetails());

        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/{aptId}/{sid}")
    public ResponseEntity<AppointmentDTO> updateAppointmentSchedule(@PathVariable Long aptId, @PathVariable Long sid,
            @AuthenticationPrincipal Credential user) {
        AppointmentDTO appointment = appointmentService.get(aptId).orElseThrow(() -> new CustomException(
                "Appointment with given aptId not found",
                "APPOINTMENT_NOT_FOUND", HttpStatus.NOT_FOUND));

        if (checkPermissions(user, appointment.getPatient().getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to edit another user's appointments",
                    "APPOINTMENT_EDIT_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }
        AppointmentDTO updatedAppointment = appointmentService.updateSchedule(aptId, sid)
                .orElseThrow(() -> new CustomException("Error while updating appointment", "APPOINTMENT_NOT_UPDATED",
                        HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @PutMapping("/{aptId}")
    public ResponseEntity<Void> updateAppointmentAttended(@PathVariable Long aptId) {
        appointmentService.markAsAttended(aptId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id,
            @AuthenticationPrincipal Credential user) {
        AppointmentDTO appointment = appointmentService.get(id)
                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                        "APPOINTMENT_NOT_FOUND", HttpStatus.NOT_FOUND));

        if (checkPermissions(user, appointment.getPatient().getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to delete another user's appointments",
                    "APPOINTMENT_DELETE_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }

        appointmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Boolean checkPermissions(Credential user, String email) {
        if (user.getRole().equals(Role.PATIENT) && !user.getEmail().equals(email))
            return false;
        return true;
    }

}
