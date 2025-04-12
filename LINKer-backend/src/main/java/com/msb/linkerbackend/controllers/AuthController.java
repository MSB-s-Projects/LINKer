package com.msb.linkerbackend.controllers;

import com.msb.linkerbackend.dtos.ErrorResponse;
import com.msb.linkerbackend.dtos.JwtTokens;
import com.msb.linkerbackend.dtos.LoginRequest;
import com.msb.linkerbackend.dtos.RegisterRequest;
import com.msb.linkerbackend.models.RefreshToken;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.services.RefreshTokenService;
import com.msb.linkerbackend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public static final String MESSAGE = "message";

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerNewUser(registerRequest).orElseThrow(IllegalArgumentException::new);
            log.info("User created successfully: {}", user);

            Map<String, Object> response = Map.of(
                    MESSAGE, "User Created Successfully",
                    "user", user
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("User Creation Failed", e));
        } catch (Exception e) {
            log.error("An error({}) occurred while registering user({}): {}", e.getClass().getSimpleName(),
                    registerRequest, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while registering user", e));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String accessToken = userService.loginUser(loginRequest).orElseThrow(IllegalArgumentException::new);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());

            // Set refresh toke in HTTP only cookie
            response.addCookie(refreshTokenService.getRefreshCookie(refreshToken));

            Map<String, Object> res = Map.of(
                    MESSAGE, "Login Successful",
                    "token", accessToken
            );
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Login Failed", e));
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials: {}", e.getMessage());
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials", e));
        } catch (Exception e) {
            log.error("An error({}) occurred while logging in user({}): {}", e.getClass().getSimpleName(),
                    loginRequest, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while logging in user", e));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(@CookieValue("refreshToken") String refreshToken,
                                               HttpServletResponse response) {
        try {
            JwtTokens jwtTokens = refreshTokenService.getJwtTokens(refreshToken);

            // Reset the cookie with the new refresh token
            response.addCookie(refreshTokenService.getRefreshCookie(jwtTokens.refreshToken()));

            Map<String, Object> res = Map.of(
                    MESSAGE, "Token refreshed successfully",
                    "token", jwtTokens.accessToken()
            );
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Token Refresh Failed", e));
        } catch (Exception e) {
            log.error("An error({}) occurred while refreshing token: {}", e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while refreshing token", e));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@CookieValue("refreshToken") String refreshToken,
                                         HttpServletResponse response) {
        try {
            refreshTokenService.deleteToken(refreshTokenService.findByToken(refreshToken));

            // Delete the refresh token cookie
            Cookie cookie = new Cookie("refreshToken", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/auth/refresh");
            cookie.setMaxAge(0); // delete cookie
            response.addCookie(cookie);

            Map<String, Object> res = Map.of(
                    MESSAGE, "Logout Successful"
            );
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            log.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Logout Failed", e));
        } catch (Exception e) {
            log.error("An error({}) occurred while logging out: {}", e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while logging out", e));
        }
    }
}
