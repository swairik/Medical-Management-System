package com.mms.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Credential;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Role;
import com.mms.demo.entity.Token;
import com.mms.demo.model.AuthenticationRequest;
import com.mms.demo.model.AuthenticationResponse;
import com.mms.demo.model.EmailDetails;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.PasswordRequest;
import com.mms.demo.model.RegisterRequest;
import com.mms.demo.model.RegisterResponse;
import com.mms.demo.service.AuthenticationService;
import com.mms.demo.service.CredentialService;
import com.mms.demo.service.EmailService;
import com.mms.demo.service.JwtService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.var;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

        private final PatientService patientService;
        private final CredentialService credentialService;

        private final PasswordEncoder passwordEncoder;

        private final JwtService jwtService;

        private final TokenService tokenService;

        private final AuthenticationManager authenticationManager;

        @Autowired
        private EmailService emailService;

        @Override
        public RegisterResponse register(RegisterRequest registerRequest) {
                Credential alreadyCreatedCredential = credentialService
                                .getCredentialsByEmail(registerRequest.getEmail()).orElse(null);

                if (alreadyCreatedCredential != null) {
                        throw new CustomException("User with email already exists", "USER_ALREADY_CREATED");
                }

                var credentials = Credential.builder()
                                .email(registerRequest.getEmail())
                                .password(passwordEncoder.encode(registerRequest.getPassword()))
                                .role(registerRequest.getRole())
                                .build();

                credentialService.createCredentials(credentials);

                if (registerRequest.getRole() == Role.PATIENT) {
                        var patient = Patient.builder()
                                        .name(registerRequest.getName())
                                        .gender(registerRequest.getGender())
                                        .age(registerRequest.getAge())
                                        .email(registerRequest.getEmail())
                                        .phone(registerRequest.getPhone())
                                        .build();

                        patientService.createPatient(patient);
                }

                return RegisterResponse.builder()
                                .message("User succesfully created")
                                .build();

        }

        @Override
        public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                                                authenticationRequest.getPassword()));
                var user = credentialService.getCredentialsByEmail(authenticationRequest.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException("User email not found"));
                var jwtToken = jwtService.generateToken(user);

                // TODO : Fix issue for multiple login for same user - revoke their old token
                // access
                saveToken(jwtToken);

                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .role(user.getRole())
                                .build();
        }

        @Override
        public String forgotPassword(String email) {
                var user = credentialService.getCredentialsByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User email not found"));
                
                String token = jwtService.generateToken(user);

                String subject = "Reset your Password";

                String msgBody = "To reset your password click on the link : \n" +
                                "http://localhost:8080/UpdatePassword?token="+token+"&email="+email;

                EmailDetails emailDetails = EmailDetails.builder()
                                .recipient(email)
                                .subject(subject)
                                .msgBody(msgBody)
                                .build();
                String response = emailService.sendSimpleMail(emailDetails);
                if (response.equals("Error")) {
                        throw new CustomException("Error while sending email", "EMAIL_NOT_SENT");
                }
                user.setResetToken(token);
                credentialService.updateCredentials(user.getId(), user);
                return response;
        }

        public String updatePassword(PasswordRequest passwordRequest) {
                var user = credentialService.getCredentialsByEmail(passwordRequest.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException("User email not found"));
                if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {
                        throw new CustomException("Passwords do not match", "PASSWORD_NOT_MATCHED");
                }
                if (user.getResetToken() == null) {
                        throw new CustomException("No reset token found in database", "RESET_TOKEN_NOT_FOUND");
                }
                if (!passwordRequest.getToken().equals(user.getResetToken())) {
                        throw new CustomException("Reset token incorrect", "INCORRECT_RESET_TOKEN");
                }

                if (!jwtService.isTokenValid(passwordRequest.getToken(), user)) {
                        throw new CustomException("Token is invalid", "INVALID_RESET_TOKEN");
                }

                user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
                user.setResetToken(null);
                credentialService.updateCredentials(user.getId(), user);

                return "Password updated successfully";
        }

        private void saveToken(String jwtToken) {
                Token token = Token.builder()
                                .identifier(jwtToken)
                                .build();
                tokenService.createToken(token);
        }

        // private void revokeUserToken(String jwtToken) {
        // tokenService.expireToken(jwtToken);
        // tokenService.revokeToken(jwtToken);
        // }

}
