package com.msb.linkerbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlMappingDTO {
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private String createdAt;
    private String username;
}
