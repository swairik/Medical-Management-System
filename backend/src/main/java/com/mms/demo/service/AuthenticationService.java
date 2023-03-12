package com.mms.demo.service;

import com.mms.demo.model.AuthenticationRequest;
import com.mms.demo.model.AuthenticationResponse;
import com.mms.demo.model.PasswordRequest;
import com.mms.demo.model.RegisterRequest;
import com.mms.demo.model.RegisterResponse;

public interface AuthenticationService {

    public RegisterResponse register(RegisterRequest registerRequest);

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    public String forgotPassword(String email);

    public String updatePassword(PasswordRequest passwordRequest);

}
