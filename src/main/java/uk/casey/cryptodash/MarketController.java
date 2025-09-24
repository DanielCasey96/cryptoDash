package uk.casey.cryptodash;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;
import uk.casey.cryptodash.models.MarketListResponseModel;

@RestController
public class MarketController {

  @GetMapping("/api/markets/top")
  public ResponseEntity<MarketListResponseModel> getMarketList(
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "GBP") String fiat) {

    MarketListResponseModel marketListResponseModel = new MarketListResponseModel();

    // TODO call the service to

    return ResponseEntity.ok(marketListResponseModel);
  }

  @GetMapping("/api/markets/item")
  public ResponseEntity<CoinGeckoResponseModel> getMarketItem(
      @RequestParam("assetId") String assetId, @RequestParam("fiat") String fiat) {

    CoinGeckoResponseModel coinGeckoResponseModel = new CoinGeckoResponseModel();

    // TODO call the service
    return ResponseEntity.ok(coinGeckoResponseModel);
  }
}
