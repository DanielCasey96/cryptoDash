package uk.casey.cryptodash;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;
import uk.casey.cryptodash.services.CoinGeckoService;

@WebMvcTest(MarketController.class)
public class MarketControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CoinGeckoService coinGeckoService;

  @Test
  void getTopMarketsList_returnsOk() throws Exception {
    CoinGeckoResponseModel item =
        new CoinGeckoResponseModel(
            "bitcoin",
            "btc",
            "Bitcoin",
            new BigDecimal("50000.00"),
            new BigDecimal("250.00"),
            new BigDecimal("0.50"),
            Instant.parse("2025-01-01T00:00:00Z"));
    item.setMarket_cap(new BigDecimal("1000000000000"));

    when(coinGeckoService.getMarketList("GBP")).thenReturn(List.of(item));

    mockMvc
        .perform(get("/api/markets/top").param("limit", "10").param("fiat", "GBP"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }
}
