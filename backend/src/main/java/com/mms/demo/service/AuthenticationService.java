package com.mms.demo.service;

import com.mms.demo.model.AuthenticationRequest;
import com.mms.demo.model.AuthenticationResponse;
import com.mms.demo.model.RegisterRequest;
import com.mms.demo.model.RegisterResponse;

public interface AuthenticationService {

    public RegisterResponse register(RegisterRequest registerRequest);

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

}
