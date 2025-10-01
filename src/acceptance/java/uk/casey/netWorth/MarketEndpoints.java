package uk.casey.netWorth;

import static io.restassured.RestAssured.given;

import java.util.UUID;
import org.junit.jupiter.api.Test;

public class MarketEndpoints {

  private String generateValidTestToken() {
    return tokenUtil.generateToken(UUID.fromString("11111111-1111-1111-1111-111111111111"), "dave");
  }

  @Test
  // Currently runs to live API Coin Gecko
  public void testGetTop10() {

    given()
        .header("authorisation", generateValidTestToken())
        .get("http://localhost:8080/api/markets/top")
        .then()
        .statusCode(200);
  }

  @Test
  // Currently runs to live API Coin Gecko
  public void testGetItem() {
    given()
        .header("authorisation", generateValidTestToken())
        .get("http://localhost:8080/api/markets/item?assetId=bitcoin&fiat=GBP")
        .then()
        .statusCode(200);
  }
}
