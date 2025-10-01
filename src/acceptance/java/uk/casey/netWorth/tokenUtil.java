package uk.casey.netWorth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

public class tokenUtil {

  private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public static String generateToken(UUID userId, String username) {
    return Jwts.builder()
        .setSubject(userId.toString())
        .claim("username", username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1200_00))
        .signWith(key)
        .compact();
  }
}
