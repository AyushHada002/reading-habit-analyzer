package com.ayush.readinghabit.service;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET =
            "this-is-a-very-long-secret-key-for-jwt-testing-only-123456789";

    @BeforeEach
    void setUp() {

        jwtService = new JwtService(
                SECRET,
                3600000
        );
    }

    @Test
    void generateToken_shouldCreateValidToken() {

        String token =
                jwtService.generateToken(
                        1L,
                        "ayush@example.com"
                );

        assertNotNull(token);
        assertFalse(token.isBlank());

        assertTrue(
                jwtService.isTokenValid(token)
        );
    }

    @Test
    void extractUserId_shouldReturnCorrectUserId() {

        String token =
                jwtService.generateToken(
                        25L,
                        "ayush@example.com"
                );

        Long userId =
                jwtService.extractUserId(token);

        assertEquals(
                25L,
                userId
        );
    }

    @Test
    void extractEmail_shouldReturnCorrectEmail() {

        String token =
                jwtService.generateToken(
                        1L,
                        "ayush@example.com"
                );

        String email =
                jwtService.extractEmail(token);

        assertEquals(
                "ayush@example.com",
                email
        );
    }

    @Test
    void isTokenValid_shouldReturnFalseForInvalidToken() {

        String invalidToken =
                "invalid.jwt.token";

        assertFalse(
                jwtService.isTokenValid(
                        invalidToken
                )
        );
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken()
            throws InterruptedException {

        JwtService shortLivedJwtService =
                new JwtService(
                        SECRET,
                        1
                );

        String token =
                shortLivedJwtService.generateToken(
                        1L,
                        "ayush@example.com"
                );

        Thread.sleep(10);

        assertFalse(
                shortLivedJwtService.isTokenValid(
                        token
                )
        );
    }
}
