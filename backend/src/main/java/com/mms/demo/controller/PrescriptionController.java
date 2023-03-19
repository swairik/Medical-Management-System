
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

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Prescription;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.PrescriptionRequest;
import com.mms.demo.model.PrescriptionResponse;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.PrescriptionService;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

        @Autowired
        PrescriptionService prescriptionService;

        @Autowired
        PatientService patientService;

        @Autowired
        DoctorService doctorService;

        @GetMapping("/display/{id}")
        public ResponseEntity<PrescriptionResponse> showPrescriptionById(@PathVariable Long id) {
                Prescription prescription = prescriptionService.getPrescriptionById(id).orElseThrow(
                                () -> new CustomException("Prescription with given id not found",
                                                "PRESCRIPTION_NOT_FOUND"));

                PrescriptionResponse response = PrescriptionResponse.createResponseFromPrescription(prescription);

                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/stamp/{stamp}")
        public ResponseEntity<List<PrescriptionResponse>> showPrescriptionByStamp(@PathVariable String stamp) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(stamp, formatter);

                List<Prescription> prescriptions = prescriptionService.getPrescriptionsByStamp(dateTime);
                List<PrescriptionResponse> response = prescriptions.stream()
                                .map((p) -> PrescriptionResponse.createResponseFromPrescription(p))
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
                                .map((p) -> PrescriptionResponse.createResponseFromPrescription(p))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PostMapping("/")
        public ResponseEntity<PrescriptionResponse> createPrescription(
                        @RequestBody PrescriptionRequest prescriptionRequest) {
                Prescription prescription = createPrescriptionFromRequest(prescriptionRequest);
                Prescription createdPrescription = prescriptionService.createPrescription(prescription);
                PrescriptionResponse response = PrescriptionResponse
                                .createResponseFromPrescription(createdPrescription);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<PrescriptionResponse> updatePrescription(@PathVariable Long id,
                        @RequestBody PrescriptionRequest prescriptionRequest) {
                Prescription prescription = createPrescriptionFromRequest(prescriptionRequest);
                Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescription).orElseThrow(
                                () -> new CustomException("Prescription with given id not found",
                                                "PRESCRIPTION_NOT_FOUND"));
                PrescriptionResponse response = PrescriptionResponse
                                .createResponseFromPrescription(updatedPrescription);
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

                String delimiter = ":,-";

                String contentsRequest = prescriptionRequest.getContents().getMedication() + delimiter
                                + prescriptionRequest.getContents().getDiagnosis() + delimiter
                                + prescriptionRequest.getContents().getTest();

                byte[] contents = Base64.getEncoder().encode(contentsRequest.getBytes());

                Prescription prescription = Prescription.builder()
                                .doctor(doctor)
                                .patient(patient)
                                .contents(contents)
                                .build();
                return prescription;
        }

}
