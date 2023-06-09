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
    CredentialRepository repository;

    @Override
    public Optional<Credential> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Credential> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Credential create(Credential credentials) {
        return repository.save(credentials);
    }

    @Override
    public Credential update(Long id, Credential credentialsUpdates) {
        Optional<Credential> temp = repository.findById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Credential credentials = temp.get();
        credentials.setEmail(credentialsUpdates.getEmail());
        credentials.setPassword(credentialsUpdates.getPassword());
        credentials.setRole(credentialsUpdates.getRole());

        return repository.save(credentials);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
