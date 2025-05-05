package com.javatechie.security;

import com.javatechie.entity.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Simplified token provider that replaces JWT functionality
 * This class is kept to avoid compilation errors but uses a simple UUID-based approach
 */
@Component
public class JwtTokenProvider {

    public String generateToken(User user) {
        return "token_" + UUID.randomUUID().toString() + "_" + user.getEmail();
    }

    public boolean validateToken(String token, User user) {
        // Simple validation - check if token contains user's email
        return token != null && token.contains(user.getEmail());
    }

    public String extractUsername(String token) {
        // Simple extraction - assume token format is "token_UUID_email"
        if (token != null && token.startsWith("token_")) {
            String[] parts = token.split("_");
            if (parts.length >= 3) {
                return parts[2];
            }
        }
        return null;
    }
}
