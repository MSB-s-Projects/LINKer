package com.msb.linkerbackend.services;

import com.msb.linkerbackend.dtos.AnalyticsDto;
import com.msb.linkerbackend.models.UrlMapping;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.repositories.ClickEventRepository;
import com.msb.linkerbackend.repositories.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;
    private final UserService userService;
    private final UrlMappingService urlMappingService;

    public List<AnalyticsDto> getLinkAnalytics(String shortUrl, LocalDateTime fromDateTime, LocalDateTime toDateTime) throws BadRequestException {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping == null) {
            throw new BadRequestException("Short URL not found");
        }

        return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, fromDateTime, toDateTime).stream()
                .collect(Collectors.groupingBy(clickEvent ->
                        clickEvent.getClickDate().toLocalDate(), Collectors.counting()))
                .entrySet().stream()
                .map(entry ->
                        new AnalyticsDto(entry.getKey().toString(),
                                entry.getValue().intValue())
                ).toList();
    }

    public List<AnalyticsDto> getUserAnalytics(String username, @NotNull LocalDate fromDate,
                                               @NotNull LocalDate toDate) {
        User user = userService.findByUsername(username);
        List<UrlMapping> urlMappings = urlMappingService.getAllUrls(user);
        return clickEventRepository
                .findByUrlMappingInAndClickDateBetween(urlMappings, fromDate.atStartOfDay(),
                        toDate.plusDays(1).atStartOfDay()).stream()
                .collect(Collectors.groupingBy(clickEvent ->
                        clickEvent.getClickDate().toLocalDate(), Collectors.counting()))
                .entrySet().stream()
                .map(entry ->
                        new AnalyticsDto(entry.getKey().toString(),
                                entry.getValue().intValue())
                ).toList();

    }

    public boolean isUserAuthorized(String shortUrl, String username) throws BadRequestException {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping == null) {
            throw new BadRequestException("Short URL not found");
        }
        return urlMapping.getUser().getUsername().equals(username);
    }
}
