package com.studentexpensetracker.controller;

import com.studentexpensetracker.dto.AuthResponse;
import com.studentexpensetracker.dto.AuthenticatedUser;
import com.studentexpensetracker.dto.LoginRequest;
import com.studentexpensetracker.dto.RegisterRequest;
import com.studentexpensetracker.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request, HttpSession session) {
        return authService.register(request, session);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        return authService.login(request, session);
    }

    @GetMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        authService.logout(session);
        return Map.of("message", "Logged out successfully");
    }

    @GetMapping("/me")
    public AuthenticatedUser currentUser(HttpSession session) {
        return authService.getAuthenticatedUser(session);
    }
}
