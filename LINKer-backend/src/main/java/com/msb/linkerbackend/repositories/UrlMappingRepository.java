package com.msb.linkerbackend.repositories;

import com.msb.linkerbackend.models.UrlMapping;
import com.msb.linkerbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    List<UrlMapping> findByUser(User user);

    UrlMapping findByShortUrl(String shortUrl);
}
