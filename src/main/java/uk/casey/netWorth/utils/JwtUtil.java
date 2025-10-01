package uk.casey.netWorth.utils;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

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

  public static boolean validateToken(String token) {
    try {
      JwtParser parser = Jwts.parser().setSigningKey(key).build();
      parser.parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isValidJwtFormat(String token) {
    if (token == null || token.trim().isEmpty()) {
      return false;
    }

    try {
      // Basic JWT format check - should have 3 parts separated by dots
      String[] parts = token.split("\\.");
      if (parts.length != 3) {
        return false;
      }

      // Check if parts are valid Base64URL
      Base64.Decoder decoder = Base64.getUrlDecoder();
      decoder.decode(parts[0]); // Header
      decoder.decode(parts[1]); // Payload
      // Don't decode signature (part[2]) as it might not be valid

      // Optional: Check if header contains expected fields
      String header = new String(decoder.decode(parts[0]));
      if (!header.contains("\"alg\"") || !header.contains("HS256")) {
        return false;
      }

      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
