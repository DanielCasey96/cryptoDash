package uk.casey.cryptodash.services.providers;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.casey.cryptodash.models.MarketListResponseModel;

@Component
public class CoinGeckoProvider {

  private final RestTemplate restTemplate;

  public CoinGeckoProvider(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public MarketListResponseModel getMarketList(String vsCurrency) {
    String baseUrl = "https://api.coingecko.com/api/v3/coints/markets";
    String url =
        baseUrl
            + "?vsCurrency="
            + vsCurrency
            + "&order=market_cap_desc&per_page=10&page=1&price_change_percentage=24h";
    return restTemplate.getForObject(url, MarketListResponseModel.class);
  }
}
