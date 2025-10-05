package uk.casey.netWorth.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.casey.netWorth.models.CoinGeckoResponseModel;
import uk.casey.netWorth.services.CoinGeckoService;
import uk.casey.netWorth.utils.IJwtService;

@RestController
public class CryptoMarketController {

  private static final Logger logger = LoggerFactory.getLogger(CryptoMarketController.class);

  private final CoinGeckoService coinGeckoService;
  private final IJwtService jwtService;

  public CryptoMarketController(CoinGeckoService coinGeckoService, IJwtService jwtService) {
    this.coinGeckoService = coinGeckoService;
    this.jwtService = jwtService;
  }

  private boolean unauthorised(String token) {
    return token == null || token.isEmpty() || !jwtService.validateToken(token);
  }

  @GetMapping("/api/markets/top")
  public ResponseEntity<List<CoinGeckoResponseModel>> getMarketList(
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "GBP") String fiat,
      @RequestHeader("authorisation") String token) {

    if (unauthorised(token)) return ResponseEntity.status(401).build();
    logger.info("JWT format is valid, proceeding with request");

    List<CoinGeckoResponseModel> marketList = coinGeckoService.getMarketList(fiat);

    return ResponseEntity.ok(marketList);
  }

  @GetMapping("/api/markets/item")
  public ResponseEntity<CoinGeckoResponseModel> getMarketItem(
      @RequestParam("assetId") String assetId,
      @RequestParam("fiat") String fiat,
      @RequestHeader("authorisation") String token) {

    if (unauthorised(token)) return ResponseEntity.status(401).build();
    logger.info("JWT format is valid, proceeding with request");

    CoinGeckoResponseModel item = coinGeckoService.getItem(assetId, fiat);

    return ResponseEntity.ok(item);
  }
}
