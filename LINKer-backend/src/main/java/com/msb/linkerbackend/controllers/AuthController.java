package com.msb.linkerbackend.controllers;

import com.msb.linkerbackend.dtos.RegisterRequest;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private UserService userService;
    private static final String MESSAGE_KEY = "message";

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerNewUser(registerRequest).orElseThrow();
            Map<String, Object> response = new HashMap<>();
            response.put(MESSAGE_KEY, "User Created Successfully");
            response.put("user", user);
            log.info("User created successfully: {}", user);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put(MESSAGE_KEY, "User Creation Failed");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put(MESSAGE_KEY, "An error occurred");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
