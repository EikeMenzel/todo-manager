package org.authenticationservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtService.class})
@TestPropertySource(properties = {
        "jwt.secret=oP,8=ut]mWat[ifl}Fx/u8[.1g\"m4!(2S}jlbLb;.pJOV7bM3)$-S_+m!}+Iln",
        "jwt.expiration=3600000"
})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void testGenerateToken() {
        Long userId = 1L;

        String token = jwtService.generateToken(userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("oP,8=ut]mWat[ifl}Fx/u8[.1g\"m4!(2S}jlbLb;.pJOV7bM3)$-S_+m!}+Iln".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(String.valueOf(userId), claims.getSubject());

        Date now = new Date();
        assertTrue(claims.getIssuedAt().before(now));
        assertTrue(claims.getExpiration().after(now));
    }
}
