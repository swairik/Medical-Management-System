package com.mms.demo.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Credential;
import com.mms.demo.repository.CredentialRepository;
import com.mms.demo.service.CredentialService;

@Service
public class CredentialServiceImpl implements CredentialService {
    @Autowired
    CredentialRepository credRepo;

    @Override
    public Optional<Credential> getCredentialsById(Long id) {
        return credRepo.findById(id);
    }

    @Override
    public Optional<Credential> getCredentialsByEmail(String email) {
        return credRepo.findByEmail(email);
    }

    @Override
    public Credential createCredentials(Credential credentials) {
        return credRepo.save(credentials);
    }

    @Override
    public Credential updateCredentials(Long id, Credential credentialsUpdates) {
        Optional<Credential> temp = credRepo.findById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Credential credentials = temp.get();
        credentials.setEmail(credentialsUpdates.getEmail());
        credentials.setPassword(credentialsUpdates.getPassword());
        credentials.setRole(credentialsUpdates.getRole());
        // credentials.setIsAdmin(credentialsUpdates.getIsAdmin());
        // credentials.setIsPatient(credentialsUpdates.getIsPatient());

        return credRepo.save(credentials);
    }

    @Override
    public void deleteCredentials(Long id) {
        credRepo.deleteById(id);
    }

}
