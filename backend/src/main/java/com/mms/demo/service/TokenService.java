package com.mms.demo.service;
import java.util.Optional;

import com.mms.demo.entity.Token;

public interface TokenService {
    Optional<Token> getTokenById(Long id);
    Optional<Token> getTokenByIdentifier(String identifier);

    Token createToken(Token token);
    Token updateToken(Long id, Token tokenUpdates);
    void deleteToken(Long id);

    Optional<Token> revokeToken(Long id);
    Optional<Token> revokeToken(String identifier);
}
