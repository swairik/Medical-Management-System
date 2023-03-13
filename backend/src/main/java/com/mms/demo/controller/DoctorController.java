package com.mms.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Speciality;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.DoctorRequest;
import com.mms.demo.model.DoctorResponse;
import com.mms.demo.model.RegisterDoctorRequest;
import com.mms.demo.model.PatientRequest;
import com.mms.demo.model.SpecialityRequest;
import com.mms.demo.model.SpecialityResponse;
import com.mms.demo.service.CredentialService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ReportService;
import com.mms.demo.service.SpecialityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/doctor")
public class DoctorController {

        @Autowired
        DoctorService doctorService;

        @Autowired
        PatientService patientService;

        @Autowired
        SpecialityService specialityService;

        @Autowired
        ReportService reportService;

        @Autowired
        CredentialService credentialService;

        private final PasswordEncoder passwordEncoder;

        @GetMapping("/display")
        public ResponseEntity<List<DoctorResponse>> showAllDoctors() {
                List<Doctor> doctors = doctorService.getAllDoctors();
                List<DoctorResponse> response = doctors.stream().map((d) -> createResponseFromDoctor(d))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/{id}")
        public ResponseEntity<DoctorResponse> showDoctorById(@PathVariable Long id) {
                Doctor dr = doctorService.getDoctortById(id)
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));

                DoctorResponse response = createResponseFromDoctor(dr);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/speciality/{id}")
        public ResponseEntity<List<DoctorResponse>> showDoctorBySpeciality(@PathVariable Long id) {
                Speciality speciality = specialityService.getSpecialityById(id)
                                .orElseThrow(() -> new CustomException("Speciality with given id not found",
                                                "SPECIALITY_NOT_FOUND"));

                List<Doctor> doctors = doctorService.getDoctorBySpeciality(speciality);
                List<DoctorResponse> response = doctors.stream().map((d) -> createResponseFromDoctor(d))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PostMapping("/")
        public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody RegisterDoctorRequest doctorRequest) {
                List<Doctor> doctors = doctorService.getAllDoctors();
                Doctor doctorAlreadyCreatedWithEmail = doctors.stream().filter(
                                (d) -> d.getEmail().equals(doctorRequest.getEmail()))
                                .findFirst().orElse(null);
                if (doctorAlreadyCreatedWithEmail != null) {
                        throw new CustomException("Doctor with given email id already exists",
                                        "DOCTOR_ALREADY_CREATED");
                }
                Doctor doctorAlreadyCreatedWithPhone = doctors.stream().filter(
                                (d) -> d.getPhone().equals(doctorRequest.getPhone()))
                                .findFirst().orElse(null);
                if (doctorAlreadyCreatedWithPhone != null) {
                        throw new CustomException("Doctor with given phone already exists", "DOCTOR_ALREADY_CREATED");
                }

                Speciality speciality = specialityService.getSpecialityById(doctorRequest.getSpecialityId())
                                .orElseThrow(() -> new CustomException("Speciality with given id not found",
                                                "SPECIALITY_NOT_FOUND"));

                var credentials = Credential.builder()
                                .email(doctorRequest.getEmail())
                                .password(passwordEncoder.encode(doctorRequest.getPassword()))
                                .role(doctorRequest.getRole())
                                .build();

                credentialService.createCredentials(credentials);

                var doctor = Doctor
                                .builder()
                                .name(doctorRequest.getName())
                                .age(doctorRequest.getAge())
                                .gender(doctorRequest.getGender())
                                .email(doctorRequest.getEmail())
                                .phone(doctorRequest.getPhone())
                                .speciality(speciality)
                                .build();

                // Doctor doctor = createDoctorFromRequest(doctorRequest);
                Doctor createdDoctor = doctorService.createDoctor(doctor);
                DoctorResponse doctorResponse = createResponseFromDoctor(createdDoctor);
                return new ResponseEntity<>(doctorResponse, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id,
                        @Valid @RequestBody DoctorRequest doctorRequest) {
                Doctor doctor = createDoctorFromRequest(doctorRequest);
                Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
                DoctorResponse doctorResponse = createResponseFromDoctor(updatedDoctor);
                return new ResponseEntity<>(doctorResponse, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
                doctorService.deleteDoctor(id);
                return new ResponseEntity<>(HttpStatus.OK);
        }

        public DoctorResponse createResponseFromDoctor(Doctor doctor) {
                SpecialityResponse specialityResponse = createResponseFromSpeciality(doctor.getSpeciality());
                DoctorResponse doctorResponse = DoctorResponse.builder()
                                .id(doctor.getId())
                                .name(doctor.getName())
                                .age(doctor.getAge())
                                .email(doctor.getEmail())
                                .gender(doctor.getGender())
                                .phone(doctor.getPhone())
                                .speciality(specialityResponse)
                                .build();
                return doctorResponse;
        }

        public Doctor createDoctorFromRequest(DoctorRequest doctorRequest) {
                Speciality speciality = specialityService.getSpecialityById(doctorRequest.getSpecialityId())
                                .orElseThrow(() -> new CustomException("Speciality with given id not found",
                                                "SPECIALITY_NOT_FOUND"));
                Doctor doctor = Doctor
                                .builder()
                                .name(doctorRequest.getName())
                                .age(doctorRequest.getAge())
                                .gender(doctorRequest.getGender())
                                .email(doctorRequest.getEmail())
                                .phone(doctorRequest.getPhone())
                                .speciality(speciality)
                                .build();
                return doctor;
        }

        public Speciality createSpecialityFromRequest(SpecialityRequest specialityRequest) {
                Speciality speciality = Speciality.builder()
                                .name(specialityRequest.getName())
                                .build();
                return speciality;
        }

        public SpecialityResponse createResponseFromSpeciality(Speciality speciality) {
                SpecialityResponse specialityResponse = SpecialityResponse.builder()
                                .id(speciality.getId())
                                .name(speciality.getName())
                                .build();
                return specialityResponse;
        }

        public Patient createPatientFromRequest(PatientRequest patientRequest) {
                Patient patient = Patient.builder()
                                .name(patientRequest.getName())
                                .gender(patientRequest.getGender())
                                .age(patientRequest.getAge())
                                .email(patientRequest.getEmail())
                                .phone(patientRequest.getPhone())
                                .build();
                return patient;
        }

}