package com.mms.demo.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtService {
    public String extractUsername(String token);

    public String generateToken(UserDetails userDetails);

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    public Date extractExpiration(String token);


    public boolean isTokenValid(String token, UserDetails userDetails);

}
