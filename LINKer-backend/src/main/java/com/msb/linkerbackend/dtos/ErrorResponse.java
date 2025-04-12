package com.msb.linkerbackend.dtos;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class ErrorResponse {
    private String message;
    private String error;
    private String errorClass;

    public ErrorResponse(String message, @NotNull Exception e) {
        this.message = message;
        this.error = e.getMessage();
        this.errorClass = e.getClass().getName();
    }
}
