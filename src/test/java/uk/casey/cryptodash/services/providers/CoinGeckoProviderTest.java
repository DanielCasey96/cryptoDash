package uk.casey.cryptodash.services.providers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.params.provider.MethodSource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.casey.cryptodash.models.MarketListResponseModel;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class CoinGeckoProviderTest {

  @Mock private RestTemplate restTemplate;

  @InjectMocks private CoinGeckoProvider coinGeckoProvider;

  @Test
  void getMarketList_returnsSuccess() throws Exception {
    MarketListResponseModel mockResponse = new MarketListResponseModel();
    when(restTemplate.getForObject(anyString(), eq(MarketListResponseModel.class)))
        .thenReturn(mockResponse);

    MarketListResponseModel result = coinGeckoProvider.getMarketList("GBP");
    assertNotNull(result);
    verify(restTemplate).getForObject(anyString(), eq(MarketListResponseModel.class));
  }

  @Test
  void getMarketList_returnsError() throws Exception {
    when(restTemplate.getForObject(anyString(), eq(MarketListResponseModel.class)))
        .thenThrow(new RuntimeException("API error"));

    assertThrows(RuntimeException.class, () -> coinGeckoProvider.getMarketList("GBP"));
  }

  @ParameterizedTest
  @MethodSource("errorProvider")
  void getMarketsList_handlesHttpErrorCodes(HttpClientErrorException exception)  throws Exception {
    when(restTemplate.getForObject(anyString(), eq(MarketListResponseModel.class)))
        .thenThrow(exception);

    assertThrows(exception.getClass(), () -> coinGeckoProvider.getMarketList("GBP"));
  }

  private static Stream<Arguments> errorProvider() {
    return Stream.of(
        Arguments.of(HttpClientErrorException.create(
            org.springframework.http.HttpStatus.NOT_FOUND,
            "404 Not found",
            HttpHeaders.EMPTY,
            null,
            StandardCharsets.UTF_8
        )),
        Arguments.of(HttpClientErrorException.create(
            org.springframework.http.HttpStatus.BAD_REQUEST,
            "400 Bad request",
            HttpHeaders.EMPTY,
            null,
            StandardCharsets.UTF_8
        )),
        Arguments.of(HttpClientErrorException.create(
            org.springframework.http.HttpStatus.FORBIDDEN,
            "403 Forbidden",
            HttpHeaders.EMPTY,
            null,
            StandardCharsets.UTF_8
        ))
    );
  }

}
