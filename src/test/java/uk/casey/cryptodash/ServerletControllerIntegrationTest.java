// package uk.casey.cryptodash;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;
//
// import java.math.BigDecimal;
// import java.time.Instant;
// import java.util.List;
//
// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.mockito.MockedStatic;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import uk.casey.cryptodash.models.CoinGeckoResponseModel;
// import uk.casey.cryptodash.services.CoinGeckoService;
// import uk.casey.cryptodash.utils.JwtUtil;
//
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// public class ServerletControllerIntegrationTest {
//
//  @LocalServerPort private int port;
//
//  @Autowired TestRestTemplate restTemplate;
//
//  @MockitoBean private CoinGeckoService coinGeckoService;
//
//  private static MockedStatic<JwtUtil> jwtMock;
//
//  @BeforeAll
//  static void initJwtMock() {
//    // Requires `mockito-inline` on test classpath
//    jwtMock = Mockito.mockStatic(JwtUtil.class);
//    // Match null or non-null tokens so the controller branch always passes
//    jwtMock.when(() -> JwtUtil.validateToken(ArgumentMatchers.nullable(String.class)))
//           .thenReturn(true);
//  }
//
//  @AfterAll
//  static void closeJwtMock() {
//    if (jwtMock != null) jwtMock.close();
//  }
//
//  @Test
//  void getTopMarketList_returnsOk() {
//    String url = "http://localhost:" + port + "/api/markets/top?limit=10&fiat=GBP";
//
//    CoinGeckoResponseModel item =
//        new CoinGeckoResponseModel(
//            "bitcoin",
//            "btc",
//            "Bitcoin",
//            new BigDecimal("50000.00"),
//            new BigDecimal("250.00"),
//            new BigDecimal("0.50"),
//            Instant.parse("2025-01-01T00:00:00Z"));
//    item.setMarket_cap(new BigDecimal("1000000000000"));
//
//    when(coinGeckoService.getMarketList("GBP")).thenReturn(List.of(item));
//
//    HttpHeaders headers = new HttpHeaders();
//    // Controller reads this header
//    headers.set("authorisation", "test-token");
//    // Adding standard header is harmless if any filter looks for it
//    headers.set("Authorization", "Bearer test-token");
//
//    HttpEntity<?> entity = new HttpEntity<>(headers);
//
//    ResponseEntity<List<CoinGeckoResponseModel>> response =
//        restTemplate.exchange(
//            url,
//            HttpMethod.GET,
//            entity,
//            new ParameterizedTypeReference<List<CoinGeckoResponseModel>>() {});
//
//    assertEquals(200, response.getStatusCode().value());
//    Assertions.assertNotNull(response.getBody());
//    assertEquals(1, response.getBody().size());
//  }
// }
