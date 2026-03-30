package com.studentexpensetracker.service;

import com.studentexpensetracker.dto.AuthenticatedUser;
import com.studentexpensetracker.dto.RegisterRequest;
import com.studentexpensetracker.exception.DuplicateResourceException;
import com.studentexpensetracker.exception.ResourceNotFoundException;
import com.studentexpensetracker.model.User;
import com.studentexpensetracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public AuthenticatedUser toAuthenticatedUser(User user) {
        return new AuthenticatedUser(user.getId(), user.getName(), user.getEmail());
    }
}
