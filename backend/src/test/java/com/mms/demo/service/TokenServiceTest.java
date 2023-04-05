package com.mms.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mms.demo.entity.Token;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class TokenServiceTest {
    @Autowired
    TokenService tokenService;

    static Token token;

    @Order(1)
    @Test
    @DisplayName("Testing create on a single token")
    void testCreateToken() {
        token = Token.builder().identifier("somestring")
                .expirationStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        assertThat(tokenService.createToken(token))
                .isNotNull()
                .extracting("isRevoked", "type")
                .containsExactly(false, "BEARER");

    }

    @Order(2)
    @Test
    @DisplayName("Testing fetch on a single token by ID")
    void testGetTokenById() {
        assertThat(tokenService.getTokenById(token.getId())).isNotEmpty().contains(token);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single token by ID")
    void testGetTokenByIdentifier() {
        assertThat(tokenService.getTokenByIdentifier(token.getIdentifier())).isNotEmpty().contains(token);
    }

    @Order(6)
    @Test
    @DisplayName("Testing revoke on a single token by ID")
    void testRevokeToken() {
        Token temp = Token.builder().identifier("test3")
                .expirationStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        assertThat(tokenService.createToken(temp)).isNotNull().isEqualTo(temp);

        assertThat(tokenService.revokeToken(temp.getId()))
                .isPresent()
                .map(val -> val.getIsRevoked())
                .hasValue(true);
    }

    @Order(7)
    @Test
    @DisplayName("Testing revoke on a single token by identifier")
    void testRevokeToken2() {
        Token temp = Token.builder().identifier("test4")
                .expirationStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        assertThat(tokenService.createToken(temp)).isNotNull().isEqualTo(temp);

        assertThat(tokenService.revokeToken(temp.getIdentifier()))
                .isPresent()
                .map(val -> val.getIsRevoked())
                .hasValue(true);
    }

    @Order(8)
    @Test
    @DisplayName("Testing update on a single token by ID")
    void testUpdateToken() {
        Token temp = token.toBuilder().isRevoked(true)
                .expirationStamp(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))
                .build();

        assertThat(tokenService.updateToken(token.getId(), temp))
                .isNotNull()
                .isEqualTo(temp)
                .isNotEqualTo(token);
    }

    @Order(9)
    @Test
    @DisplayName("Testing delete on a single token by ID")
    void testDeleteToken() {
        tokenService.deleteToken(token.getId());
        assertThat(tokenService.getTokenById(token.getId())).isEmpty();
        assertThat(tokenService.getTokenByIdentifier(token.getIdentifier())).isEmpty();
    }
}
