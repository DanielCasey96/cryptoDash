package uk.casey.netWorth.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import uk.casey.netWorth.models.WatchListItemModel;
import uk.casey.netWorth.services.providers.IDataBaseProvider;

@Service
public class WatchListService {

  private final IDataBaseProvider postGresProvider;

  public WatchListService(IDataBaseProvider postGresProvider) {
    this.postGresProvider = postGresProvider;
  }

  public List<WatchListItemModel> getWatchList(UUID userId) throws SQLException {
    return postGresProvider.getWatchList(userId);
  }

  public boolean addToWatchlist(
      UUID userId,
      String name,
      String type,
      String provider,
      String category,
      BigDecimal value,
      Timestamp updated_at)
      throws SQLException {
    return postGresProvider.createItem(userId, name, type, provider, category, value, updated_at);
  }

  public boolean deleteFromWatchlist(UUID userId, int assetId) throws SQLException {
    return postGresProvider.deleteItem(userId, assetId);
  }

  public boolean updateItemWatchList(UUID userId, int assetId, BigDecimal value)
      throws SQLException {
    return postGresProvider.updateItem(userId, assetId, value);
  }
}
