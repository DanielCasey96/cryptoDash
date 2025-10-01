package uk.casey.cryptodash;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;
import uk.casey.cryptodash.services.CoinGeckoService;
import uk.casey.cryptodash.utils.JwtUtil;

@WebMvcTest(MarketController.class)
public class MarketControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CoinGeckoService coinGeckoService;

  private MockedStatic<JwtUtil> jwtMock;

  @BeforeEach
  void setUpJwt() {
    jwtMock = Mockito.mockStatic(JwtUtil.class);
    jwtMock.when(() -> JwtUtil.isValidJwtFormat(Mockito.anyString())).thenReturn(true);
  }

  @AfterEach
  void tearDownJwt() {
    if (jwtMock != null) jwtMock.close();
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

    when(coinGeckoService.getMarketList("GBP")).thenReturn(List.of(item));

    mockMvc
        .perform(
            get("/api/markets/top")
                .param("limit", "10")
                .param("fiat", "GBP")
                .header(
                    "Authorisation",
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MTEyMGFiZi0zMzlkLTQ2MjctODE4OC0xZTI0ZTc3NTk0NzUiLCJ1c2VybmFtZSI6ImNhc2V5MmJvb2dhbG9vIiwiaWF0IjoxNzU3NzA5NzQ5LCJleHAiOjE3NTc3MDk4Njl9.03sPM5GMx0y0SI0H133ng4EhPdCqjDgv6loU-Q-zVqU"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }
}
