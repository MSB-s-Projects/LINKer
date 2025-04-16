package com.msb.linkerbackend.services;

import com.msb.linkerbackend.models.UrlMapping;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.repositories.UrlMappingRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final Random random = new Random();

    public UrlMappingService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public UrlMapping shortenUrl(String originalUrl, User user) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(generateShortUrl());
        urlMapping.setUser(user);
        return urlMappingRepository.save(urlMapping);
    }

    private @NotNull String generateShortUrl() {
        StringBuilder shortUrl = new StringBuilder(8);
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 8; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }
}
