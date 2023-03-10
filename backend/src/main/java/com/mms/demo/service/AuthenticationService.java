package com.mms.demo.service;

import com.mms.demo.model.AuthenticationRequest;
import com.mms.demo.model.AuthenticationResponse;
import com.mms.demo.model.RegisterRequest;

public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest registerRequest);

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

}
