package uk.casey.netWorth.services.providers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.casey.netWorth.models.WatchListItemModel;

@ExtendWith(MockitoExtension.class)
public class PostGresProviderTest {

  @Mock private DataSource dataSource;

  @Mock private Connection connection;

  @Mock private PreparedStatement statement;

  @Mock private ResultSet resultSet;

  private PostGresProvider postGresProvider() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    return new PostGresProvider(dataSource);
  }

  @Test
  void getWatchList_returnsOk() throws Exception {
    UUID userId = UUID.randomUUID();
    PostGresProvider provider = postGresProvider();

    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, true, false);

    when(resultSet.getString("id")).thenReturn("id1", "id2");
    when(resultSet.getString("name")).thenReturn("Name1", "Name2");
    when(resultSet.getString("type")).thenReturn("TYPE1", "TYPE2");
    when(resultSet.getString("provider")).thenReturn("PROV1", "PROV2");
    when(resultSet.getString("category")).thenReturn("CAT1", "CAT2");
    when(resultSet.getBigDecimal("value"))
        .thenReturn(new BigDecimal("10.50"), new BigDecimal("20.00"));
    Timestamp t1 = new Timestamp(System.currentTimeMillis());
    Timestamp t2 = new Timestamp(t1.getTime() + 1000);
    when(resultSet.getTimestamp("updated_at")).thenReturn(t1, t2);

    List<WatchListItemModel> result = provider.getWatchList(userId);

    assertEquals(2, result.size());
    assertEquals("id1", result.get(0).getId());
    assertEquals("id2", result.get(1).getId());
    assertEquals(new BigDecimal("10.50"), result.get(0).getValue());
    assertEquals(new BigDecimal("20.00"), result.get(1).getValue());

    InOrder order = inOrder(dataSource, connection, statement);
    order.verify(dataSource).getConnection();
    order.verify(connection).prepareStatement(startsWith("SELECT id"));
    verify(statement).setObject(1, userId);
    verify(resultSet, times(3)).next();
  }

  @Test
  void getWatchList_returnsEmpty() throws Exception {
    UUID userId = UUID.randomUUID();
    PostGresProvider provider = postGresProvider();

    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    List<WatchListItemModel> result = provider.getWatchList(userId);

    assertEquals(0, result.size());
  }

  @Test
  void getWatchList_sqlExceptionThrown() throws Exception {
    UUID userId = UUID.randomUUID();
    when(dataSource.getConnection()).thenThrow(new SQLException("sadge"));
    PostGresProvider provider = new PostGresProvider(dataSource);

    SQLException ex = assertThrows(SQLException.class, () -> provider.getWatchList(userId));
    assertTrue(ex.getCause() instanceof SQLException);
  }

  @Test
  void createWatchList_returnsOk() throws Exception {
    UUID userId = UUID.randomUUID();
    String name = "Name";
    String type = "TYPE";
    String company = "PROV";
    String category = "CAT";
    BigDecimal value = new BigDecimal("10.50");
    Timestamp t1 = new Timestamp(System.currentTimeMillis());
    PostGresProvider provider = postGresProvider();
    when(statement.executeUpdate()).thenReturn(1);

    boolean created = provider.createItem(userId, name, type, company, category, value, t1);

    assertTrue(created);
    verify(statement).setObject(1, userId);
    verify(statement).setString(2, name);
    verify(statement).setString(3, type);
    verify(statement).setString(4, company);
    verify(statement).setString(5, category);
    verify(statement).setBigDecimal(6, value);
    verify(statement).setTimestamp(7, t1);
    verify(statement, times(1)).executeUpdate();
  }

  @Test
  void createWatchList_sqlExceptionThrown() throws Exception {
    UUID userId = UUID.randomUUID();
    String name = "Name";
    String type = "TYPE";
    String company = "PROV";
    String category = "CAT";
    BigDecimal value = new BigDecimal("10.50");
    Timestamp t1 = new Timestamp(System.currentTimeMillis());
    when(dataSource.getConnection()).thenThrow(new SQLException("sadge"));
    PostGresProvider provider = new PostGresProvider(dataSource);

    SQLException ex =
        assertThrows(
            SQLException.class,
            () -> provider.createItem(userId, name, type, company, category, value, t1));
    assertTrue(ex.getCause() instanceof SQLException);
  }

  @Test
  void deleteWatchList_returnsOk() throws Exception {
    UUID userId = UUID.randomUUID();
    int assetId = 1;
    PostGresProvider provider = postGresProvider();
    when(statement.executeUpdate()).thenReturn(1);

    boolean deleted = provider.deleteItem(userId, assetId);
    assertTrue(deleted);
    verify(statement).setObject(1, userId);
    verify(statement).setInt(2, assetId);
    verify(statement, times(1)).executeUpdate();
  }

  @Test
  void deleteWatchList_sqlExceptionThrown() throws Exception {
    UUID userId = UUID.randomUUID();
    int assetId = 1;

    when(dataSource.getConnection()).thenThrow(new SQLException("sadge"));
    PostGresProvider provider = new PostGresProvider(dataSource);

    SQLException ex = assertThrows(SQLException.class, () -> provider.deleteItem(userId, assetId));
    assertTrue(ex.getCause() instanceof SQLException);
  }

  @Test
  void updateWatchList_returnsOk() throws Exception {
    UUID userId = UUID.randomUUID();
    int assetId = 1;
    BigDecimal value = new BigDecimal("10.50");
    PostGresProvider provider = postGresProvider();
    when(statement.executeUpdate()).thenReturn(1);

    boolean updated = provider.updateItem(userId, assetId, value);

    assertTrue(updated);
    verify(statement).setBigDecimal(1, value);
    verify(statement).setObject(2, userId);
    verify(statement).setInt(3, assetId);
    verify(statement, times(1)).executeUpdate();
  }

  @Test
  void updateWatchList_sqlExceptionThrown() throws Exception {
    UUID userId = UUID.randomUUID();
    int assetId = 1;
    BigDecimal value = new BigDecimal("10.50");

    when(dataSource.getConnection()).thenThrow(new SQLException("sadge"));
    PostGresProvider provider = new PostGresProvider(dataSource);

    SQLException ex =
        assertThrows(SQLException.class, () -> provider.updateItem(userId, assetId, value));
    assertTrue(ex.getCause() instanceof SQLException);
  }

  @Test
  void registerUser_returnsOk() throws Exception {
    String username = "David";
    String hashedPassword = "1234";
    String email = "clear@yahoo.com";
    String userId = UUID.randomUUID().toString();
    PostGresProvider provider = postGresProvider();

    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, false);
    when(resultSet.getString("id")).thenReturn(userId);

    String response = provider.registerUser(username, hashedPassword, email);

    verify(statement).setString(1, username);
    verify(statement).setString(2, hashedPassword);
    verify(statement).setString(3, email);

    assertEquals(response, resultSet.getString("id"));
  }

  @Test
  void registerUser_sqlExceptionThrown() throws Exception {
    String username = "David";
    String hashedPassword = "1234";
    String email = "clear@yahoo.com";
    when(dataSource.getConnection()).thenThrow(new SQLException("sadge"));
    PostGresProvider provider = new PostGresProvider(dataSource);

    SQLException ex =
        assertThrows(
            SQLException.class, () -> provider.registerUser(username, hashedPassword, email));
    assertTrue(ex.getCause() instanceof SQLException);
  }

  @Test
  void checkPassword_returnsOk() throws Exception {
    UUID userId = UUID.randomUUID();
    String username = "David";
    PostGresProvider provider = postGresProvider();
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, false);

    String checkPassword = provider.checkPassword(userId, username);

    verify(statement).setObject(1, userId);
    verify(statement).setString(2, username);

    assertEquals(checkPassword, resultSet.getString("password"));
  }

  @Test
  void checkPassword_sqlExceptionThrown() throws Exception {
    UUID userId = UUID.randomUUID();
    String username = "David";
    when(dataSource.getConnection()).thenThrow(new SQLException("sadge"));
    PostGresProvider provider = new PostGresProvider(dataSource);
    SQLException ex =
        assertThrows(SQLException.class, () -> provider.checkPassword(userId, username));
    assertTrue(ex.getCause() instanceof SQLException);
  }
}
