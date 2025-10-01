package uk.casey.cryptodash.services.providers;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;
import uk.casey.cryptodash.models.WatchListItemModel;

@Component
public class DataBaseProvider {

  private final DataSource dataSource;

  public DataBaseProvider(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public List<WatchListItemModel> getWatchList(UUID userId) {

    String sql =
        "SELECT id, name, type, provider, category, value, updated_at "
            + "FROM products WHERE user_id = ?";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)) {

      statement.setObject(1, userId);

      List<WatchListItemModel> watchList = new ArrayList<>();
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          WatchListItemModel response = new WatchListItemModel();
          response.setId(rs.getString("id"));
          response.setName(rs.getString("name"));
          response.setType(rs.getString("type"));
          response.setProvider(rs.getString("provider"));
          response.setValue(rs.getBigDecimal("value"));
          response.setCategory(rs.getString("category"));
          response.setUpdated_at(rs.getTimestamp("updated_at"));
          watchList.add(response);
        }
      }
      return watchList;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean createItem(
      UUID userId,
      String name,
      String type,
      String provider,
      String category,
      BigDecimal value,
      Timestamp updated_at)
      throws SQLException {

    String sql =
        "INSERT INTO products (user_id, name, type, provider, category, value, updated_at) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)) {

      statement.setObject(1, userId);
      statement.setString(2, name);
      statement.setString(3, type);
      statement.setString(4, provider);
      statement.setString(5, category);
      statement.setBigDecimal(6, value);
      statement.setTimestamp(7, updated_at);

      int rowsCreated = statement.executeUpdate();
      return rowsCreated > 0;
    } catch (SQLException ex) {
      throw new SQLException(ex.getMessage());
    }
    // TODO return id
  }

  public boolean deleteItem(UUID userId, int assetId) {

    String sql = "DELETE FROM products WHERE user_id = ? AND id = ?";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)) {

      statement.setObject(1, userId);
      statement.setInt(2, assetId);

      int rowsRemoved = statement.executeUpdate();
      return rowsRemoved > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean updateItem(UUID userId, int assetId, BigDecimal value) {

    String sql = "UPDATE products SET value = ? WHERE user_id = ? AND id = ?";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)) {
      statement.setBigDecimal(1, value);
      statement.setObject(2, userId);
      statement.setInt(3, assetId);

      int rowsUpdated = statement.executeUpdate();
      return rowsUpdated > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    // TODO return value
  }
}
