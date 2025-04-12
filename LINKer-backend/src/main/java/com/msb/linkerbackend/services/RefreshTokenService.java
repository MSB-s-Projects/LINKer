package com.msb.linkerbackend.services;

import com.msb.linkerbackend.dtos.JwtTokens;
import com.msb.linkerbackend.models.RefreshToken;
import com.msb.linkerbackend.repositories.RefreshTokenRepository;
import com.msb.linkerbackend.repositories.UserRepository;
import com.msb.linkerbackend.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    public RefreshToken createRefreshToken(String username) {
        refreshTokenRepository.findByUser_Username(username).ifPresent(this::deleteToken);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username).orElseThrow(() ->
                        new IllegalArgumentException("User not found")))
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plusMillis(refreshTokenExpiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
    }

    public RefreshToken verifyExpiration(@NotNull RefreshToken token) {
        if (token.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new IllegalArgumentException("Refresh token expired");
        }
        return token;
    }

    public void deleteToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    public Cookie getRefreshCookie(@NotNull RefreshToken refreshToken) {
        return getRefreshCookie(refreshToken.getToken());
    }

    public Cookie getRefreshCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(environment.equals("prod")); // Set to true in production
        cookie.setPath("/api/auth/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }

    public JwtTokens getJwtTokens(String refreshToken) {
        RefreshToken token = findByToken(refreshToken);
        token = verifyExpiration(token);
        deleteToken(token);
        RefreshToken newRefreshToken = createRefreshToken(token.getUser().getUsername());
        String accessToken =
                jwtUtils.generateAccessToken(userDetailsService.loadUserByUsername(token.getUser().getUsername()));
        return new JwtTokens(newRefreshToken, accessToken);
    }

}
