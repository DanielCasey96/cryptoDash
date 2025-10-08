package uk.casey.netWorth.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.casey.netWorth.exceptions.AuthenticationException;
import uk.casey.netWorth.services.providers.IDataBaseProvider;
import uk.casey.netWorth.utils.IJwtService;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

  @Mock IJwtService jwtService;

  @Mock IDataBaseProvider postGresProvider;

  @InjectMocks UsersService usersService;

  @Test
  void registerUser_returnsOk() throws SQLException {
    String username = "David";
    String password = "1234";
    String email = "clear@yahoo.com";
    String userId = UUID.randomUUID().toString();
    when(postGresProvider.registerUser(anyString(), anyString(), anyString())).thenReturn(userId);

    String response = usersService.registerUser(username, password, email);

    assertEquals(userId, response);
  }

  @Test
  void registerUser_hashesPassword() throws SQLException {
    String username = "David";
    String password = "1234";
    String email = "clear@yahoo.com";
    String userId = UUID.randomUUID().toString();
    when(postGresProvider.registerUser(anyString(), anyString(), anyString())).thenReturn(userId);

    usersService.registerUser(username, password, email);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(postGresProvider)
        .registerUser(Mockito.eq(username), captor.capture(), Mockito.eq(email));

    String hashed = captor.getValue();
    assertNotEquals(password, hashed);
  }

  @Test
  void authoriseUser_returnsOk() throws SQLException {
    UUID userId = UUID.randomUUID();
    String username = "David";
    String password = "1234";
    String token = "eyshaha";
    String storedHash = BCrypt.hashpw(password, BCrypt.gensalt());

    when(postGresProvider.checkPassword(userId, username)).thenReturn(storedHash);
    when(jwtService.generateToken(userId, username)).thenReturn(token);

    String response = usersService.authoriseUser(userId, username, password);

    assertEquals(token, response);
  }

  @Test
  void authoriseUser_returns_invalidCredentials() throws SQLException {
    UUID userId = UUID.randomUUID();
    String username = "David";
    String password = "1234";
    String fakePassword = "fakePassword";
    String storedHash = BCrypt.hashpw(fakePassword, BCrypt.gensalt());
    when(postGresProvider.checkPassword(userId, username)).thenReturn(storedHash);

    AuthenticationException ex =
        assertThrows(
            AuthenticationException.class,
            () -> usersService.authoriseUser(userId, username, password));
    assertNull(ex.getCause());
  }

  @Test
  void authoriseUser_checkPassword_failsWithRuntimeException() throws SQLException {
    UUID userId = UUID.randomUUID();
    String username = "David";
    String password = "1234";
    String fakePassword = "fakePassword";
    when(postGresProvider.checkPassword(userId, username)).thenReturn(fakePassword);

    RuntimeException ex =
        assertThrows(
            RuntimeException.class, () -> usersService.authoriseUser(userId, username, password));
    assertNull(ex.getCause());
  }
}
