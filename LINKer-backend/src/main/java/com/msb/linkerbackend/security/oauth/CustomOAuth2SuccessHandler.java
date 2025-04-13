package com.msb.linkerbackend.security.oauth;

import com.msb.linkerbackend.models.RefreshToken;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.repositories.UserRepository;
import com.msb.linkerbackend.security.jwt.JwtUtils;
import com.msb.linkerbackend.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, @NotNull HttpServletResponse response,
                                        @NotNull Authentication authentication) throws IOException,
            BadCredentialsException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String githubUsername = oAuth2User.getAttribute("login");
        String email = oAuth2User.getAttribute("email");


        User user = userRepository.findByEmail(email).orElseGet(() -> registerNewOauthUser(githubUsername, email));

        // Create JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        response.addCookie(refreshTokenService.getRefreshCookie(refreshToken));
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("""
                {
                    "accessToken": "%s",
                    "user": {
                        "id": %d,
                        "username": "%s",
                        "email": "%s",
                        "role": "%s"
                    }
                }
                """.formatted(accessToken, user.getId(), user.getUsername(), user.getEmail(), user.getRole()));

    }

    private @NotNull User registerNewOauthUser(String githubUsername, String email) {
        User newUser = new User();
        newUser.setUsername(githubUsername);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(githubUsername));
        newUser.setRole("USER");
        newUser.setEmailVerified(true);
        return userRepository.save(newUser);
    }
}
