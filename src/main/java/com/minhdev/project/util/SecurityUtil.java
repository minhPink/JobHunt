package com.minhdev.project.util;

import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    };

    public  static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    @Value("${minh.jwt.base64-secret}")
    private String jwtKey;

    @Value("${minh.jwt.token-validity-in-seconds}")
    private long jwtExpiryInSeconds;

    public String createToken(Authentication authentication) {

        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtExpiryInSeconds, ChronoUnit.SECONDS);
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject (authentication.getName())
                .claim("minhdev", authentication)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }
}
