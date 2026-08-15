package com.futureretail.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration using RSA256 public key from auth-service JWKS endpoint.
 * No longer uses shared secret - validates JWT signatures using public key.
 */
@Configuration
public class JwtConfig {

    @Value("${app.security.auth-server-jwks-url:http://localhost:18081/oauth2/jwks}")
    private String jwksUrl;

    @Bean
    public DefaultJWTProcessor<com.nimbusds.jose.proc.SecurityContext> jwtProcessor() throws Exception {
        JWKSource<com.nimbusds.jose.proc.SecurityContext> keySource =
                new RemoteJWKSet<>(new URL(jwksUrl), new DefaultResourceRetriever(5000, 5000));

        DefaultJWTProcessor<com.nimbusds.jose.proc.SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<com.nimbusds.jose.proc.SecurityContext> keySelector =
                new JWSVerificationKeySelector<>(com.nimbusds.jose.JWSAlgorithm.RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);
        return jwtProcessor;
    }
}
