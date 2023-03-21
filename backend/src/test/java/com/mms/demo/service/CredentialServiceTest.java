package com.mms.demo.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Credential;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class CredentialServiceTest {
    @Autowired
    CredentialService impl;

    static Credential cred;

    private String genAlnum(int targetStringLength) {
        int leftLimit = 48;
        int rightLimit = 122; 
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        
        return generatedString;
    }

    @Test
    @Order(1)
    @DisplayName("Testing create on a single credential")
    void testCreateCredentials() {
        cred = Credential.builder().email(genAlnum(10) + "@xyz.com").password("xyz").build();
        assertThat(impl.createCredentials(cred)).isEqualTo(cred);
    }

    @Order(2)
    @Test
    @DisplayName("Testing fetch on a single credential by email")
    void testGetCredentialsByEmail() {
        assertThat(impl.getCredentialsByEmail(cred.getEmail())).isNotEmpty().contains(cred);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single credential by ID")
    void testGetCredentialsById() {
        assertThat(impl.getCredentialsById(cred.getId())).isNotEmpty().contains(cred);
    }

    @Order(4)
    @Test
    @DisplayName("Testing update on a single credential by ID")
    void testUpdateCredentials() {
        Credential tempCreds = cred.toBuilder().email(genAlnum(10) + "@xyz.com").build();
        assertThat(impl.updateCredentials(cred.getId(), tempCreds)).isEqualTo(tempCreds).isNotEqualTo(cred);
    }

    @Test
    void testDeleteCredentials() {
        impl.deleteCredentials(cred.getId());
        assertThat(impl.getCredentialsById(cred.getId())).isEmpty();
    
    }
}
