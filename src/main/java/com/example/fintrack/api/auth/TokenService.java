package com.example.fintrack.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fintrack.api.common.exception.AuthenticationException;
import com.example.fintrack.api.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {
    private static final String ISSUER = "fintrack-api";

    @Value("${security.token.secret-key}")
    private String secret;

    @Value("${security.token.expiration-time-in-minutes}")
    private Long expirationTimeInMinutes;

    public String generateToken(User user) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .withClaim("userId", user.getId())
                    .withClaim("userName", user.getName())
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new AuthenticationException("Bad credentials");
        }
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public UserTokenDetails getUserTokenDetails(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
            return new UserTokenDetails(
                    decodedJWT.getClaim("userId").asString(),
                    decodedJWT.getClaim("userName").asString(),
                    decodedJWT.getSubject());
        } catch (JWTVerificationException exception) {
            throw new AuthenticationException("Invalid token");
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusMinutes(expirationTimeInMinutes).toInstant(ZoneOffset.UTC);
    }
}