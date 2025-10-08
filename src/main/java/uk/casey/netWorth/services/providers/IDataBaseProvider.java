package uk.casey.netWorth.services.providers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import uk.casey.netWorth.models.WatchListItemModel;

public interface IDataBaseProvider {

  List<WatchListItemModel> getWatchList(UUID userId) throws SQLException;

  boolean createItem(
      UUID userId,
      String name,
      String type,
      String provider,
      String category,
      BigDecimal value,
      Timestamp updated_at)
      throws SQLException;

  boolean deleteItem(UUID userId, int assetId) throws SQLException;

  boolean updateItem(UUID userId, int assetId, BigDecimal value) throws SQLException;

  String registerUser(String username, String hashedPassword, String email) throws SQLException;

  String checkPassword(UUID userId, String username) throws SQLException;
}
