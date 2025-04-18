package com.msb.linkerbackend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class ErrorResponse {
    private String message;
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorClass;

    public ErrorResponse(String message, @NotNull Exception e) {
        this.message = message;
        this.error = e.getMessage();
        this.errorClass = e.getClass().getName();
    }

    public ErrorResponse(String message) {
        this.message = message;
        this.error = message;
        this.errorClass = null;
    }
}
