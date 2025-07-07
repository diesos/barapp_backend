package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${JWT_ALGORITHM_KEY:SuperSecureSecretKeyForBarApp2025}")
    private String algorithmKey;

    @Value("${JWT_ISSUER:BarApp}")
    private String issuer;

    @Value("${JWT_EXPIRY_IN_SECONDS:604800}")
    private int expiryInSeconds;

    private static final String EMAIL_KEY = "EMAIL";
    private static final String USER_ID_KEY = "USER_ID";
    private static final String ROLE_KEY = "ROLE";

    private Algorithm algorithm;

    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    /**
     * Generate JWT token for user
     */
    public String generateJWT(User user) {
        return JWT.create()
                .withClaim(EMAIL_KEY, user.getEmail())
                .withClaim(USER_ID_KEY, user.getId())
                .withClaim(ROLE_KEY, user.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * expiryInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    /**
     * Get email from token
     */
    public String getEmail(String token) {
        return JWT.decode(token).getClaim(EMAIL_KEY).asString();
    }

    /**
     * Get user ID from token
     */
    public Long getUserId(String token) {
        return JWT.decode(token).getClaim(USER_ID_KEY).asLong();
    }

    /**
     * Get role from token
     */
    public String getRole(String token) {
        return JWT.decode(token).getClaim(ROLE_KEY).asString();
    }

    public boolean validateToken(String token, User user) {
        try {
            String email = getEmail(token);
            Long userId = getUserId(token);
            String role = getRole(token);

            return email.equals(user.getEmail()) &&
                    userId.equals(user.getId()) &&
                    role.equals(user.getRole().name()) &&
                    !JWT.decode(token).getExpiresAt().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
