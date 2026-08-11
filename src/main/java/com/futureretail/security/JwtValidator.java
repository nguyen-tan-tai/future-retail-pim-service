package com.futureretail.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final Algorithm jwtAlgorithm;

    public Optional<DecodedJWT> validateToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                return Optional.empty();
            }
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            DecodedJWT decodedJWT = JWT.require(jwtAlgorithm).build().verify(cleanToken);
            return Optional.of(decodedJWT);
        } catch (JWTVerificationException e) {
            log.debug("JWT token validation failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> getUsernameFromToken(String token) {
        return validateToken(token).map(decodedJWT -> decodedJWT.getSubject());
    }

    public Optional<String> getClaimFromToken(String token, String claimName) {
        return validateToken(token).map(decodedJWT -> decodedJWT.getClaim(claimName).asString());
    }
}
