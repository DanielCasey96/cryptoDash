package uk.casey.netWorth.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.casey.netWorth.models.WatchListItemModel;
import uk.casey.netWorth.services.providers.IDataBaseProvider;

@ExtendWith(MockitoExtension.class)
public class WatchListServiceTest {

  @Mock private IDataBaseProvider postGresProvider;

  @InjectMocks private WatchListService watchListService;

  @Test
  void getWatchList_returnsOk() throws SQLException {
    UUID userId = UUID.randomUUID();
    List<WatchListItemModel> response = List.of(new WatchListItemModel());
    when(postGresProvider.getWatchList(userId)).thenReturn(response);

    List<WatchListItemModel> returned = watchListService.getWatchList(userId);

    assertEquals(returned, response);
    assertEquals(1, returned.size());
  }

  @Test
  void getWatchList_returnsSQLException() throws SQLException {
    UUID userId = UUID.randomUUID();
    when(postGresProvider.getWatchList(userId)).thenThrow(new SQLException("sadge"));

    SQLException ex = assertThrows(SQLException.class, () -> watchListService.getWatchList(userId));
    assertEquals("sadge", ex.getMessage());
  }

  @Test
  void addToWatchList_returnsOk() throws SQLException {
    UUID userId = UUID.randomUUID();
    String name = "big Mac";
    String type = "Savings";
    String provider = "Vanguard";
    String category = "Holiday";
    BigDecimal value = new BigDecimal("100");
    Timestamp updated_at = new Timestamp(System.currentTimeMillis());
    when(postGresProvider.createItem(userId, name, type, provider, category, value, updated_at))
        .thenReturn(true);

    boolean returned =
        watchListService.addToWatchlist(userId, name, type, provider, category, value, updated_at);

    assertEquals(true, returned);
  }

  @Test
  void addToWatchList_returnsSQLException() throws SQLException {
    UUID userId = UUID.randomUUID();
    String name = "big Mac";
    String type = "Savings";
    String provider = "Vanguard";
    String category = "Holiday";
    BigDecimal value = new BigDecimal("100");
    Timestamp updated_at = new Timestamp(System.currentTimeMillis());
    when(postGresProvider.createItem(userId, name, type, provider, category, value, updated_at))
        .thenThrow(new SQLException("sadge"));

    SQLException ex =
        assertThrows(
            SQLException.class,
            () ->
                watchListService.addToWatchlist(
                    userId, name, type, provider, category, value, updated_at));
    assertEquals("sadge", ex.getMessage());
  }

  @Test
  void deleteFromWatchList_returnsOk() throws SQLException {
    UUID userId = UUID.randomUUID();
    int assetId = 1;
    when(postGresProvider.deleteItem(userId, assetId)).thenReturn(true);

    boolean returned = watchListService.deleteFromWatchlist(userId, assetId);

    assertEquals(true, returned);
  }

  @Test
  void deleteFromWatchList_returnsSQLException() throws SQLException {
    UUID userId = UUID.randomUUID();
    int assetId = 1;
    when(postGresProvider.deleteItem(userId, assetId)).thenThrow(new SQLException("sadge"));

    SQLException ex =
        assertThrows(
            SQLException.class, () -> watchListService.deleteFromWatchlist(userId, assetId));
    assertEquals("sadge", ex.getMessage());
  }

  @Test
  void updateItemWatchList_returnsOk() throws SQLException {
    UUID userId = UUID.randomUUID();
    int assetId = 1;
    BigDecimal value = new BigDecimal("100");
    when(postGresProvider.updateItem(userId, assetId, value)).thenReturn(true);

    boolean returned = watchListService.updateItemWatchList(userId, assetId, value);

    assertEquals(true, returned);
  }

  @Test
  void updateItemWatchList_returnsSQLException() throws SQLException {
    UUID userId = UUID.randomUUID();
    int assetId = 1;
    BigDecimal value = new BigDecimal("100");
    when(postGresProvider.updateItem(userId, assetId, value)).thenThrow(new SQLException("sadge"));

    SQLException ex =
        assertThrows(
            SQLException.class, () -> watchListService.updateItemWatchList(userId, assetId, value));
    assertEquals("sadge", ex.getMessage());
  }
}
