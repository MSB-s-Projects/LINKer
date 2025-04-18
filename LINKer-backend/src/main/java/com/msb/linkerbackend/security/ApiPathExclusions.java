package com.msb.linkerbackend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiPathExclusions {
    HEALTH("/api/health/"),
    DOCS("/api/docs/**"),
    AUTH("/api/auth/**"),
    LOGIN("/login/**"),
    SHORT_URL("/{shortUrl}");

    private final String path;
}
