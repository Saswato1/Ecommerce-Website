package com.javatechie.service;

import com.javatechie.dto.AuthenticationRequest;
import com.javatechie.dto.AuthenticationResponse;
import com.javatechie.dto.RegisterRequest;
import com.javatechie.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    // Simple token generator to replace JWT
    private String generateToken(User user) {
        return "token_" + UUID.randomUUID().toString();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhoneNumber()
        );

        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Simple authentication - in a real app, verify credentials properly
        User user = userService.findByEmail(request.getEmail());

        // In a real app, check password here

        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthenticationResponse authenticateWithGoogle(String email, String name, String googleId) {
        User user = userService.processOAuthPostLogin(email, name, googleId);
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
