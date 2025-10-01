package uk.casey.netWorth;

import static io.restassured.RestAssured.given;

import java.util.UUID;
import org.junit.jupiter.api.Test;

public class WatchListEndpoints {

  private String generateValidTestToken() {
    return tokenUtil.generateToken(UUID.fromString("11111111-1111-1111-1111-111111111111"), "dave");
  }

  @Test
  public void testGetWatchList() {
    given()
        .header("userId", "11111111-1111-1111-1111-111111111111")
        .header("authorisation", generateValidTestToken())
        .header("content-Type", "application/json")
        .when()
        .get("http://localhost:8080/api/user/watchlist?fiat=GBP")
        .then()
        .statusCode(200);
  }

  @Test
  public void testPostWatchListItem() {
    given()
        .header("userId", "11111111-1111-1111-1111-111111111111")
        .header("authorisation", generateValidTestToken())
        .header("content-Type", "application/json")
        .body(
            "{\n"
                + "    \"type\": \"investment\",\n"
                + "    \"provider\": \"coinbase\",\n"
                + "    \"category\": \"chillin\",\n"
                + "    \"value\": 125.78\n"
                + "}")
        .when()
        .post("http://localhost:8080/api/user/watchlist?name=bitcoin")
        .then()
        .statusCode(200);
  }

  @Test
  public void testDeleteWatchListItem() {
    given()
        .header("userId", "11111111-1111-1111-1111-111111111111")
        .header("authorisation", generateValidTestToken())
        .header("content-Type", "application/json")
        .when()
        .delete("http://localhost:8080/api/user/watchlist?assetId=2")
        .then()
        .statusCode(200);
  }

  @Test
  public void testPatchWatchListItemValue() {
    given()
        .header("userId", "11111111-1111-1111-1111-111111111111")
        .header("authorisation", generateValidTestToken())
        .header("content-Type", "application/json")
        .body("{ \n" + "    \"value\": 15.99\n" + "}")
        .when()
        .patch("http://localhost:8080/api/user/watchlist?assetId=2")
        .then()
        .statusCode(200);
  }
}
