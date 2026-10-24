package com.societycare.societycare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.societycare.societycare.dto.LoginRequest;
import com.societycare.societycare.dto.LoginResponse;
import com.societycare.societycare.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

@ExceptionHandler(AuthenticationException.class)
public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Invalid email or password");
}
}
