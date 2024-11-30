package com.gandalftheblack.pm.fileservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gandalftheblack.pm.fileservice.exception.InvalidTokenException;
import com.gandalftheblack.pm.fileservice.exception.UnauthenticatedUserException;
import com.gandalftheblack.pm.fileservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    @Value("${password-manager.security.jwt.secret}")
    private String jwtSecret;

    private static final Pattern TOKEN_PATTERN
            = Pattern.compile("^Bearer *([^ ]+) *$", Pattern.CASE_INSENSITIVE);

    public String getUserIdFromToken(String authorizationHeader) throws UnauthenticatedUserException, InvalidTokenException {
        String authToken = extractToken(authorizationHeader);
        String email = getTokenEmailClaim(authToken);
        return userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new UnauthenticatedUserException(email))
                .getId();
    }

    private String extractToken(String authorizationHeader) throws InvalidTokenException {
        Matcher matcher = TOKEN_PATTERN.matcher(authorizationHeader);
        if (!matcher.matches()) {
            throw new InvalidTokenException();
        }
        return matcher.group(1);
    }

    private String getTokenEmailClaim(String authToken) throws InvalidTokenException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(authToken);
            return decodedJWT.getClaim("email").asString();
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException();
        }
    }
}
