package uk.casey.netWorth.utils;

import java.util.UUID;

public interface IJwtService {

  String generateToken(UUID userId, String username);

  boolean validateToken(String token);
}
