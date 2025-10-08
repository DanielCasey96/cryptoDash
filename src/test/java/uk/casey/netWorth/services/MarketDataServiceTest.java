package uk.casey.netWorth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.casey.netWorth.models.CoinGeckoResponseModel;
import uk.casey.netWorth.services.providers.IMarketDataProvider;

@ExtendWith(MockitoExtension.class)
public class MarketDataServiceTest {

  @Mock private IMarketDataProvider mockMarketDataProvider;

  @InjectMocks protected MarketDataService marketDataService;

  @Test
  void getMarketListReturnsSuccess() {
    when(mockMarketDataProvider.getMarketList("GBP"))
        .thenReturn(List.of(new CoinGeckoResponseModel()));

    List<CoinGeckoResponseModel> result = marketDataService.getMarketList("GBP");
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void getMarketItemReturnsSuccess() {
    when(mockMarketDataProvider.getItem("bitcoin", "GBP")).thenReturn(new CoinGeckoResponseModel());

    CoinGeckoResponseModel result = marketDataService.getItem("bitcoin", "GBP");
    assertNotNull(result);
  }
}
