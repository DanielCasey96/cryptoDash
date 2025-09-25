package uk.casey.cryptodash.services.providers;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;

@Component
public class CoinGeckoProvider {

  private final RestTemplate restTemplate;

  public CoinGeckoProvider(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<CoinGeckoResponseModel> getMarketList(String vsCurrency) {
    String baseUrl = "https://api.coingecko.com/api/v3/coins/markets";
    String url =
        baseUrl
            + "?vs_currency="
            + vsCurrency
            + "&order=market_cap_desc&per_page=10&page=1&price_change_percentage=24h";
    CoinGeckoResponseModel[] response =
        restTemplate.getForObject(url, CoinGeckoResponseModel[].class);
    assert response != null;
    return Arrays.asList(response);
  }

  public List<CoinGeckoResponseModel> getItem(String itemId, String vsCurrency) {
    String baseUrl = "https://api.coingecko.com/api/v3/coins/markets";
    String url =
        baseUrl + "?vs_currency=" + vsCurrency + "&ids=" + itemId + "&price_change_percentage=24h";
    CoinGeckoResponseModel[] response =
        restTemplate.getForObject(url, CoinGeckoResponseModel[].class);
    assert response != null;
    return Arrays.asList(response);
  }
}
