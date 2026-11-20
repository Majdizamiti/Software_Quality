package com.Shadows.SpringZ.security;

import com.Shadows.SpringZ.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "TestSecretKeyAtLeast64BytesLong_0123456789_0123456789_0123456789");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    }

    @Test
    void generateTokenFromSubject_ReturnsValidToken() {
        String token = jwtUtils.generateTokenFromSubject("test@example.com");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("test@example.com", jwtUtils.getSubject(token));
    }

    @Test
    void validateJwtToken_InvalidToken_ReturnsFalse() {
        boolean isValid = jwtUtils.validateJwtToken("invalid.token.string");

        assertFalse(isValid);
    }
}
