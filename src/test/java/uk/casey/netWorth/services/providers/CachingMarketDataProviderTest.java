package uk.casey.netWorth.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.casey.netWorth.models.CoinGeckoResponseModel;

@ExtendWith(MockitoExtension.class)
public class CachingMarketDataProviderTest {

  @Mock private CoinGeckoProvider delegate;

  private CachingMarketDataProvider cachingMarketDataProvider;

  @BeforeEach
  void setUp() {
    cachingMarketDataProvider = new CachingMarketDataProvider(delegate);
  }

  @Test
  void getMarketList_goesToDelegate() {
    List<CoinGeckoResponseModel> gives = List.of(new CoinGeckoResponseModel());
    when(delegate.getMarketList(any())).thenReturn(gives);

    List<CoinGeckoResponseModel> response = cachingMarketDataProvider.getMarketList("GBP");

    verify(delegate, times(1)).getMarketList(any());
  }

  @Test
  void getMarketList_returnsCache() {
    List<CoinGeckoResponseModel> response1 = List.of(new CoinGeckoResponseModel());
    List<CoinGeckoResponseModel> response2 = List.of(new CoinGeckoResponseModel());
    when(delegate.getMarketList("GBP")).thenReturn(response1, response2);

    List<CoinGeckoResponseModel> check1 = cachingMarketDataProvider.getMarketList("GBP");
    List<CoinGeckoResponseModel> check2 = cachingMarketDataProvider.getMarketList("GBP");

    assertThat(check1).isSameAs(response1);
    assertThat(check2).isSameAs(response1);

    verify(delegate, times(1)).getMarketList("GBP");
  }

  @Test
  void getItem_goesToDelegate() {
    CoinGeckoResponseModel gives = new CoinGeckoResponseModel();
    when(delegate.getItem("bitcoin", "GBP")).thenReturn(gives);

    CoinGeckoResponseModel response = cachingMarketDataProvider.getItem("bitcoin", "GBP");

    verify(delegate, times(1)).getItem("bitcoin", "GBP");
  }

  @Test
  void getItem_returnsCache() {
    CoinGeckoResponseModel gives1 = new CoinGeckoResponseModel();
    CoinGeckoResponseModel gives2 = new CoinGeckoResponseModel();
    when(delegate.getItem("bitcoin", "GBP")).thenReturn(gives1, gives2);

    CoinGeckoResponseModel check1 = cachingMarketDataProvider.getItem("bitcoin", "GBP");
    CoinGeckoResponseModel check2 = cachingMarketDataProvider.getItem("bitcoin", "GBP");

    assertThat(check1).isSameAs(gives1);
    assertThat(check2).isSameAs(gives1);

    verify(delegate, times(1)).getItem("bitcoin", "GBP");
  }
}
