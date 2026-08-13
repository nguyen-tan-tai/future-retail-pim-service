package com.futureretail.security;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validates JWT tokens using RSA256 algorithm via auth-service JWKS endpoint.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final DefaultJWTProcessor<SecurityContext> jwtProcessor;

    public Optional<JWTClaimsSet> validateToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                return Optional.empty();
            }
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            JWTClaimsSet claimsSet = jwtProcessor.process(cleanToken, null);
            return Optional.of(claimsSet);
        } catch (Exception e) {
            log.debug("JWT token validation failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> getUsernameFromToken(String token) {
        return validateToken(token).map(claims -> {
            try {
                return claims.getSubject();
            } catch (Exception e) {
                return null;
            }
        }).filter(s -> s != null);
    }

    public Optional<String> getClaimFromToken(String token, String claimName) {
        return validateToken(token).map(claims -> {
            try {
                Object claim = claims.getClaim(claimName);
                return claim != null ? claim.toString() : null;
            } catch (Exception e) {
                return null;
            }
        }).filter(s -> s != null);
    }
}
