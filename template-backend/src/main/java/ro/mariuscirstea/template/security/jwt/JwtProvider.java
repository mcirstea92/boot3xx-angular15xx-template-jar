package ro.mariuscirstea.template.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ro.mariuscirstea.template.security.UserPrincipal;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import static java.time.Instant.now;

@Component
@Slf4j
public class JwtProvider {

    @Value("${ro.marius.cirstea.properties.jwtExpiration}")
    private int jwtExpiration;

    private final Key hmacKey;

    public JwtProvider() {
        String safeToken = generateSafeToken();
        log.info("Generated safe token: {}", safeToken);
        this.hmacKey = new SecretKeySpec(safeToken.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
    }

    private String generateSafeToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[36]; // 36 bytes * 8 = 288 bits, a little more than the 256 required bits
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        Instant now = now();
        try {
            String jwt = Jwts.builder()
                    .signWith(hmacKey)
                    .setSubject(username)
                    .setIssuedAt(Date.from(now()))
                    .setExpiration(Date.from(now.plus(jwtExpiration, ChronoUnit.MILLIS)))
                    .compact();
            log.debug("Generated JWT: {}", jwt);
            return jwt;
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new RuntimeException("There was a problem while generating the JWT! " + e.getMessage());
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(authToken);
            return jws.getBody().getExpiration().after(new Date());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: ", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: ", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: ", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: ", e);
        } catch (Exception e) {
            log.error("Exception -> Message: ", e);
        }

        return false;
    }

    public String getUserFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Date getExpirationFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

}
