package com.msb.linkerbackend.services;

import com.msb.linkerbackend.models.ClickEvent;
import com.msb.linkerbackend.models.UrlMapping;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.repositories.ClickEventRepository;
import com.msb.linkerbackend.repositories.UrlMappingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final Random random = new Random();
    private final ClickEventRepository clickEventRepository;

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

    public List<UrlMapping> getAllUrls(User user) {
        return urlMappingRepository.findByUser(user);
    }

    @Transactional
    public String getOriginalUrl(String shortUrl) {
        var urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            // Increment the click count
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);

            // Create click Event
            var clickEvent = new ClickEvent();
            clickEvent.setUrlMapping(urlMapping);
            clickEvent.setClickDate(LocalDateTime.now());
            clickEventRepository.save(clickEvent);

            return urlMapping.getOriginalUrl();
        }
        return null;
    }
}
