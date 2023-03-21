package com.mms.demo.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;
import com.mms.demo.entity.Speciality;
import com.mms.demo.entity.Token;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class TokenRepositoryTest {
    @Autowired
    TokenRepository repository;

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
    @Transactional
    void testDeleteAllExpiredTokensBefore() {
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Token temp = Token.builder().identifier(genAlnum(255)).expirationStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).build();
            repository.save(temp);
            tokens.add(temp);

            assertThat(repository.findById(temp.getId())).isNotEmpty().contains(temp);
        }

        repository.deleteAllByExpirationStampBefore(LocalDateTime.now());

        for (Token token : tokens) {
            assertThat(repository.findById(token.getId())).isEmpty();
        }
    }
}
