package com.msb.linkerbackend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "email_otps")
@Data
public class EmailOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String code;
    private Instant expiresAt;
    private boolean used = false;
}
