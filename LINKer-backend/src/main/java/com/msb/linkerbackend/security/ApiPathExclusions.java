package com.msb.linkerbackend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiPathExclusions {
    HEALTH("/api/health/**"),
    DOCS("/api/docs/**"),
    AUTH("/api/auth/**"),
    OAUTH("/api/oauth/**"),
    LOGIN("/login/**"),
    SHORT_URL("/{shortUrl}");

    private final String path;
}
