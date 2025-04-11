package com.msb.linkerbackend.controllers;

import com.msb.linkerbackend.dtos.ErrorResponse;
import com.msb.linkerbackend.dtos.LoginRequest;
import com.msb.linkerbackend.dtos.RegisterRequest;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerNewUser(registerRequest).orElseThrow(IllegalArgumentException::new);
            log.info("User created successfully: {}", user);

            Map<String, Object> response = Map.of(
                    "message", "User Created Successfully",
                    "user", user
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User creation failed: {}", e.getMessage());

            ErrorResponse response = ErrorResponse.builder()
                    .message("User Creation Failed")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("An error({}) occurred while registering user({}): {}", e.getClass().getSimpleName(),
                    registerRequest, e.getMessage());

            ErrorResponse response = ErrorResponse.builder()
                    .message("An error occurred")
                    .message("An error occurred :" + e.getClass().getSimpleName())
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
            String jwt = userService.loginUser(loginRequest).orElseThrow(IllegalArgumentException::new);

            Map<String, Object> response = Map.of(
                    "message", "Login Successful",
                    "token", jwt
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Login failed: {}", e.getMessage());

            ErrorResponse response = ErrorResponse.builder()
                    .message("Login Failed")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials: {}", e.getMessage());

            ErrorResponse response = ErrorResponse.builder()
                    .message("Invalid Credentials")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(401).body(response);
        } catch (Exception e) {
            log.error("An error({}) occurred while logging in user({}): {}", e.getClass().getSimpleName(),
                    loginRequest, e.getMessage());

            ErrorResponse response = ErrorResponse.builder()
                    .message("An error occurred :" + e.getClass().getSimpleName())
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
