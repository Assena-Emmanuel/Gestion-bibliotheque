package com.biblio.app.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "blacklist:";

    public void blacklistToken(
            String token,
            Instant expiration
    ) {

        long seconds = Duration.between(
                Instant.now(),
                expiration
        ).getSeconds();

        if (seconds <= 0) {
            return;
        }

        String key = PREFIX + hashToken(token);

        redisTemplate.opsForValue().set(
                key,
                "blacklisted",
                Duration.ofSeconds(seconds)
        );
    }

    public boolean isBlacklisted(String token) {

        String key = PREFIX + hashToken(token);

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }

    private String hashToken(String token) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            token.getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder hexString =
                    new StringBuilder();

            for (byte b : hash) {
                hexString.append(
                        String.format("%02x", b)
                );
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e
            );
        }
    }
}