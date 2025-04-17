package com.msb.linkerbackend.dtos;

import com.msb.linkerbackend.models.UrlMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlMappingDTO {
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private String createdAt;
    private String username;

    public UrlMappingDTO(@NotNull UrlMapping urlMapping, @NotNull String baseUrl) {
        this.originalUrl = urlMapping.getOriginalUrl();
        this.shortUrl = baseUrl + urlMapping.getShortUrl();
        this.clickCount = urlMapping.getClickCount();
        this.createdAt = urlMapping.getCreatedDate().toString();
        this.username = urlMapping.getUser().getUsername();
    }
}
