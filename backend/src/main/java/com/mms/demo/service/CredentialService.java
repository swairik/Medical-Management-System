package com.mms.demo.service;
import java.util.Optional;

import com.mms.demo.entity.Credential;

public interface CredentialService {
    Optional<Credential> getCredentialsById(Long id);
    Optional<Credential> getCredentialsByEmail(String email);
    
    Credential createCredentials(Credential credentials);
    Credential updateCredentials(Long id, Credential credentials);
    void deleteCredentials(Long id);
}
