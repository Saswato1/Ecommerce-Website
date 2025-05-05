package com.javatechie.controller;

import com.javatechie.dto.AuthenticationRequest;
import com.javatechie.dto.AuthenticationResponse;
import com.javatechie.dto.RegisterRequest;
import com.javatechie.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponse> googleAuth(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("googleId") String googleId
    ) {
        return ResponseEntity.ok(authenticationService.authenticateWithGoogle(email, name, googleId));
    }
}