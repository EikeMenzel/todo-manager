package org.gatewayservice.services;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtServiceTest {

    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String secret;

    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        jwtService.init();

        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        long validDuration = System.currentTimeMillis() + (1000 * 60); // 1 min in the future
        validToken = Jwts.builder()
                .setSubject("TestUser")
                .setExpiration(new Date(validDuration))
                .signWith(key)
                .compact();

        long expiredDuration = System.currentTimeMillis() - (1000 * 60); // 1 min in the past
        expiredToken = Jwts.builder()
                .setSubject("TestUser")
                .setExpiration(new Date(expiredDuration))
                .signWith(key)
                .compact();
    }

    @Test
    void getAllClaimsFromToken_ValidToken_ReturnsClaims() {
        Optional<Claims> claims = jwtService.getAllClaimsFromToken(validToken);
        assertThat(claims).isNotEmpty();
        assertThat(claims.get().getSubject()).isEqualTo("TestUser");
    }

    @Test
    void getAllClaimsFromToken_InvalidToken_ReturnsEmpty() {
        String invalidToken = "invalid.token.here";
        Optional<Claims> claims = jwtService.getAllClaimsFromToken(invalidToken);
        assertThat(claims).isEmpty();
    }

    @Test
    void isTokenExpired_ValidToken_ReturnsFalse() {
        boolean isExpired = jwtService.isTokenExpired(validToken);
        assertThat(isExpired).isFalse();
    }

    @Test
    void isTokenExpired_ExpiredToken_ReturnsTrue() {
        boolean isExpired = jwtService.isTokenExpired(expiredToken);
        assertThat(isExpired).isTrue();
    }
}

