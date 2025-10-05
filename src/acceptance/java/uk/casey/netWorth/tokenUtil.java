package uk.casey.netWorth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

public class tokenUtil {

  private static final String FALLBACK_SECRET = "TEST_PROFILE_STATIC_SECRET_32B_MIN_LEN_123456";
  private static final long TTL_MILLIS = 20 * 60 * 1000;
  private static final SecretKey KEY =
      Keys.hmacShaKeyFor(FALLBACK_SECRET.getBytes(StandardCharsets.UTF_8));

  private tokenUtil() {}

  public static String generateToken(UUID userId, String username) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setSubject(userId.toString())
        .claim("username", username)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + TTL_MILLIS))
        .signWith(KEY, SignatureAlgorithm.HS256)
        .compact();
  }
}
