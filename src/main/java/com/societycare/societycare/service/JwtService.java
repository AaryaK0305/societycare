package com.societycare.societycare.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public static final long DEFAULT_EXPIRATION_MS = 30 * 60 * 1000L; // 30 minutes

    private final SecretKey signingKey;
    private final JwtParser jwtParser;
    private final long expirationMs;

    @Autowired
    public JwtService(
            @Value("${JWT_SECRET:${jwt.secret:#{null}}}") String secret,
            @Value("${jwt.expiration-ms:1800000}") Long expirationMs) {

        if (secret == null || secret.isBlank()) {
            secret = System.getenv("JWT_SECRET");
        }

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT signing secret is missing. The JWT_SECRET environment variable must be set.");
        }

        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
            if (keyBytes.length < 32) {
                keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT signing secret is too weak. The secret must be at least 256 bits (32 bytes) for HMAC-SHA256.");
        }

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parser().verifyWith(this.signingKey).build();
        this.expirationMs = (expirationMs != null && expirationMs > 0) ? expirationMs : DEFAULT_EXPIRATION_MS;
    }

    public JwtService(String secret) {
        this(secret, DEFAULT_EXPIRATION_MS);
    }

    public String generateToken(String email, String role) {
        return generateToken(Collections.emptyMap(), email, role);
    }

    public String generateToken(Map<String, Object> extraClaims, String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.expirationMs);

        var builder = Jwts.builder();

        if (extraClaims != null && !extraClaims.isEmpty()) {
            builder.claims(extraClaims);
        }

        return builder
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(this.signingKey)
                .compact();
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return extractSubject(token);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return this.jwtParser.parseSignedClaims(token).getPayload();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration != null && expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            this.jwtParser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateToken(String token, String expectedSubject) {
        try {
            String subject = extractSubject(token);
            return subject != null && subject.equalsIgnoreCase(expectedSubject) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenValid(String token) {
        return validateToken(token);
    }

    public boolean isTokenValid(String token, String expectedSubject) {
        return validateToken(token, expectedSubject);
    }

    public long getExpirationMs() {
        return this.expirationMs;
    }
}
