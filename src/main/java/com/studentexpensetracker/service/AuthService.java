package com.studentexpensetracker.service;

import com.studentexpensetracker.dto.AuthResponse;
import com.studentexpensetracker.dto.AuthenticatedUser;
import com.studentexpensetracker.dto.LoginRequest;
import com.studentexpensetracker.dto.RegisterRequest;
import com.studentexpensetracker.exception.UnauthorizedException;
import com.studentexpensetracker.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public static final String SESSION_USER = "loggedInUser";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request, HttpSession session) {
        User user = userService.registerUser(request);
        AuthenticatedUser authenticatedUser = userService.toAuthenticatedUser(user);
        session.setAttribute(SESSION_USER, authenticatedUser);
        return new AuthResponse("Registration successful", authenticatedUser);
    }

    public AuthResponse login(LoginRequest request, HttpSession session) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        AuthenticatedUser authenticatedUser = userService.toAuthenticatedUser(user);
        session.setAttribute(SESSION_USER, authenticatedUser);
        return new AuthResponse("Login successful", authenticatedUser);
    }

    public AuthenticatedUser getAuthenticatedUser(HttpSession session) {
        Object sessionUser = session.getAttribute(SESSION_USER);
        if (sessionUser instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }
        throw new UnauthorizedException("Please log in to continue");
    }

    public User requireLoggedInUser(HttpSession session) {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser(session);
        return userService.getUserById(authenticatedUser.getId());
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
