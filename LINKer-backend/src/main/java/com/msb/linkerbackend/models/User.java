package com.msb.linkerbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    boolean isEmailVerified = false;
    private String role = "USER";
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "user")
    private List<UrlMapping> urlMappings;
}
