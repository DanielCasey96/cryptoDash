package uk.casey.cryptodash.utils;

import java.util.Base64;

public class JwtUtil {

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
