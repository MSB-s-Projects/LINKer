package com.msb.linkerbackend.controllers;

import com.msb.linkerbackend.dtos.ErrorResponse;
import com.msb.linkerbackend.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            security = @SecurityRequirement(name = "bearerAuth"),
            summary = "Get link analytics",
            description = "Fetches the analytics for a given short URL within the specified date range.")
    public ResponseEntity<Object> getLinkAnalytics(@PathVariable String shortUrl, @RequestParam String from,
                                                   @RequestParam String to, Principal principal) {
        try {
            if (shortUrl == null || shortUrl.isEmpty()) {
                throw new BadRequestException("ShortUrl is empty");
            }
            if (from == null || from.isEmpty()) {
                throw new BadRequestException("From date is empty");
            }
            if (to == null || to.isEmpty()) {
                throw new BadRequestException("To date is empty");
            }
            if (!analyticsService.isUserAuthorized(shortUrl, principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("You are not authorized to access this resource"));
            }

            var formatter = DateTimeFormatter.ISO_DATE_TIME;
            var fromDateTime = LocalDateTime.parse(from, formatter);
            var toDateTime = LocalDateTime.parse(to, formatter);

            var analytics = analyticsService.getLinkAnalytics(shortUrl, fromDateTime, toDateTime);
            return ResponseEntity.ok(analytics);
        } catch (IllegalArgumentException | DateTimeParseException | BadRequestException e) {
            log.error("Failed to fetch analytics: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to fetch analytics", e));
        } catch (Exception e) {
            log.error("Error({}) fetching analytics for short URL({}): {}", e.getClass().getSimpleName(), shortUrl,
                    e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while fetching analytics", e));
        }
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            security = @SecurityRequirement(name = "bearerAuth"),
            summary = "Get total clicks",
            description = "Fetches the total number of clicks for all short URLs within the specified date range.")
    public ResponseEntity<Object> getAllUserAnalytics(@RequestParam String from, @RequestParam String to,
                                                      Principal principal) {
        try {
            if (from == null || from.isEmpty()) {
                throw new BadRequestException("From date is empty");
            }
            if (to == null || to.isEmpty()) {
                throw new BadRequestException("To date is empty");
            }

            var formatter = DateTimeFormatter.ISO_DATE;
            var fromDate = LocalDate.parse(from, formatter);
            var toDate = LocalDate.parse(to, formatter);

            var analytics = analyticsService.getUserAnalytics(principal.getName(), fromDate, toDate);
            return ResponseEntity.ok(analytics);

        } catch (IllegalArgumentException | DateTimeParseException | BadRequestException e) {
            log.error("Failed to fetch total click analytics: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to fetch analytics", e));
        } catch (Exception e) {
            log.error("Error({}) fetching analytics for user({}): {}", e.getClass().getSimpleName(),
                    principal.getName(),
                    e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while fetching analytics", e));
        }
    }
}
