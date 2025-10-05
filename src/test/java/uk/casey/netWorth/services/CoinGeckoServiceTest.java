package uk.casey.netWorth.services;

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
public class CoinGeckoServiceTest {

  @Mock private IMarketDataProvider mockMarketDataProvider;

  @InjectMocks protected CoinGeckoService coinGeckoService;

  @Test
  void getMarketListReturnsSuccess() {
    when(mockMarketDataProvider.getMarketList("GBP"))
        .thenReturn(List.of(new CoinGeckoResponseModel()));

    List<CoinGeckoResponseModel> result = coinGeckoService.getMarketList("GBP");
    assertNotNull(result);
  }

  @Test
  void getMarketItemReturnsSuccess() {
    when(mockMarketDataProvider.getItem("bitcoin", "GBP")).thenReturn(new CoinGeckoResponseModel());

    CoinGeckoResponseModel result = coinGeckoService.getItem("bitcoin", "GBP");
    assertNotNull(result);
  }
}
