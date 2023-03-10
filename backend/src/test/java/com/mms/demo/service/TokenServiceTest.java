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

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class TokenServiceTest {
    @Autowired
    TokenService impl;

    static Token token;

    @Order(1)
    @Test
    @DisplayName("Testing create on a single token")
    void testCreateToken() {
        token = Token.builder().identifier("somestring").build();
        assertThat(impl.createToken(token))
            .isNotNull()
            .extracting("isExpired", "isRevoked", "type")
            .containsExactly(false, false, "BEARER");

    }

    @Order(2)
    @Test
    @DisplayName("Testing fetch on a single token by ID")
    void testGetTokenById() {
        assertThat(impl.getTokenById(token.getId())).isNotEmpty().contains(token);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single token by ID")
    void testGetTokenByIdentifier() {
        assertThat(impl.getTokenByIdentifier(token.getIdentifier())).isNotEmpty().contains(token);
    }

    @Order(4)
    @Test
    @DisplayName("Testing expiry on a single token by ID")
    void testExpireToken() {
        Token temp = Token.builder().identifier("test1").build();
        
        assertThat(impl.createToken(temp)).isNotNull().isEqualTo(temp);
        
        assertThat(impl.expireToken(temp.getId()))
            .isPresent()
            .map(val -> val.getIsExpired())
            .hasValue(true);
    }

    @Order(5)
    @Test
    @DisplayName("Testing expiry on a single token by identifier")
    void testExpireToken2() {
        Token temp = Token.builder().identifier("test2").build();
        
        assertThat(impl.createToken(temp)).isNotNull().isEqualTo(temp);
        
        assertThat(impl.expireToken(temp.getIdentifier()))
            .isPresent()
            .map(val -> val.getIsExpired())
            .hasValue(true);
    }

    @Order(6)
    @Test
    @DisplayName("Testing revoke on a single token by ID")
    void testRevokeToken() {
        Token temp = Token.builder().identifier("test3").build();
        
        assertThat(impl.createToken(temp)).isNotNull().isEqualTo(temp);
        
        assertThat(impl.revokeToken(temp.getId()))
            .isPresent()
            .map(val -> val.getIsRevoked())
            .hasValue(true);
    }

    @Order(7)
    @Test
    @DisplayName("Testing revoke on a single token by identifier")
    void testRevokeToken2() {
        Token temp = Token.builder().identifier("test4").build();
        
        assertThat(impl.createToken(temp)).isNotNull().isEqualTo(temp);
        
        assertThat(impl.revokeToken(temp.getIdentifier()))
            .isPresent()
            .map(val -> val.getIsRevoked())
            .hasValue(true);
    }

    @Order(8)
    @Test
    @DisplayName("Testing update on a single token by ID")
    void testUpdateToken() {
        Token temp = token.toBuilder().isExpired(true).isRevoked(true).build();
        
        assertThat(impl.updateToken(token.getId(), temp))
            .isNotNull()
            .isEqualTo(temp)
            .isNotEqualTo(token);
    }

    @Order(9)
    @Test
    @DisplayName("Testing delete on a single token by ID")
    void testDeleteToken() {
        impl.deleteToken(token.getId());
        assertThat(impl.getTokenById(token.getId())).isEmpty();
        assertThat(impl.getTokenByIdentifier(token.getIdentifier())).isEmpty();
    }
}
