package com.msb.linkerbackend.configurations;

import com.msb.linkerbackend.dtos.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(@NotNull IllegalArgumentException ex) {
        log.error("Illegal argument exception: {}", ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));

        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .error("Bad Request")
                        .message(ex.getMessage())
                        .errorClass(ex.getClass().getSimpleName())
                        .build()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(@NotNull BadCredentialsException ex) {
        log.error("Bad credentials exception: {}", ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .error("Unauthorized")
                        .message(ex.getMessage())
                        .errorClass(ex.getClass().getSimpleName())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(@NotNull Exception ex) {
        log.error("Internal server error: {}", ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));

        return ResponseEntity.internalServerError().body(
                ErrorResponse.builder()
                        .error("Internal Server Error")
                        .message(ex.getMessage())
                        .errorClass(ex.getClass().getSimpleName())
                        .build()
        );
    }
}
