package com.msb.linkerbackend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {

            // Get JWT From Header
            String accessToken = jwtUtils.getJwtFromRequest(request);

            // Validate Token
            if (accessToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // get username -> load User to UserDetails
                String username = jwtUtils.getUsernameFromJWT(accessToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // set auth context
                if (userDetails != null && jwtUtils.validate(accessToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception e) {
            log.error("Error while authenticating user: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                    {
                        "error": "Unauthorized",
                        "errorClass": "%s",
                        "message": "%s"
                    }
                    """.formatted(e.getClass().getSimpleName(), e.getMessage()));
            response.getWriter().flush();
        }
        filterChain.doFilter(request, response);
    }
}
