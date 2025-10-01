package uk.casey.cryptodash;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import uk.casey.cryptodash.models.WatchListItemModel;
import uk.casey.cryptodash.services.WatchListService;
import uk.casey.cryptodash.utils.JwtUtil;

@RestController
public class WatchListController {

  private final WatchListService watchListService;

  public WatchListController(WatchListService watchListService) {
    this.watchListService = watchListService;
  }

  private static final Logger logger = LoggerFactory.getLogger(WatchListController.class);

  @GetMapping("/api/user/watchlist")
  public ResponseEntity<List<WatchListItemModel>> getWatchList(
      @RequestParam String fiat,
      @RequestHeader("userId") UUID userId,
      @RequestHeader("authorisation") String token) {

    if (!JwtUtil.isValidJwtFormat(token)) {
      return ResponseEntity.status(401).build();
    }

    List<WatchListItemModel> watchlist = watchListService.getWatchList(userId);

    return ResponseEntity.ok().body(watchlist);
  }

  @PostMapping(path = "/api/user/watchlist", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> addToWatchlist(
      @RequestParam String name,
      @RequestHeader("userId") UUID userId,
      @RequestHeader("authorisation") String token,
      @RequestBody WatchListItemModel req)
      throws SQLException {

    if (!JwtUtil.isValidJwtFormat(token)) {
      return ResponseEntity.status(401).build();
    }

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
      @RequestParam int assetId,
      @RequestHeader("userId") UUID userId,
      @RequestHeader("authorisation") String token) {

    if (!JwtUtil.isValidJwtFormat(token)) {
      return ResponseEntity.status(401).build();
    }

    watchListService.deleteFromWatchlist(userId, assetId);

    return ResponseEntity.ok().build();
  }

  @PatchMapping(path = "/api/user/watchlist", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> updateWatchlist(
      @RequestParam int assetId,
      @RequestHeader("userId") UUID userId,
      @RequestHeader("authorisation") @NonNull String token,
      @RequestBody WatchListItemModel req) {

    if (!JwtUtil.isValidJwtFormat(token)) {
      return ResponseEntity.status(401).build();
    }

    watchListService.updateItemWatchList(userId, assetId, req.getValue());

    return ResponseEntity.ok().build();
  }
}
