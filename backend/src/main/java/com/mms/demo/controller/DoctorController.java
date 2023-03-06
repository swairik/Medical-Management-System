package com.mms.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
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
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Speciality;
import com.mms.demo.model.DoctorRequest;
import com.mms.demo.model.DoctorResponse;
import com.mms.demo.model.PatientRequest;
import com.mms.demo.model.SpecialityRequest;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ReportService;
import com.mms.demo.service.SpecialityService;

import jakarta.validation.Valid;

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
                .orElseThrow(() -> new RuntimeException("Doctor with given id not found"));

        DoctorResponse response = createResponseFromDoctor(dr);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/speciality")
    public ResponseEntity<List<DoctorResponse>> showDoctorById(
            @Valid @RequestBody SpecialityRequest specialityRequest) {
        Speciality speciality = createSpecialityFromRequest(specialityRequest);

        List<Doctor> doctors = doctorService.getDoctorBySpeciality(speciality);
        List<DoctorResponse> response = doctors.stream().map((d) -> createResponseFromDoctor(d))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest doctorRequest) {
        Doctor doctor = createDoctorFromRequest(doctorRequest);
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

    // TODO : Add report generation controllers

    public DoctorResponse createResponseFromDoctor(Doctor doctor) {
        DoctorResponse doctorResponse = new DoctorResponse();
        BeanUtils.copyProperties(doctor, doctorResponse);
        return doctorResponse;
    }

    public Doctor createDoctorFromRequest(DoctorRequest doctorRequest) {
        Speciality speciality = specialityService.getSpecialityById(doctorRequest.getSpecialityId())
                .orElseThrow(() -> new RuntimeException("Speciality with given id not found"));
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
