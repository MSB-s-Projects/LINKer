package com.msb.linkerbackend.repositories;

import com.msb.linkerbackend.models.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
    Optional<EmailOtp> findTopByUsernameAndCodeAndUsedFalseOrderByExpiresAtDesc(String username, String code);
}
