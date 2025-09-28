package uk.casey.cryptodash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;
import uk.casey.cryptodash.services.CoinGeckoService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServerletControllerIntegrationTest {

  @LocalServerPort private int port;

  @Autowired TestRestTemplate restTemplate;

  @MockitoBean private CoinGeckoService coinGeckoService;

  @Test
  void getTopMarketList_returnsOk() {
    String url = "http://localhost:" + port + "/api/markets/top?limit=10&fiat=GBP";
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

    ResponseEntity<List<CoinGeckoResponseModel>> response =
        restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<CoinGeckoResponseModel>>() {});

    assertEquals(200, response.getStatusCode().value());
    Assertions.assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }
}
