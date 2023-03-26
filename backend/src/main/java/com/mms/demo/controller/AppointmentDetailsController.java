package com.mms.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Feedback;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Prescription;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.AppointmentDetailsRequest;
import com.mms.demo.model.AppointmentDetailsResponse;
import com.mms.demo.service.AppointmentDetailsService;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.FeedbackService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.PrescriptionService;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.service.SlotService;

@RestController
@RequestMapping("/appointmentDetails")
public class AppointmentDetailsController {

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    SlotService slotService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PrescriptionService prescriptionService;

    @Autowired
    AppointmentDetailsService appointmentDetailsService;

    @Autowired
    FeedbackService feedbackService;

    @GetMapping("/display/{id}")
    public ResponseEntity<AppointmentDetailsResponse> showAppointmentById(@PathVariable Long id) {
        AppointmentDetails appointment = appointmentDetailsService.getById(id)
                .orElseThrow(() -> new CustomException("Appointment Details with given id not found",
                        "APPOINTMENT_DETAILS_NOT_FOUND"));
        AppointmentDetailsResponse response = AppointmentDetailsResponse
                .createResponseFromAppointmentDetails(appointment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/doctor/{did}")
    public ResponseEntity<List<AppointmentDetailsResponse>> showAppointmentByDoctorId(@PathVariable Long did) {
        Doctor doctor = doctorService.getDoctortById(did)
                .orElseThrow(() -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND"));
        List<AppointmentDetails> appointmentDetails = appointmentDetailsService.getAllByDoctor(doctor);
        List<AppointmentDetailsResponse> response = appointmentDetails.stream()
                .map((a) -> AppointmentDetailsResponse.createResponseFromAppointmentDetails(a))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/patient/{pid}")
    public ResponseEntity<List<AppointmentDetailsResponse>> showAppointmentByPatientId(@PathVariable Long pid) {
        Patient patient = patientService.getPatientById(pid)
                .orElseThrow(() -> new CustomException("Patient with given id not found", "PATIENT_NOT_FOUND"));
        List<AppointmentDetails> appointmentDetails = appointmentDetailsService.getAllByPatient(patient);
        List<AppointmentDetailsResponse> response = appointmentDetails.stream()
                .map((a) -> AppointmentDetailsResponse.createResponseFromAppointmentDetails(a))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/{did}/{pid}")
    public ResponseEntity<AppointmentDetailsResponse> showAppointmentByPatientDoctorStamp(@PathVariable Long did,
            @PathVariable Long pid, @RequestParam String stamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(stamp, formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT");
        }
        Patient patient = patientService.getPatientById(pid)
                .orElseThrow(() -> new CustomException("Patient with given id not found", "PATIENT_NOT_FOUND"));
        Doctor doctor = doctorService.getDoctortById(did)
                .orElseThrow(() -> new CustomException("Doctor with given id not found", "DOCTOR_NOT_FOUND"));

        List<AppointmentDetails> allAppointmentDetails = appointmentDetailsService.getAllByPatientAndDoctor(patient,
                doctor);
        AppointmentDetails appointmentDetails = allAppointmentDetails.stream()
                .filter((a) -> a.getStamp().equals(dateTime)).findFirst().orElse(null);
        if (appointmentDetails == null) {
            throw new CustomException("Appointment Details with given time stamp not found", "APPOINTMENT_NOT_FOUND");
        }
        AppointmentDetailsResponse response = AppointmentDetailsResponse
                .createResponseFromAppointmentDetails(appointmentDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<AppointmentDetailsResponse> createAppointment(
            @RequestBody AppointmentDetailsRequest appointmentDetailsRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(appointmentDetailsRequest.getAppointmentDateTime(), formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT");
        }

        Patient patient = patientService.getPatientById(appointmentDetailsRequest.getPatientId())
                .orElseThrow(() -> new CustomException("Patient with given id not found",
                        "PATIENT_NOT_FOUND"));
        Doctor doctor = doctorService.getDoctortById(appointmentDetailsRequest.getDoctorId())
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND"));

        String delimiter = ":,-";
        String prescriptionContent;
        if (appointmentDetailsRequest.getPrescriptionContentRequest() == null) {
            prescriptionContent = delimiter + delimiter;
        } else {
            prescriptionContent = appointmentDetailsRequest.getPrescriptionContentRequest().getMedication()
                    + delimiter + appointmentDetailsRequest.getPrescriptionContentRequest().getDiagnosis() + delimiter
                    + appointmentDetailsRequest.getPrescriptionContentRequest().getTest();
        }
        Prescription prescription = Prescription.builder()
                .contents(Base64.getEncoder().encode(prescriptionContent.getBytes()))
                .build();

        Feedback feedback = Feedback.builder()
                .contents(Base64.getEncoder()
                        .encode(appointmentDetailsRequest.getFeedbackRequest().getBytes()))
                .build();

        AppointmentDetails appointmentDetails = AppointmentDetails.builder()
                .doctor(doctor)
                .patient(patient)
                .prescription(prescription)
                .feedback(feedback)
                .stamp(dateTime)
                .build();

        AppointmentDetails createdAppointmentDetails = appointmentDetailsService.create(appointmentDetails)
                .orElseThrow(() -> new CustomException("Error while creating appointment details",
                        "APPOINTMENT_DETAILS_NOT_CREATED"));

        AppointmentDetailsResponse response = AppointmentDetailsResponse
                .createResponseFromAppointmentDetails(createdAppointmentDetails);
        // AppointmentDetailsResponse response = AppointmentDetailsResponse
        //         .createResponseFromAppointmentDetails(appointmentDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDetailsResponse> updateAppointment(@PathVariable Long id,
            @RequestBody AppointmentDetailsRequest appointmentDetailsRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(appointmentDetailsRequest.getAppointmentDateTime(), formatter);
        } catch (Exception e) {
            throw new CustomException("Wrong format of stamp", "WRONG_FORMAT");
        }

        Patient patient = patientService.getPatientById(appointmentDetailsRequest.getPatientId())
                .orElseThrow(() -> new CustomException("Patient with given id not found",
                        "PATIENT_NOT_FOUND"));
        Doctor doctor = doctorService.getDoctortById(appointmentDetailsRequest.getDoctorId())
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND"));

        String delimiter = ":,-";
        String prescriptionContent = appointmentDetailsRequest.getPrescriptionContentRequest().getMedication()
                + delimiter + appointmentDetailsRequest.getPrescriptionContentRequest().getDiagnosis() + delimiter
                + appointmentDetailsRequest.getPrescriptionContentRequest().getTest();
        Prescription prescription = Prescription.builder()
                .contents(Base64.getEncoder().encode(prescriptionContent.getBytes()))
                .build();

        Feedback feedback = Feedback.builder()
                .contents(Base64.getEncoder()
                        .encode(appointmentDetailsRequest.getFeedbackRequest().getBytes()))
                .build();

        AppointmentDetails appointmentDetails = AppointmentDetails.builder()
                .doctor(doctor)
                .patient(patient)
                .prescription(prescription)
                .feedback(feedback)
                .stamp(dateTime)
                .build();

        AppointmentDetails updatedAppointmentDetails = appointmentDetailsService.update(id, appointmentDetails)
                .orElseThrow(() -> new CustomException("Error while udpating appointment details",
                        "APPOINTMENT_DETAILS_NOT_UPDATED"));

        AppointmentDetailsResponse response = AppointmentDetailsResponse
                .createResponseFromAppointmentDetails(updatedAppointmentDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentDetails(@PathVariable Long id) {
        appointmentDetailsService.getById(id)
                .orElseThrow(() -> new CustomException("Appointment with given id not found", "APPOINTMENT_NOT_FOUND"));
        appointmentDetailsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
