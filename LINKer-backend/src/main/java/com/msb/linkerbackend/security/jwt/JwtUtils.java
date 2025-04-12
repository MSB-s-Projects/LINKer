package com.msb.linkerbackend.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Contract(" -> new")
    private @NotNull SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getJwtFromRequest(@NotNull HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        if (jwt != null && jwt.startsWith("Bearer ")) {
            return jwt.substring(7);
        }
        return null;
    }

    public boolean validate(String jwt) {
        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(jwt);
            return true;
        } catch (JwtException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT is empty: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT is invalid: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromJWT(String jwt) {
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(jwt).getPayload().getSubject();
    }

    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().toString();
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(getSignKey())
                .compact();
    }
}
