package uk.casey.netWorth.controllers;

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
import uk.casey.netWorth.models.WatchListItemModel;
import uk.casey.netWorth.services.WatchListService;
import uk.casey.netWorth.utils.IJwtService;

@RestController
public class WatchListController {

  private static final Logger logger = LoggerFactory.getLogger(WatchListController.class);

  private final WatchListService watchListService;
  private final IJwtService jwtService;

  public WatchListController(WatchListService watchListService, IJwtService jwtService) {
    this.watchListService = watchListService;
    this.jwtService = jwtService;
  }

  private boolean unauthorised(String token) {
    return token == null || token.isEmpty() || !jwtService.validateToken(token);
  }

  @GetMapping("/api/user/watchlist")
  public ResponseEntity<List<WatchListItemModel>> getWatchList(
      @RequestHeader("userId") UUID userId, @RequestHeader("authorisation") String token)
      throws SQLException {
    logger.info("Received request with userId: {} and token: {}", userId, token);

    if (unauthorised(token)) return ResponseEntity.status(401).build();
    logger.info("JWT format is valid, proceeding with request");

    List<WatchListItemModel> watchlist = watchListService.getWatchList(userId);

    return ResponseEntity.ok().body(watchlist);
  }

  @PostMapping(path = "/api/user/watchlist", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> addToWatchlistItem(
      @RequestParam String name,
      @RequestHeader("userId") UUID userId,
      @RequestHeader("authorisation") String token,
      @RequestBody WatchListItemModel req)
      throws SQLException {
    logger.info("Received request with userId: {} and token: {}", userId, token);

    if (unauthorised(token)) return ResponseEntity.status(401).build();
    logger.info("JWT format is valid, proceeding with request");

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
  public ResponseEntity<Boolean> deleteWatchlistItem(
      @RequestParam int assetId,
      @RequestHeader("userId") UUID userId,
      @RequestHeader("authorisation") String token)
      throws SQLException {
    logger.info("Received request with userId: {} and token: {}", userId, token);

    if (unauthorised(token)) return ResponseEntity.status(401).build();
    logger.info("JWT format is valid, proceeding with request");

    watchListService.deleteFromWatchlist(userId, assetId);

    return ResponseEntity.ok().build();
  }

  @PatchMapping(path = "/api/user/watchlist", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> updateWatchlistItem(
      @RequestParam int assetId,
      @RequestHeader("userId") UUID userId,
      @RequestHeader("authorisation") @NonNull String token,
      @RequestBody WatchListItemModel req)
      throws SQLException {
    logger.info("Received request with userId: {} and token: {}", userId, token);

    if (unauthorised(token)) return ResponseEntity.status(401).build();
    logger.info("JWT format is valid, proceeding with request");

    watchListService.updateItemWatchList(userId, assetId, req.getValue());

    return ResponseEntity.ok().build();
  }
}
