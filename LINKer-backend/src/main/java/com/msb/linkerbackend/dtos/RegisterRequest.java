package com.msb.linkerbackend.dtos;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Set<String> roles;

    public void validateAndClean() throws IllegalArgumentException {
        this.username = StringUtils.trimToNull(username);
        if (username == null || username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be between 3 and 20 characters");
        }

        this.email = StringUtils.trimToNull(email);
        if (email == null || !email.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        this.password = StringUtils.trimToNull(password);
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be empty");
        }
        for (String role : roles) {
            role = StringUtils.trimToNull(role);
            // TODO: Check if the role is in enum
            if (role == null || role.isEmpty()) {
                throw new IllegalArgumentException("Role cannot be empty");
            }
        }
    }
}
