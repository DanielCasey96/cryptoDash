package uk.casey.netWorth.services.providers;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.casey.netWorth.config.CoinGeckoProperties;
import uk.casey.netWorth.models.CoinGeckoResponseModel;

@Component
public class CoinGeckoProvider implements IMarketDataProvider {

  private final RestTemplate restTemplate;
  private final String baseUrl;

  public CoinGeckoProvider(RestTemplate restTemplate, CoinGeckoProperties cgProps) {
    this.restTemplate = restTemplate;
    this.baseUrl = cgProps.getBaseUrl();
  }

  @Override
  public List<CoinGeckoResponseModel> getMarketList(String vsCurrency) {
    String fullUrl = baseUrl + "/coins/markets";
    String url =
        fullUrl
            + "?vs_currency="
            + vsCurrency
            + "&order=market_cap_desc&per_page=10&page=1&price_change_percentage=24h";
    CoinGeckoResponseModel[] response =
        restTemplate.getForObject(url, CoinGeckoResponseModel[].class);
    assert response != null;
    return Arrays.asList(response);
  }

  @Override
  public CoinGeckoResponseModel getItem(String itemId, String vsCurrency) {
    String fullUrl = baseUrl + "/coins/markets";
    String url =
        fullUrl + "?vs_currency=" + vsCurrency + "&ids=" + itemId + "&price_change_percentage=24h";
    CoinGeckoResponseModel[] response =
        restTemplate.getForObject(url, CoinGeckoResponseModel[].class);
    assert response != null && response.length > 0;
    return response[0];
  }
}
