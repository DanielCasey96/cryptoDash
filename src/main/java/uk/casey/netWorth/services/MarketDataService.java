package uk.casey.netWorth.services;

import java.util.List;
import org.springframework.stereotype.Service;
import uk.casey.netWorth.models.CoinGeckoResponseModel;
import uk.casey.netWorth.services.providers.IMarketDataProvider;

@Service
public class MarketDataService {

  private final IMarketDataProvider provider;

  public MarketDataService(IMarketDataProvider provider) {
    this.provider = provider;
  }

  public List<CoinGeckoResponseModel> getMarketList(String vsCurrency) {
    return provider.getMarketList(vsCurrency);
  }

  public CoinGeckoResponseModel getItem(String itemId, String vsCurrency) {
    return provider.getItem(itemId, vsCurrency);
  }
}
