package uk.casey.cryptodash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import uk.casey.cryptodash.models.CoinGeckoResponseModel;
import uk.casey.cryptodash.services.CoinGeckoService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MarketControllerIntegrationTest {

  @LocalServerPort private int port;

  @Autowired TestRestTemplate restTemplate;

  @Autowired FakeCoinGeckoConfig.FakeCoinGeckoService fakeService;

  @Test
  void getTopMarketsList_returnsOk() {
    fakeService.setMarketList(List.of(new CoinGeckoResponseModel()));

    String url = "http://localhost:" + port + "/api/markets/top?limit=10&fiat=GBP";
    ResponseEntity<List<CoinGeckoResponseModel>> response =
        restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    assertEquals(200, response.getStatusCode().value());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getTopMarketsList_returnsEmptyList() {
    fakeService.setMarketList(Collections.emptyList());

    String url = "http://localhost:" + port + "/api/markets/top?limit=10&fiat=GBP";
    ResponseEntity<List<CoinGeckoResponseModel>> response =
        restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    assertEquals(200, response.getStatusCode().value());
    assertEquals(0, response.getBody().size());
  }

  @Test
  void getTopMarketsList_reliesOnDefaultRequestParams() {
    fakeService.setMarketList(List.of(new CoinGeckoResponseModel()));

    String url = "http://localhost:" + port + "/api/markets/top";
    ResponseEntity<List<CoinGeckoResponseModel>> response =
        restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    assertEquals("GBP", fakeService.getLastFiat());
    assertEquals(200, response.getStatusCode().value());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getTopMarketsList_passesDifferentFiat() {
    fakeService.setMarketList(List.of(new CoinGeckoResponseModel()));

    String url = "http://localhost:" + port + "/api/markets/top?limit=10&fiat=USD";
    ResponseEntity<List<CoinGeckoResponseModel>> response =
        restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    assertEquals("USD", fakeService.getLastFiat());
    assertEquals(200, response.getStatusCode().value());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getTopMarketsList_returns404_invalidUrl() {
    fakeService.setMarketList(List.of(new CoinGeckoResponseModel()));

    String url = "http://localhost:" + port + "/api/markets/topOfThePops";
    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);

    assertEquals(404, response.getStatusCode().value());
    assertEquals(null, response.getBody());
  }

  @Test
  void getTopMarketsList_returns500_whenServerError() {
    fakeService.setThrowError(true);
    try {
      String url = "http://localhost:" + port + "/api/markets/top";
      ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);

      assertEquals(500, response.getStatusCode().value());
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
          throw new RuntimeException();
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
