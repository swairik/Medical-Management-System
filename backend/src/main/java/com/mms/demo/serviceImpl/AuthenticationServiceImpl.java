package com.mms.demo.serviceImpl;

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
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.RegisterRequest;
import com.mms.demo.model.RegisterResponse;
import com.mms.demo.service.AuthenticationService;
import com.mms.demo.service.CredentialService;
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
                                .build();
        }

        private void saveToken(String jwtToken) {
                Token token = Token.builder()
                                .identifier(jwtToken)
                                .build();
                tokenService.createToken(token);
        }

        private void revokeUserToken(String jwtToken) {
                tokenService.expireToken(jwtToken);
                tokenService.revokeToken(jwtToken);
        }

}
