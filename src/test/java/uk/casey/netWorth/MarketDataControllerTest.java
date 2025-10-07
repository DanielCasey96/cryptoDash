package uk.casey.netWorth;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.casey.netWorth.controllers.MarketDataController;
import uk.casey.netWorth.models.CoinGeckoResponseModel;
import uk.casey.netWorth.services.MarketDataService;
import uk.casey.netWorth.utils.IJwtService;

@WebMvcTest(MarketDataController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MarketDataControllerTest {

  private final String TOKEN =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MTEyMGFiZi0zMzlkLTQ2MjctODE4OC0xZTI0ZTc3NTk0NzUiLCJ1c2VybmFtZSI6ImNhc2V5MmJvb2dhbG9vIiwiaWF0IjoxNzU3NzA5NzQ5LCJleHAiOjE3NTc3MDk4Njl9.03sPM5GMx0y0SI0H133ng4EhPdCqjDgv6loU-Q-zVqU";

  @Autowired private MockMvc mockMvc;

  @MockitoBean private MarketDataService marketDataService;

  @MockitoBean private IJwtService jwtService;

  @BeforeEach
  void setUpJwt() {
    when(jwtService.validateToken(anyString())).thenReturn(true);
  }

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

    when(marketDataService.getMarketList("GBP")).thenReturn(List.of(item));

    mockMvc
        .perform(
            get("/api/markets/top")
                .param("limit", "10")
                .param("fiat", "GBP")
                .header("Authorisation", TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }
}
