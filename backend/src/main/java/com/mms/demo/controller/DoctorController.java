package com.mms.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Role;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Speciality;
import com.mms.demo.exception.Custom403Exception;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.DoctorRequest;
import com.mms.demo.model.DoctorResponse;
import com.mms.demo.model.EmailDetails;
import com.mms.demo.model.RegisterDoctorRequest;
import com.mms.demo.model.RegisterDoctorResponse;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.CredentialService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.EmailService;
import com.mms.demo.service.JwtService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ReportService;
import com.mms.demo.service.ScheduleService;
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
    ScheduleService scheduleService;

    @Autowired
    AppointmentService appointmentService;
    
    @Autowired
    SpecialityService specialityService;

    @Autowired
    ReportService reportService;

    @Autowired
    CredentialService credentialService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/display")
    public ResponseEntity<List<DoctorResponse>> showAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorResponse> response = doctors.stream().map((d) -> DoctorResponse.createResponseFromDoctor(d))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<DoctorResponse> showDoctorById(@PathVariable Long id) {
        Doctor dr = doctorService.getDoctortById(id)
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND"));

        DoctorResponse response = DoctorResponse.createResponseFromDoctor(dr);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/speciality/{id}")
    public ResponseEntity<List<DoctorResponse>> showDoctorBySpeciality(@PathVariable Long id) {
        Speciality speciality = specialityService.getSpecialityById(id)
                .orElseThrow(() -> new CustomException("Speciality with given id not found",
                        "SPECIALITY_NOT_FOUND"));

        List<Doctor> doctors = doctorService.getDoctorBySpeciality(speciality);
        List<DoctorResponse> response = doctors.stream().map((d) -> DoctorResponse.createResponseFromDoctor(d))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<RegisterDoctorResponse> createDoctor(
            @Valid @RequestBody RegisterDoctorRequest doctorRequest) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        Doctor doctorAlreadyCreatedWithEmail = doctors.stream()
                .filter((d) -> d.getEmail().equals(doctorRequest.getEmail())).findFirst()
                .orElse(null);
        if (doctorAlreadyCreatedWithEmail != null) {
            throw new CustomException("Doctor with given email id already exists",
                    "DOCTOR_ALREADY_CREATED");
        }
        Doctor doctorAlreadyCreatedWithPhone = doctors.stream()
                .filter((d) -> d.getPhone().equals(doctorRequest.getPhone())).findFirst()
                .orElse(null);
        if (doctorAlreadyCreatedWithPhone != null) {
            throw new CustomException("Doctor with given phone already exists",
                    "DOCTOR_ALREADY_CREATED");
        }

        Speciality speciality = specialityService.getSpecialityById(doctorRequest.getSpecialityId())
                .orElseThrow(() -> new CustomException("Speciality with given id not found",
                        "SPECIALITY_NOT_FOUND"));

        String password = doctorRequest.getEmail().substring(0, 3) + "passsword";

        var credentials = Credential.builder().email(doctorRequest.getEmail())
                .password(passwordEncoder.encode(password)).role(Role.DOCTOR).build();

        var doctor = Doctor.builder().name(doctorRequest.getName()).age(doctorRequest.getAge())
                .gender(doctorRequest.getGender()).email(doctorRequest.getEmail())
                .phone(doctorRequest.getPhone()).speciality(speciality).build();

        Doctor createdDoctor = doctorService.createDoctor(doctor);
        credentialService.createCredentials(credentials);

        String token = jwtService.generateToken(credentials);

        String subject = "Account has been created";

        String msgBody = "This email id has been registered as a doctor in Care4u.\n" +
                "Following are the user details : \n" +
                "Name : " + doctor.getName() + "\n" +
                "Email : " + doctor.getEmail() + "\n" +
                "Password : " + password + "\n" +
                "Gender : " + (doctor.getGender() == "M" ? "Female" : "Male") + "\n" +
                "Age : " + doctor.getAge() + "\n" +
                "Phone : " + doctor.getPhone() + "\n" +
                "Speciality : " + doctor.getSpeciality().getName() + "\n" +
                "To reset your password click on the link : " + "\n" +
                "http://localhost:8080/UpdatePassword?token=" + token + "&email=" + doctor.getEmail();

        EmailDetails emailDetails = EmailDetails.builder().recipient(doctor.getEmail()).subject(subject)
                .msgBody(msgBody).build();
        emailService.sendSimpleMail(emailDetails);

        RegisterDoctorResponse doctorResponse = RegisterDoctorResponse.createResponseFromDoctor(createdDoctor);
        doctorResponse.setPassword(password);
        return new ResponseEntity<>(doctorResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id,
            @Valid @RequestBody DoctorRequest doctorRequest,
            @AuthenticationPrincipal Credential user) {

        Doctor doctor = doctorService.getDoctortById(id)
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND"));

        if (checkPermissions(user, doctor.getEmail()) == false) {
            throw new Custom403Exception(
                    "Logged in user is not permitted to edit another user's profile",
                    "PROFILE_EDIT_NOT_ALLOWED");
        }

        Doctor updateDoctor = createDoctorFromRequest(doctorRequest);
        updateDoctor.setEmail(doctor.getEmail());
        Doctor updatedDoctor = doctorService.updateDoctor(id, updateDoctor);
        DoctorResponse doctorResponse = DoctorResponse.createResponseFromDoctor(updatedDoctor);
        return new ResponseEntity<>(doctorResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctortById(id)
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND"));
        Credential credential = credentialService.getCredentialsByEmail(doctor.getEmail())
                .orElseThrow(() -> new CustomException("Doctor credentials not found",
                        "CREDENTIAL_NOT_FOUND"));
        // List<Schedule> schedules = scheduleService.getSchedulesByDoctor(doctor);
        // List<Appointment> appointments = appointmentService.getAllAppointments().stream().filter((a) -> {
        //         Schedule schedule = schedules.stream().filter((sched) -> sched.getSlot().getId() == a.getSlot().getId()).findFirst().orElse(null);
        //         return schedule != null;
        // }).collect(Collectors.toList());
        // appointments.stream().forEach((a) -> appointmentService.deleteAppointment(a.getId()));
        // schedules.stream().forEach((s) -> scheduleService.deleteSchedule(s.getId()));

        // doctorService.deleteDoctor(id);
        // credentialService.deleteCredentials(credential.getId());

        // doctor.setApproval(false);
        // doctorService.updateDoctor(doctor.getId(), doctor);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Doctor createDoctorFromRequest(DoctorRequest doctorRequest) {
        Speciality speciality = specialityService.getSpecialityById(doctorRequest.getSpecialityId())
                .orElseThrow(() -> new CustomException("Speciality with given id not found",
                        "SPECIALITY_NOT_FOUND"));
        Doctor doctor = Doctor.builder().name(doctorRequest.getName()).age(doctorRequest.getAge())
                .gender(doctorRequest.getGender()).phone(doctorRequest.getPhone())
                .speciality(speciality).build();
        return doctor;
    }

    private Boolean checkPermissions(Credential user, String email) {
        if (user.getRole().equals(Role.DOCTOR) && !user.getEmail().equals(email))
            return false;
        return true;
    }

}
