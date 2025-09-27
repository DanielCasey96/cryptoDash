package uk.casey.cryptodash.services.providers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.casey.cryptodash.models.WatchResponseModel;

@Component
public class DataBaseProvider {

  @Autowired private Properties prop;

  public List<WatchResponseModel> getWatchList(String userId) throws SQLException {

    try {
      Connection conn = databaseSetUp();

      String sql = "WHATEVER IT IS";
      String temp = "a";

      try (PreparedStatement statement = conn.prepareStatement(sql)) {
        statement.setString(1, temp);
      }
    } catch (SQLException ex) {
      throw new SQLException(ex.getMessage());
    }

    return null;
  }

  private Connection databaseSetUp() throws SQLException {

    try (InputStream input =
        getClass().getClassLoader().getResourceAsStream("application.properties")) {
      prop.load(input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String url = prop.getProperty("url");
    String username = prop.getProperty("username");
    String password = prop.getProperty("password");

    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }
}
