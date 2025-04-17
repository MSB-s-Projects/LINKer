package com.msb.linkerbackend.controllers;

import com.msb.linkerbackend.dtos.ErrorResponse;
import com.msb.linkerbackend.dtos.UrlMappingDTO;
import com.msb.linkerbackend.models.UrlMapping;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.services.UrlMappingService;
import com.msb.linkerbackend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
@Slf4j
public class UrlMappingController {

    private final UserService userService;
    private final UrlMappingService urlMappingService;

    @Value("${app.base-url}")
    private String baseUrl;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Shorten a URL")
    public ResponseEntity<Object> shortenUrl(@RequestBody Map<String, String> request, Principal principal) {
        try {
            String originalUrl = request.get("originalUrl");
            if (originalUrl == null || originalUrl.isEmpty()) {
                throw new BadRequestException("originalUrl is a required field");
            }
            User user = userService.findByUsername(principal.getName());

            UrlMapping urlMapping = urlMappingService.shortenUrl(originalUrl, user);

            return ResponseEntity.ok(new UrlMappingDTO(urlMapping, baseUrl));
        } catch (BadRequestException | UsernameNotFoundException | IllegalArgumentException e) {
            log.error("Failed to shorten URL: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to shorten URL", e));
        } catch (Exception e) {
            log.error("An error({}) occurred while shortening URL({}): {}", e.getClass().getSimpleName(),
                    request, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while shortening URL", e));
        }
    }

    @GetMapping("/allUrls")
    @PreAuthorize("hasRole('USER')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Get all URLs for a user")
    public ResponseEntity<Object> getAllUrls(Principal principal) {
        try {
            var user = userService.findByUsername(principal.getName());
            var urlMappings = urlMappingService.getAllUrls(user).stream()
                    .map(urlMapping -> new UrlMappingDTO(urlMapping, baseUrl))
                    .toList();
            return ResponseEntity.ok(urlMappings);
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            log.error("Failed to retrieve URLs: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to retrieve URLs", e));
        } catch (Exception e) {
            log.error("An error({}) occurred while retrieving URLs: {}", e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while retrieving URLs", e));
        }
    }
}
