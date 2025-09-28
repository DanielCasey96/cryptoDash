package uk.casey.cryptodash.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import uk.casey.cryptodash.models.WatchListItemModel;
import uk.casey.cryptodash.services.providers.DataBaseProvider;

@Service
public class WatchListService {

  private final DataBaseProvider dataBaseProvider;

  public WatchListService(DataBaseProvider dataBaseProvider) {
    this.dataBaseProvider = dataBaseProvider;
  }

  public List<WatchListItemModel> getWatchList(UUID userId) {
    return dataBaseProvider.getWatchList(userId);
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
    return dataBaseProvider.createItem(userId, name, type, provider, category, value, updated_at);
  }

  public boolean deleteFromWatchlist(UUID userId, int assetId) {
    return dataBaseProvider.deleteItem(userId, assetId);
  }

  public boolean updateItemWatchList(UUID userId, int assetId, BigDecimal value) {
    return dataBaseProvider.updateItem(userId, assetId, value);
  }
}
