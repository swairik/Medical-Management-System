package com.mms.demo.service;

import java.util.Optional;

import com.mms.demo.entity.Token;

/**
 * The Interface TokenService defines all the interactions needed between a high level controller
 * and the corresponding repository
 * 
 * @author Mananveer Singh
 */
public interface TokenService {
    /**
     * Gets the token by id.
     *
     * @param id the id
     * @return the token by id
     */
    Optional<Token> getTokenById(Long id);

    /**
     * Gets the token by identifier.
     *
     * @param identifier the identifier
     * @return the token by identifier
     */
    Optional<Token> getTokenByIdentifier(String identifier);

    /**
     * Creates the token.
     *
     * @param token the token
     * @return the token
     */
    Token createToken(Token token);

    /**
     * Update token.
     *
     * @param id the id
     * @param tokenUpdates the token updates
     * @return the token
     */
    Token updateToken(Long id, Token tokenUpdates);

    /**
     * Delete token.
     *
     * @param id the id
     */
    void deleteToken(Long id);

    /**
     * Revoke token.
     *
     * @param id the id
     * @return the updated token
     */
    Optional<Token> revokeToken(Long id);

    /**
     * Revoke token.
     *
     * @param identifier the identifier
     * @return the updated token
     */
    Optional<Token> revokeToken(String identifier);

    /**
     * Declares the scheduled job responsible for cleaning up expired tokens periodically.
     */
    void tokenCleanerScheduler();
}
