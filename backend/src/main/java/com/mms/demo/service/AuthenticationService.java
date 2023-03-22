package com.mms.demo.service;

import com.mms.demo.model.AuthenticationRequest;
import com.mms.demo.model.AuthenticationResponse;
import com.mms.demo.model.PasswordRequest;
import com.mms.demo.model.RegisterRequest;
import com.mms.demo.model.RegisterResponse;

/**
 * The Interface AuthenticationService defines all the interactions needed between a high level
 * controller and the corresponding repository.
 * 
 * @author Swairik Dey
 */
public interface AuthenticationService {

    /**
     * Creates a user and credential pair.
     * 
     * @param registerRequest contains the details for the registration of a new user
     * @return the results of the attempted creation
     */
    public RegisterResponse register(RegisterRequest registerRequest);

    /**
     * Authenticates and on success creates an authentication token.
     * 
     * @param authenticationRequest contains the credentials used to perform authentication
     * @return the result of the authentication. Contains the authentication token.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    /**
     * Generates a password reset request.
     * 
     * @param email the email that needs its password reset
     * @return a string on successfully sending the reset email
     */
    public String forgotPassword(String email);

    /**
     * Authenticates a password reset request and updates the saved password.
     * 
     * @param passwordRequest contains the necessary details to identify the validity of a reset
     *        request
     * @return a string on successful completion of the operation
     */
    public String updatePassword(PasswordRequest passwordRequest);

}
