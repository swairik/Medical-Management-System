
package com.mms.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Prescription;
import com.mms.demo.entity.Role;
import com.mms.demo.entity.Schedule;
import com.mms.demo.exception.Custom403Exception;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.AppointmentResponse;
import com.mms.demo.model.PatientResponse;
import com.mms.demo.model.PrescriptionRequest;
import com.mms.demo.model.PrescriptionResponse;
import com.mms.demo.model.ScheduleResponse;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.PrescriptionService;
import com.mms.demo.service.ScheduleService;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

        @Autowired
        PrescriptionService prescriptionService;

        @Autowired
        PatientService patientService;

        @Autowired
        DoctorService doctorService;

        @Autowired
        AppointmentService appointmentService;

        @Autowired
        ScheduleService scheduleService;

        @GetMapping("/display/{id}")
        public ResponseEntity<PrescriptionResponse> showPrescriptionById(@PathVariable Long id) {
                Prescription prescription = prescriptionService.getPrescriptionById(id).orElseThrow(
                                () -> new CustomException("Prescription with given id not found",
                                                "PRESCRIPTION_NOT_FOUND"));

                Appointment appointment = appointmentService.getAppointmentById(prescription.getAppointment().getId())
                                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));
                AppointmentResponse appointmentResponse = createResponseFromAppointment(appointment);
                PrescriptionResponse response = PrescriptionResponse.createResponseFromPrescription(prescription,
                                appointmentResponse);

                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/stamp/{stamp}")
        public ResponseEntity<List<PrescriptionResponse>> showPrescriptionByStamp(@PathVariable String stamp) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(stamp, formatter);

                List<Prescription> prescriptions = prescriptionService.getPrescriptionsByStamp(dateTime);
                List<PrescriptionResponse> response = prescriptions.stream()
                                .map((p) -> {
                                        Appointment appointment = appointmentService
                                                        .getAppointmentById(p.getAppointment().getId())
                                                        .orElseThrow(() -> new CustomException(
                                                                        "Appointment with given id not found",
                                                                        "APPOINTMENT_NOT_FOUND"));
                                        AppointmentResponse appointmentResponse = createResponseFromAppointment(
                                                        appointment);

                                        return PrescriptionResponse.createResponseFromPrescription(p,
                                                        appointmentResponse);
                                })
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/stampBetween")
        public ResponseEntity<List<PrescriptionResponse>> showPrescriptionByStampBetween(@RequestParam String start,
                        @RequestParam String end) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime startTime, endTime;
                try {
                        startTime = LocalDateTime.parse(start, formatter);
                        endTime = LocalDateTime.parse(end, formatter);
                } catch (Exception e) {
                        throw new CustomException("Wrong format of timestamp", "WRONG_FORMAT");
                }
                List<Prescription> prescriptions = prescriptionService.getAllPrescriptionsByStampBetween(startTime,
                                endTime);
                List<PrescriptionResponse> response = prescriptions.stream()
                                .map((p) -> {
                                        Appointment appointment = appointmentService
                                                        .getAppointmentById(p.getAppointment().getId())
                                                        .orElseThrow(() -> new CustomException(
                                                                        "Appointment with given id not found",
                                                                        "APPOINTMENT_NOT_FOUND"));
                                        AppointmentResponse appointmentResponse = createResponseFromAppointment(
                                                        appointment);

                                        return PrescriptionResponse.createResponseFromPrescription(p,
                                                        appointmentResponse);
                                })
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/{did}/{pid}/{aptid}")
        public ResponseEntity<PrescriptionResponse> showPrescriptionByDoctorPatientAppointment(@PathVariable Long did,
                        @PathVariable Long pid, @PathVariable Long aptid) {
                List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
                Prescription prescription = prescriptions.stream().filter((p) -> p.getDoctor().getId() == did
                                && p.getPatient().getId() == pid && p.getAppointment().getId().equals(aptid))
                                .findFirst()
                                .orElse(null);
                if (prescription == null) {
                        throw new CustomException(
                                        "Prescription with given combination of doctor id, patient id & appointment id not found",
                                        "PRESCRIPTION_NOT_FOUND");
                }

                Appointment appointment = appointmentService
                                .getAppointmentById(prescription.getAppointment().getId())
                                .orElseThrow(() -> new CustomException(
                                                "Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));
                AppointmentResponse appointmentResponse = createResponseFromAppointment(
                                appointment);

                PrescriptionResponse response = PrescriptionResponse.createResponseFromPrescription(prescription,
                                appointmentResponse);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PostMapping("/")
        public ResponseEntity<PrescriptionResponse> createPrescription(
                        @RequestBody PrescriptionRequest prescriptionRequest,
                        @AuthenticationPrincipal Credential user) {

                Doctor doctor = doctorService.getDoctortById(prescriptionRequest.getDoctorId())
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));

                if (checkPermissions(user, doctor.getEmail()) == false) {
                        throw new Custom403Exception(
                                        "Logged in user is not permitted to create another user's prescription",
                                        "PROFILE_EDIT_NOT_ALLOWED");
                }
                // if apt id already there throw exception

                List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
                Prescription alreadyCreatedPrescription = prescriptions.stream()
                                .filter((p) -> p.getAppointment().getId() == prescriptionRequest.getAppointmentId())
                                .findFirst().orElse(null);

                if (alreadyCreatedPrescription != null) {
                        throw new CustomException("Prescription with given appointment id already created",
                                        "APPOINTMENT_ALREADY_CREATED");
                }

                Prescription prescription = createPrescriptionFromRequest(prescriptionRequest);
                Prescription createdPrescription = prescriptionService.createPrescription(prescription);

                Appointment appointment = appointmentService.getAppointmentById(prescription.getAppointment().getId())
                                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));
                AppointmentResponse appointmentResponse = createResponseFromAppointment(appointment);

                PrescriptionResponse response = PrescriptionResponse
                                .createResponseFromPrescription(createdPrescription, appointmentResponse);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<PrescriptionResponse> updatePrescription(@PathVariable Long id,
                        @RequestBody PrescriptionRequest prescriptionRequest,
                        @AuthenticationPrincipal Credential user) {
                Doctor doctor = doctorService.getDoctortById(prescriptionRequest.getDoctorId())
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));

                if (checkPermissions(user, doctor.getEmail()) == false) {
                        throw new Custom403Exception(
                                        "Logged in user is not permitted to edit another user's prescription",
                                        "PROFILE_EDIT_NOT_ALLOWED");
                }

                Prescription prescription = createPrescriptionFromRequest(prescriptionRequest);
                Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescription).orElseThrow(
                                () -> new CustomException("Prescription with given id not found",
                                                "PRESCRIPTION_NOT_FOUND"));

                Appointment appointment = appointmentService.getAppointmentById(prescription.getAppointment().getId())
                                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));
                AppointmentResponse appointmentResponse = createResponseFromAppointment(appointment);

                PrescriptionResponse response = PrescriptionResponse
                                .createResponseFromPrescription(updatedPrescription, appointmentResponse);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
                prescriptionService.deletePrescription(id);
                return new ResponseEntity<>(HttpStatus.OK);
        }

        public Prescription createPrescriptionFromRequest(PrescriptionRequest prescriptionRequest) {
                Patient patient = patientService.getPatientById(prescriptionRequest.getPatientId())
                                .orElseThrow(() -> new CustomException("Patient with given id not found",
                                                "PATIENT_NOT_FOUND"));
                Doctor doctor = doctorService.getDoctortById(prescriptionRequest.getDoctorId())
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));

                Appointment appointment = appointmentService
                                .getAppointmentById(prescriptionRequest.getAppointmentId())
                                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));

                String delimiter = ":,-";

                String contentsRequest = prescriptionRequest.getContents().getMedication() + delimiter
                                + prescriptionRequest.getContents().getDiagnosis() + delimiter
                                + prescriptionRequest.getContents().getTest();

                byte[] contents = Base64.getEncoder().encode(contentsRequest.getBytes());

                Prescription prescription = Prescription.builder()
                                .doctor(doctor)
                                .patient(patient)
                                .appointment(appointment)
                                .contents(contents)
                                .build();
                return prescription;
        }

        public AppointmentResponse createResponseFromAppointment(Appointment appointment) {
                PatientResponse patientResponse = PatientResponse.createResponseFromPatient(appointment.getPatient());
                List<Schedule> schedules = scheduleService.getAllSchedules();
                List<Schedule> filteredSchedules = schedules.stream()
                                .filter((s) -> {
                                        return s.getSlot().getId() == appointment.getSlot().getId();
                                })
                                .collect(Collectors.toList());
                if (filteredSchedules.size() == 0) {
                        throw new CustomException("Schedule does not exist", "SCHEDULE_NOT_FOUND");
                }
                ScheduleResponse scheduleResponse = ScheduleResponse
                                .createResponseFromSchedule(filteredSchedules.get(0));
                AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                                .id(appointment.getId())
                                .patientResponse(patientResponse)
                                .scheduleResponse(scheduleResponse)
                                .attended(appointment.getAttended())
                                .build();
                return appointmentResponse;
        }

        private Boolean checkPermissions(Credential user, String email) {
                if (user.getRole().equals(Role.DOCTOR) && !user.getEmail().equals(email))
                        return false;
                return true;
        }

}
