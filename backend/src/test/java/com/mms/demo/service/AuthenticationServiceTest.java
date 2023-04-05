package com.mms.demo.service;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Role;
import com.mms.demo.model.AuthenticationRequest;
import com.mms.demo.model.RegisterRequest;
import com.mms.demo.transferobject.PatientDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    CredentialService credentialService;

    @Autowired
    PatientService patientService;

    @Autowired
    TokenService tokenService;

    private Credential admin = createCredentials(1L, "admin@email.com", "1234", Role.ADMIN);
    private Credential patient = createCredentials(2L, "patient@email.com", "1234", Role.PATIENT);
    private Credential doctor = createCredentials(3L, "doctor@email.com", "1234", Role.DOCTOR);

    private Credential createCredentials(Long id, String email, String password, Role role) {
        Credential credential = Credential.builder().id(id).email(email).password(password).role(role).build();
        return credential;
    }

    private String genAlnum(int targetStringLength) {
        int leftLimit = 48;
        int rightLimit = 122;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @Test
    @Order(1)
    @DisplayName("Test for patient creation")
    public void testCreate() {
        RegisterRequest registerRequest = RegisterRequest.builder().email(patient.getEmail())
                .password(patient.getPassword()).name("patient").role(patient.getRole())
                .build();
        assertThat(authenticationService.register(registerRequest).getMessage()).isEqualTo("User succesfully created");

        List<PatientDTO> allPatients = patientService.getAll();
        assertThat(allPatients.size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    @DisplayName("Test for patient login")
    public void testLogin() {
        List<PatientDTO> allPatients = patientService.getAll();
        if (allPatients.isEmpty()) {
            return;
        }
        PatientDTO patientDTO = allPatients.get(0);

        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().email(patient.getEmail())
                .password(patient.getPassword())
                .build();
        var response = authenticationService.authenticate(authenticationRequest);
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertThat(response.getId()).isEqualTo(patientDTO.getId());
    }

    @Test
    @Order(3)
    @DisplayName("Test for failed patient login")
    public void testLoginFail() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().email(patient.getEmail())
                .password(genAlnum(5))
                .build();
        var exception = assertThrows(org.springframework.security.authentication.BadCredentialsException.class, () -> authenticationService.authenticate(authenticationRequest));
        assertNotNull(exception);
        assertThat(exception.getMessage()).isEqualTo("Bad credentials");
    }

    @Test
    @Order(4)
    @DisplayName("Test for forgot Password")
    public void testForgotPassword() {
        assertThat(authenticationService.forgotPassword(patient.getEmail())).isEqualTo("Email has been sent");
    }

}
