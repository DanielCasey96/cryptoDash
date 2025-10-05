package uk.casey.netWorth.services.providers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.casey.netWorth.config.CoinGeckoProperties;
import uk.casey.netWorth.models.CoinGeckoResponseModel;

@ExtendWith(MockitoExtension.class)
public class CoinGeckoProviderTest {

  @Mock private RestTemplate restTemplate;

  @Mock private CoinGeckoProperties coinGeckoProperties;

  @InjectMocks private CoinGeckoProvider coinGeckoProvider;

  @Test
  void getMarketList_returnsSuccess() throws Exception {
    CoinGeckoResponseModel[] mockResponse = {new CoinGeckoResponseModel()};
    when(restTemplate.getForObject(anyString(), eq(CoinGeckoResponseModel[].class)))
        .thenReturn(new CoinGeckoResponseModel[] {new CoinGeckoResponseModel()});

    List<CoinGeckoResponseModel> result = coinGeckoProvider.getMarketList("GBP");
    assertNotNull(result);
    verify(restTemplate).getForObject(anyString(), eq(CoinGeckoResponseModel[].class));
  }

  @Test
  void getMarketList_returnsError() throws Exception {
    when(restTemplate.getForObject(anyString(), eq(CoinGeckoResponseModel[].class)))
        .thenThrow(new RuntimeException("API error"));

    assertThrows(RuntimeException.class, () -> coinGeckoProvider.getMarketList("GBP"));
  }

  @ParameterizedTest
  @MethodSource("errorProvider")
  void getMarketsList_handlesHttpErrorCodes(HttpClientErrorException exception) throws Exception {
    when(restTemplate.getForObject(anyString(), eq(CoinGeckoResponseModel[].class)))
        .thenThrow(exception);

    assertThrows(exception.getClass(), () -> coinGeckoProvider.getMarketList("GBP"));
  }

  @Test
  void getItem_returnsSuccess() throws Exception {
    CoinGeckoResponseModel[] mockResponse = {new CoinGeckoResponseModel()};
    when(restTemplate.getForObject(anyString(), eq(CoinGeckoResponseModel[].class)))
        .thenReturn(new CoinGeckoResponseModel[] {new CoinGeckoResponseModel()});

    CoinGeckoResponseModel result = coinGeckoProvider.getItem("bitcoin", "GBP");
    assertNotNull(result);
    verify(restTemplate).getForObject(anyString(), eq(CoinGeckoResponseModel[].class));
  }

  @Test
  void getItem_returnsError() throws Exception {
    when(restTemplate.getForObject(anyString(), eq(CoinGeckoResponseModel[].class)))
        .thenThrow(new RuntimeException("API error"));

    assertThrows(RuntimeException.class, () -> coinGeckoProvider.getItem("bitcoin", "GBP"));
  }

  @ParameterizedTest
  @MethodSource("errorProvider")
  void getItem_handlesHttpErrorCodes(HttpClientErrorException exception) throws Exception {
    when(restTemplate.getForObject(anyString(), eq(CoinGeckoResponseModel[].class)))
        .thenThrow(exception);

    assertThrows(exception.getClass(), () -> coinGeckoProvider.getItem("bitcoin", "GBP"));
  }

  private static Stream<Arguments> errorProvider() {
    return Stream.of(
        Arguments.of(
            HttpClientErrorException.create(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "404 Not found",
                HttpHeaders.EMPTY,
                null,
                StandardCharsets.UTF_8)),
        Arguments.of(
            HttpClientErrorException.create(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "400 Bad request",
                HttpHeaders.EMPTY,
                null,
                StandardCharsets.UTF_8)),
        Arguments.of(
            HttpClientErrorException.create(
                org.springframework.http.HttpStatus.FORBIDDEN,
                "403 Forbidden",
                HttpHeaders.EMPTY,
                null,
                StandardCharsets.UTF_8)));
  }
}
