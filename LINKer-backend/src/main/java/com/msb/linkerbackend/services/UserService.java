package com.msb.linkerbackend.services;

import com.msb.linkerbackend.dtos.RegisterRequest;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public Optional<User> registerNewUser(@NotNull RegisterRequest registerRequest) throws IllegalArgumentException {
        // Validate and clean the input
        registerRequest.validateAndClean();

        // Check if the username or email already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create a new user
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // TODO: Change this to a proper role system
        newUser.setRole("USER");

        // Save the new user to the database
        userRepository.save(newUser);

        // Return the newly created user
        return Optional.of(newUser);
    }
}
