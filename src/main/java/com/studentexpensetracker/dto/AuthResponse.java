package com.studentexpensetracker.dto;

public class AuthResponse {

    private String message;
    private AuthenticatedUser user;

    public AuthResponse(String message, AuthenticatedUser user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthenticatedUser getUser() {
        return user;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }
}
