package uk.casey.cryptodash;

import java.sql.SQLException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.casey.cryptodash.models.WatchResponseModel;
import uk.casey.cryptodash.services.WatchListService;

@Controller
public class WatchListController {

  private WatchListService watchListService;

  @GetMapping("/api/user/watchlist")
  public ResponseEntity<List<WatchResponseModel>> getWatchList(
      @RequestParam String fiat, @RequestHeader("userId") String userId) throws SQLException {

    List<WatchResponseModel> watchlist = watchListService.getWatchList(userId);

    return ResponseEntity.ok().body(watchlist);
  }
}
