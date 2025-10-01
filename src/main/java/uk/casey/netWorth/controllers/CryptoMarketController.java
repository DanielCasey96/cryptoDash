package uk.casey.netWorth.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.casey.netWorth.models.CoinGeckoResponseModel;
import uk.casey.netWorth.services.CoinGeckoService;
import uk.casey.netWorth.utils.JwtUtil;

@RestController
public class CryptoMarketController {
  private CoinGeckoService coinGeckoService;

  public CryptoMarketController(CoinGeckoService coinGeckoService) {
    this.coinGeckoService = coinGeckoService;
  }

  @GetMapping("/api/markets/top")
  public ResponseEntity<List<CoinGeckoResponseModel>> getMarketList(
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "GBP") String fiat,
      @RequestHeader("authorisation") String token) {

    if (!JwtUtil.isValidJwtFormat(token)) {
      return ResponseEntity.status(401).build();
    }

    List<CoinGeckoResponseModel> marketList = coinGeckoService.getMarketList(fiat);

    return ResponseEntity.ok(marketList);
  }

  @GetMapping("/api/markets/item")
  public ResponseEntity<CoinGeckoResponseModel> getMarketItem(
      @RequestParam("assetId") String assetId,
      @RequestParam("fiat") String fiat,
      @RequestHeader("authorisation") String token) {

    if (!JwtUtil.isValidJwtFormat(token)) {
      return ResponseEntity.status(401).build();
    }

    CoinGeckoResponseModel item = coinGeckoService.getItem(assetId, fiat);

    return ResponseEntity.ok(item);
  }
}
