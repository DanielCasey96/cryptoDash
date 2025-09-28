package uk.casey.cryptodash;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.casey.cryptodash.models.WatchListItemModel;
import uk.casey.cryptodash.services.WatchListService;

@RestController
public class WatchListController {

  private final WatchListService watchListService;

  public WatchListController(WatchListService watchListService) {
    this.watchListService = watchListService;
  }

  @GetMapping("/api/user/watchlist")
  public ResponseEntity<List<WatchListItemModel>> getWatchList(
      @RequestParam String fiat, @RequestHeader("userId") UUID userId) {

    List<WatchListItemModel> watchlist = watchListService.getWatchList(userId);

    return ResponseEntity.ok().body(watchlist);
  }

  @PostMapping(path = "/api/user/watchlist", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> addToWatchlist(
      @RequestParam String name,
      @RequestHeader("userId") UUID userId,
      @RequestBody WatchListItemModel req)
      throws SQLException {

    watchListService.addToWatchlist(
        userId,
        name,
        req.getType(),
        req.getProvider(),
        req.getCategory(),
        req.getValue(),
        Timestamp.from(Instant.now()));

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/api/user/watchlist")
  public ResponseEntity<Boolean> deleteWatchlist(
      @RequestParam int assetId, @RequestHeader("userId") UUID userId) {

    watchListService.deleteFromWatchlist(userId, assetId);

    return ResponseEntity.ok().build();
  }

  @PatchMapping("/api/user/watchlist")
  public ResponseEntity<Boolean> updateWatchlist(
      @RequestParam int assetId,
      @RequestHeader("userId") UUID userId,
      @RequestBody WatchListItemModel req) {

    watchListService.updateItemWatchList(userId, assetId, req.getValue());

    return ResponseEntity.ok().build();
  }
}
