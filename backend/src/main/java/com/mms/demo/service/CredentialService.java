package com.mms.demo.service;

import java.util.Optional;

import com.mms.demo.entity.Credential;

/**
 * The Interface CredentialService defines all the interactions needed between a high level
 * controller and the corresponding repository.
 * 
 * @author Mananveer Singh
 */
public interface CredentialService {
    /**
     * Gets the credentials by id.
     *
     * @param id the id
     * @return the credentials by id
     */
    Optional<Credential> getCredentialsById(Long id);

    /**
     * Gets the credentials by email.
     *
     * @param email the email
     * @return the credentials by email
     */
    Optional<Credential> getCredentialsByEmail(String email);

    /**
     * Creates credentials.
     *
     * @param credentials the credentials
     * @return the credentials, on successful creation
     */
    Credential createCredentials(Credential credentials);

    /**
     * Update credentials.
     *
     * @param id the id
     * @param credentials the credentials
     * @return the credential
     */
    Credential updateCredentials(Long id, Credential credentials);

    /**
     * Delete credentials.
     *
     * @param id the id
     */
    void deleteCredentials(Long id);
}
