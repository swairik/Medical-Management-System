package com.mms.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Credential;

/**
 * CredentialRepository defines an interface to generate JPA defined queries to interact with the
 * Credentials table.
 * 
 * @author Mananveer Singh
 */
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    /**
     * Find the credential that has the given email address contained in it.
     *
     * @param email the email
     * @return the credentials with the specified email
     */
    Optional<Credential> findByEmail(String email);
}
