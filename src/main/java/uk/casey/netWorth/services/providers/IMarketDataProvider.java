package uk.casey.netWorth.services.providers;

import java.util.List;
import uk.casey.netWorth.models.CoinGeckoResponseModel;

public interface IMarketDataProvider {

  List<CoinGeckoResponseModel> getMarketList(String vsCurrency);

  CoinGeckoResponseModel getItem(String itemId, String vsCurrency);
}
