package com.mms.demo.controller;

import java.util.List;
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

import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Role;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.DoctorRequest;
import com.mms.demo.model.EmailDetails;
import com.mms.demo.model.RegisterDoctorRequest;
import com.mms.demo.service.CredentialService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.EmailService;
import com.mms.demo.service.JwtService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ReportService;
import com.mms.demo.service.SpecialityService;
import com.mms.demo.transferobject.DoctorDTO;
import com.mms.demo.transferobject.SpecialityDTO;

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

    @Autowired
    EmailService emailService;

    @Autowired
    JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/display")
    public ResponseEntity<List<DoctorDTO>> showAllDoctors() {
        List<DoctorDTO> doctorList = doctorService.getAll();
        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<DoctorDTO> showDoctorById(@PathVariable Long id) {
        DoctorDTO doctor = doctorService.get(id)
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND", HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping("/display/speciality/{id}")
    public ResponseEntity<List<DoctorDTO>> showDoctorBySpeciality(@PathVariable Long id) {
        SpecialityDTO speciality = specialityService.get(id)
                .orElseThrow(() -> new CustomException("Speciality with given id not found", "SPECIALITY_NOT_FOUND",
                        HttpStatus.NOT_FOUND));

        List<DoctorDTO> doctors = doctorService.getAllBySpeciality(speciality.getId());
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody RegisterDoctorRequest doctorRequest) {
        List<DoctorDTO> doctors = doctorService.getAll();
        DoctorDTO doctorAlreadyCreatedWithEmail = doctors.stream()
                .filter((d) -> d.getEmail().equals(doctorRequest.getEmail())).findFirst()
                .orElse(null);
        if (doctorAlreadyCreatedWithEmail != null) {
            throw new CustomException("Doctor with given email id already exists",
                    "DOCTOR_ALREADY_CREATED", HttpStatus.CONFLICT);
        }
        DoctorDTO doctorAlreadyCreatedWithPhone = doctors.stream()
                .filter((d) -> d.getPhone().equals(doctorRequest.getPhone())).findFirst()
                .orElse(null);
        if (doctorAlreadyCreatedWithPhone != null) {
            throw new CustomException("Doctor with given phone already exists",
                    "DOCTOR_ALREADY_CREATED", HttpStatus.CONFLICT);
        }

        String password = "password";

        var credentials = Credential.builder().email(doctorRequest.getEmail())
                .password(passwordEncoder.encode(password)).role(Role.DOCTOR).build();

        DoctorDTO doctor = DoctorDTO.builder()
                .name(doctorRequest.getName())
                .gender(doctorRequest.getGender())
                .age(doctorRequest.getAge())
                .email(doctorRequest.getEmail())
                .phone(doctorRequest.getPhone())
                .build();

        DoctorDTO createdDoctor = doctorService.create(doctor, doctorRequest.getSpecialityId());
        credentialService.create(credentials);

        String token = jwtService.generateToken(credentials);

        String subject = "Account has been created";

        String msgBody = "This email id has been registered as a doctor in Care4u.\n" +
                "Following are the user details : \n" +
                "Name : " + createdDoctor.getName() + "\n" +
                "Email : " + createdDoctor.getEmail() + "\n" +
                "Password : " + password + "\n" +
                "Gender : " + (createdDoctor.getGender() == "M" ? "Female" : "Male") + "\n" +
                "Age : " + createdDoctor.getAge() + "\n" +
                "Phone : " + createdDoctor.getPhone() + "\n" +
                "Speciality : " + createdDoctor.getSpeciality().getName() + "\n" +
                "To reset your password click on the link : " + "\n" +
                "http://localhost:8080/UpdatePassword?token=" + token + "&email=" + createdDoctor.getEmail();

        EmailDetails emailDetails = EmailDetails.builder().recipient(createdDoctor.getEmail()).subject(subject)
                .msgBody(msgBody).build();
        emailService.sendSimpleMail(emailDetails);

        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    @PutMapping("/{did}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long did,
            @Valid @RequestBody DoctorRequest doctorRequest,
            @AuthenticationPrincipal Credential user) {

        DoctorDTO doctor = doctorService.get(did)
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND", HttpStatus.NOT_FOUND));

        if (checkPermissions(user, doctor.getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to edit another user's profile",
                    "PROFILE_EDIT_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }
        
        DoctorDTO updateDoctor = DoctorDTO.builder()
                .name(doctorRequest.getName())
                .age(doctorRequest.getAge())
                .gender(doctorRequest.getGender())
                .phone(doctorRequest.getPhone())
                .build();

        DoctorDTO updatedDoctor = doctorService.update(did, updateDoctor)
                .orElseThrow(() -> new CustomException("Error while updating doctor", "DOCTOR_NOT_UPDATED",
                        HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }

    @PutMapping("/{did}/speciality/{specialityId}")
    public ResponseEntity<DoctorDTO> updateDoctorSpeciality(@PathVariable Long did, @PathVariable Long specialityId,
            @AuthenticationPrincipal Credential user) {
        DoctorDTO doctor = doctorService.get(did)
                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                        "DOCTOR_NOT_FOUND", HttpStatus.NOT_FOUND));

        if (checkPermissions(user, doctor.getEmail()) == false) {
            throw new CustomException(
                    "Logged in user is not permitted to edit another user's profile",
                    "PROFILE_EDIT_NOT_ALLOWED", HttpStatus.FORBIDDEN);
        }

        DoctorDTO updatedDoctor = doctorService.updateSpeciality(did, specialityId)
                .orElseThrow(() -> new CustomException("Error while updating doctor", "DOCTOR_NOT_UPDATED",
                        HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        // doctorService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Boolean checkPermissions(Credential user, String email) {
        if (user.getRole().equals(Role.DOCTOR) && !user.getEmail().equals(email))
            return false;
        return true;
    }

}
