package com.msb.linkerbackend.services;

import com.msb.linkerbackend.dtos.LoginRequest;
import com.msb.linkerbackend.dtos.RegisterRequest;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.repositories.UserRepository;
import com.msb.linkerbackend.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    @Transactional
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

    public Optional<String> loginUser(@NotNull LoginRequest loginRequest) {
        loginRequest.validateAndClean();

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
                        , loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Optional.ofNullable(jwtUtils.generateAccessToken(userDetails));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void setEmailVerified(@NotNull User user) {
        user.setEmailVerified(true);
        userRepository.save(user);
    }
}
