package com.societycare.societycare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String email;
    private String role;
    private String message;
    private String token;

    public LoginResponse(String email, String role) {
        this.email = email;
        this.role = role;
        this.message = "Login successful";
    }

    public LoginResponse(String email, String role, String message) {
        this.email = email;
        this.role = role;
        this.message = message;
    }
}
