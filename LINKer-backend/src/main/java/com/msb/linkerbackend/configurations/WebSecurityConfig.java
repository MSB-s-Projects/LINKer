package com.msb.linkerbackend.configurations;

import com.msb.linkerbackend.security.ApiPathExclusions;
import com.msb.linkerbackend.security.jwt.JwtAuthenticationFilter;
import com.msb.linkerbackend.security.oauth.CustomOAuth2SuccessHandler;
import com.msb.linkerbackend.security.oauth.GithubOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final GithubOAuth2UserService githubOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // disable CSRF protection
        // set url patterns for authentication
        http
                // TODO: Enable CSRF protection
                // TODO: Enable CORS
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Arrays.stream(ApiPathExclusions.values())
                                .map(ApiPathExclusions::getPath)
                                .toArray(String[]::new)).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(
                                        (request, response, authException) ->
                                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(githubOAuth2UserService)
                        )
                        .loginPage("/oauth2/authorization/github")
                        .successHandler(customOAuth2SuccessHandler))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        // set DAO authentication provider
        // set password encoder
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
