package uk.casey.cryptodash.services;

import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;
import uk.casey.cryptodash.models.WatchResponseModel;
import uk.casey.cryptodash.services.providers.DataBaseProvider;

@Service
public class WatchListService {

  private final DataBaseProvider dataBaseProvider;

  public WatchListService(DataBaseProvider dataBaseProvider) {
    this.dataBaseProvider = dataBaseProvider;
  }

  public List<WatchResponseModel> getWatchList(String userId) throws SQLException {
    return dataBaseProvider.getWatchList(userId);
  }
}
