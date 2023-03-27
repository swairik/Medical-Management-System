package com.mms.demo.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

/**
 * The Interface JwtService defines all the interactions needed between a high level controller and
 * the Token repository
 * 
 * @author Swairik Dey
 */
public interface JwtService {
    /**
     * Gets a username given a proper token
     * 
     * @param token the token
     * @return the username
     */
    public String extractUsername(String token);

    /**
     * Generates a new token, given correct user details.
     * 
     * @param userDetails encapsulates the details of a user
     * @return an authentication token
     */
    public String generateToken(UserDetails userDetails);

    /**
     * Generates a new token, given correct user details and claims.
     * 
     * @param extraClaims encapsulates any claims to be considered
     * @param userDetails encapsulates the details of a user
     * @return an authentication token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Extracts the claims granted to a token
     * 
     * @param <T> the entity class
     * @param token the authentication token
     * @param claimsResolver the corresponding claims resolver for the entity class
     * @return the requested claims
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Extracts the expiration timestamp for a given token
     * 
     * @param token the token
     * @return the expiration date
     */
    public Date extractExpiration(String token);

    /**
     * Checks if a token is valid or not
     * 
     * @param token the token
     * @param userDetails the details of the user that has been assigned to the token
     * @return true if valid else false
     */
    public boolean isTokenValid(String token, UserDetails userDetails);

}
