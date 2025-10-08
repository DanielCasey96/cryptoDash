package uk.casey.netWorth;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

public class UserEndpoints {

  @Test
  void registerUser() {
    given()
        .header("Content-Type", "application/json")
        .body(
            "{\n"
                + "    \"username\" : \"testuser\",\n"
                + "    \"passcode\" : \"testpassword\",\n"
                + "    \"email\" : \"testuser@example.com\"\n"
                + "}")
        .post("/api/user/register")
        .then()
        .statusCode(200);
  }

  @Test
  void authoriseUser() {
    given()
        .header("Content-Type", "application/json")
        .header("userId", "11111111-1111-1111-1111-111111111111")
        .body(
            "{\n"
                + "    \"username\" : \"testuser\",\n"
                + "    \"passcode\" : \"testpassword\"\n"
                + "}")
        .then()
        .statusCode(200);
  }
}
