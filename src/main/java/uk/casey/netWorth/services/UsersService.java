package uk.casey.netWorth.services;

import java.sql.SQLException;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import uk.casey.netWorth.exceptions.AuthenticationException;
import uk.casey.netWorth.services.providers.DataBaseProvider;
import uk.casey.netWorth.utils.JwtUtil;

@Component
public class UsersService {

  private final DataBaseProvider dataBaseProvider;

  public UsersService(DataBaseProvider dataBaseProvider) {
    this.dataBaseProvider = dataBaseProvider;
  }

  public String registerUser(String username, String password, String email) {
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    return dataBaseProvider.registerUser(username, hashedPassword, email);
  }

  public String authoriseUser(UUID userId, String username, String password) throws SQLException {
    String dbPassword = dataBaseProvider.checkPassword(userId, username);
    if (!BCrypt.checkpw(password, dbPassword))
      throw new AuthenticationException("Invalid Credentials");
    return JwtUtil.generateToken(userId, username);
  }
}
