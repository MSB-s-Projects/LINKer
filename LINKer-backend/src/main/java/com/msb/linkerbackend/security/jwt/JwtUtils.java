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
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

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
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(jwt);
            return true;
        } catch (JwtException e) {
            log.error(e.getMessage());
            // Token is invalid
            return false;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            // Token is empty or null
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            // Other exceptions
            return false;
        }
    }

    public String getUsernameFromJWT(String jwt) {
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(jwt).getPayload().getSubject();
    }

}
