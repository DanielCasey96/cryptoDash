package uk.casey.cryptodash;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

public class MarketEndpoints {

  @Test
  // Currently runs to live API Coin Gecko
  public void testGetTop10() {
    given().when().get("http://localhost:8080/api/markets/top").then().statusCode(200);
  }

  @Test
  // Currently runs to live API Coin Gecko
  public void testGetItem() {
    given()
        .when()
        .get("http://localhost:8080/api/markets/item?assetId=bitcoin&fiat=GBP")
        .then()
        .statusCode(200);
  }
}
