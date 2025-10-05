package uk.casey.netWorth.utils;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtService implements IJwtService {

  private static final String FALLBACK_TEST_SECRET =
      "TEST_PROFILE_STATIC_SECRET_32B_MIN_LEN_123456";

  private final SecretKey secretKey;
  private final long ttlMillis;

  public JwtService(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.ttl-seconds}") long ttlSeconds,
      Environment env) {

    if (secret == null || secret.isBlank()) {
      for (String property : env.getActiveProfiles()) {
        if ("test".equalsIgnoreCase(property) || "local".equalsIgnoreCase(property)) {
          secret = FALLBACK_TEST_SECRET;
          break;
        }
      }
      if (secret == null || secret.isBlank()) {
        throw new IllegalStateException("security.jwt.secret not configured for active profiles");
      }
    }

    byte[] raw;
    try {
      raw = Base64.getDecoder().decode(secret);
    } catch (IllegalArgumentException e) {
      raw = secret.getBytes(StandardCharsets.UTF_8);
    }
    if (raw.length < 32) {
      throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
    }
    this.secretKey = Keys.hmacShaKeyFor(raw);
    this.ttlMillis = ttlSeconds * 1000;
  }

  @Override
  public String generateToken(UUID userId, String username) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setSubject(userId.toString())
        .claim("username", username)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + ttlMillis))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public boolean validateToken(String token) {
    try {
      parser().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private JwtParser parser() {
    return Jwts.parser().verifyWith(secretKey).build();
  }
}
