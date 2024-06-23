package com.bci.exercise.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@Log4j2
public class JWTUtil {

    @Value("${auth.jwt.issuer}")
    private String issuer;

    @Value("${auth.jwt.secret}")
    private String secret;

    @Value("${auth.jwt.audience}")
    private String audience;

    @Value("${auth.jwt.ttl-in-seconds}")
    private long timeToLiveInSeconds;

    private SecretKey secretKey;

    @PostConstruct
    public void setUpSecretKey() {
        try {
            secretKey = Keys.hmacShaKeyFor(secret.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("Error generating JWT Secret Key : {}", e.getMessage());
            throw new RuntimeException("Error generating JWT Secret Key", e);
        }
    }


    private static final String CLAIM_NAME = "Name";
    private static final String CLAIM_EMAIL = "Email";


    public String createJWT(String username, String name, String email) {
        String jwt =
                Jwts.builder()
                        .setId(UUID.randomUUID().toString())
                        .setSubject(username)
                        .setIssuer(issuer)
                        .setAudience(audience)
                        .setIssuedAt(Date.from(Instant.now()))
                        .setExpiration(Date.from(Instant.now().plus(Duration.ofSeconds(timeToLiveInSeconds))))
                        .claim(CLAIM_NAME, name)
                        .claim(CLAIM_EMAIL, email)
                        .signWith(secretKey)
                        .compact();
        return jwt;
    }



    public Claims parseJWT(String jwtString) {

        Jws<Claims> headerClaimsJwt =
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(jwtString);

        Claims claims = headerClaimsJwt.getBody();

        return claims;
    }
}
