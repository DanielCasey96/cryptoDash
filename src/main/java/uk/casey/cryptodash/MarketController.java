package uk.casey.cryptodash;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;
import uk.casey.cryptodash.services.CoinGeckoService;

@RestController
public class MarketController {
  private CoinGeckoService coinGeckoService;

  public MarketController(CoinGeckoService coinGeckoService) {
    this.coinGeckoService = coinGeckoService;
  }

  @GetMapping("/api/markets/top")
  public ResponseEntity<List<CoinGeckoResponseModel>> getMarketList(
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "GBP") String fiat) {

    List<CoinGeckoResponseModel> marketList = coinGeckoService.getMarketList(fiat);

    return ResponseEntity.ok(marketList);
  }

  @GetMapping("/api/markets/item")
  public ResponseEntity<List<CoinGeckoResponseModel>> getMarketItem(
      @RequestParam("assetId") String assetId,
      @RequestParam("fiat") String fiat) {

    List<CoinGeckoResponseModel> item = coinGeckoService.getItem(assetId, fiat);

    return ResponseEntity.ok(item);
  }
}
