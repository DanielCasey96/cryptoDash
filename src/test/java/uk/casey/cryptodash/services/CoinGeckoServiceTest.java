package uk.casey.cryptodash.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;
import uk.casey.cryptodash.services.providers.CoinGeckoProvider;

@ExtendWith(MockitoExtension.class)
public class CoinGeckoServiceTest {

  @Mock private CoinGeckoProvider mockCoinGeckoProvider;

  @InjectMocks protected CoinGeckoService coinGeckoService;

  @Test
  void getMarketListReturnsSuccess() {
    when(mockCoinGeckoProvider.getMarketList("GBP"))
        .thenReturn(List.of(new CoinGeckoResponseModel()));

    List<CoinGeckoResponseModel> result = coinGeckoService.getMarketList("GBP");
    assertNotNull(result);
  }
}
