package uk.casey.netWorth.services;

import java.util.List;
import org.springframework.stereotype.Service;
import uk.casey.netWorth.models.CoinGeckoResponseModel;
import uk.casey.netWorth.services.providers.CoinGeckoProvider;

@Service
public class CoinGeckoService {

  private final CoinGeckoProvider coinGeckoProvider;

  public CoinGeckoService(CoinGeckoProvider coinGeckoProvider) {
    this.coinGeckoProvider = coinGeckoProvider;
  }

  public List<CoinGeckoResponseModel> getMarketList(String vsCurrency) {
    return coinGeckoProvider.getMarketList(vsCurrency);
  }

  public CoinGeckoResponseModel getItem(String itemId, String vsCurrency) {
    return coinGeckoProvider.getItem(itemId, vsCurrency);
  }
}
