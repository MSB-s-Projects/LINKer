package com.msb.linkerbackend.repositories;

import com.msb.linkerbackend.models.ClickEvent;
import com.msb.linkerbackend.models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping urlMapping, LocalDateTime fromDateTime,
                                                         LocalDateTime toDateTime);

    List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMappings, LocalDateTime fromDate,
                                                           LocalDateTime toDate);
}
