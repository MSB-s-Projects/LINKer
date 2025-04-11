package com.msb.linkerbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class LoginRequest {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;

    public void validateAndClean() throws IllegalArgumentException {
        this.username = StringUtils.trimToNull(username);
        if (username == null) {
            throw new IllegalArgumentException("Username must not be empty");
        }

        this.password = StringUtils.trimToNull(password);
        if (password == null) {
            throw new IllegalArgumentException("Password must not be empty");
        }
    }
}
