package com.msb.linkerbackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = Map.of(
                "status", "UP",
                "message", "Service is running",
                "version", "1.0.0",
                "timestamp", new Date().toString(),
                "service", "linker-backend",
                "author", "MSB");
        return ResponseEntity.ok(response);
    }
}
