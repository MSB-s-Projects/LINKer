package com.msb.linkerbackend.services;

import com.msb.linkerbackend.models.EmailOtp;
import com.msb.linkerbackend.models.User;
import com.msb.linkerbackend.repositories.EmailOtpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class EmailOtpService {

    private final SecureRandom random = new SecureRandom();
    private final EmailOtpRepository emailOtpRepository;
    private final JavaMailSender mailSender;
    private final UserService userService;

    @Transactional
    public void generateAndSendOtp(String username) throws UsernameNotFoundException {
        // 1. Generate a random 6-digit code
        int code = random.nextInt(1000000);

        // 2. Save the OTP to the database
        User user = userService.findByUsername(username);
        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setUsername(user.getUsername());
        emailOtp.setCode(String.format("%06d", code));
        emailOtp.setExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));
        emailOtp.setUsed(false);
        emailOtpRepository.save(emailOtp);

        // 3. Send the OTP to the user's email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("LINKer: Your OTP Code");
        message.setText("Your OTP code is: " + emailOtp.getCode());
        mailSender.send(message);
    }

    @Transactional
    public boolean verifyOtp(String username, String code) throws BadCredentialsException {
        EmailOtp emailOtp = emailOtpRepository.findTopByUsernameAndCodeAndUsedFalseOrderByExpiresAtDesc(username,
                code).orElseThrow(() -> new BadCredentialsException("Invalid OTP"));
        if (emailOtp.getExpiresAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("OTP expired");
        } else {
            User user = userService.findByUsername(username);
            userService.setEmailVerified(user);
            emailOtp.setUsed(true);
            emailOtpRepository.save(emailOtp);
            return true;
        }
    }
}
