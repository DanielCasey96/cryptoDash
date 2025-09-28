package uk.casey.cryptodash;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;
import uk.casey.cryptodash.services.CoinGeckoService;

@WebMvcTest(MarketController.class)
@ActiveProfiles("test")
class MarketControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired FakeCoinGeckoConfig.FakeCoinGeckoService fakeService;

  @Test
  void getTopMarketsList_returnsOk() throws Exception {
    fakeService.setMarketList(List.of(new CoinGeckoResponseModel()));

    mockMvc
        .perform(get("/api/markets/top").param("limit", "10").param("fiat", "GBP"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void getTopMarketLists_returnsEmptyList() throws Exception {
    fakeService.setMarketList(Collections.emptyList());

    mockMvc
        .perform(get("/api/markets/top").param("limit", "10").param("fiat", "GBP"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void getTopMarketLists_reliesOnDefaultRequestParams() throws Exception {
    fakeService.setMarketList(List.of(new CoinGeckoResponseModel()));

    mockMvc
        .perform(get("/api/markets/top"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void getTopMarketLists_passesDifferentFiat() throws Exception {
    fakeService.setMarketList(List.of(new CoinGeckoResponseModel()));

    mockMvc
        .perform(get("/api/markets/top").param("limit", "10").param("fiat", "USD"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
    assertEquals("USD", fakeService.getLastFiat());
  }

  @Test
  void getTopMarketList_returns404_invalidUrl() throws Exception {
    fakeService.setMarketList(List.of(new CoinGeckoResponseModel()));

    mockMvc
        .perform(get("/api/markets/topOfThePops").param("limit", "10").param("fiat", "GBP"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getTopMarketsList_returns500_whenServerError() throws Exception {
    try {
      fakeService.setThrowError(true);

      mockMvc
          .perform(get("/api/markets/top").param("limit", "10").param("fiat", "GBP"))
          .andExpect(status().is5xxServerError());
    } finally {
      fakeService.setThrowError(false);
    }
  }

  @TestConfiguration
  static class FakeCoinGeckoConfig {
    @Bean
    @Primary
    FakeCoinGeckoService fakeCoinGeckoService() {
      return new FakeCoinGeckoService();
    }

    static class FakeCoinGeckoService extends CoinGeckoService {
      private String lastFiat;
      private boolean throwError = false;
      private List<CoinGeckoResponseModel> marketList = List.of(new CoinGeckoResponseModel());

      FakeCoinGeckoService() {
        super(null);
      }

      String getLastFiat() {
        return lastFiat;
      }

      void setThrowError(boolean throwError) {
        this.throwError = throwError;
      }

      void setMarketList(List<CoinGeckoResponseModel> marketList) {
        this.marketList = marketList;
      }

      @Override
      public List<CoinGeckoResponseModel> getMarketList(String fiat) {
        this.lastFiat = fiat;
        if (throwError) {
          throw new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        return marketList;
      }

      @Override
      public CoinGeckoResponseModel getItem(String assetId, String fiat) {
        return new CoinGeckoResponseModel();
      }
    }
  }
}
