package ro.mariuscirstea.eventtracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${ro.marius.cirstea.properties.jwtSecret}")
    private String jwtSecret;

    @Value("${ro.marius.cirstea.properties.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        try {
            String jwt = Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000L))
                    .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
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
            Jws<Claims> jws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
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

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

}
