package uk.casey.cryptodash;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MarketController.class)
public class MarketControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getTopMarketsList_returnsOk() throws Exception {
    mockMvc
        .perform(
            get("/api/markets/top")
                .param("limit", "10")
                .param("fiat", "GBP")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorisation", "Bearer token"))
        .andExpect(status().isOk());
  }

  @Test
  void getTopMarketsList_returnsNotFound() throws Exception {
    mockMvc
        .perform(
            get("/api/markets/66")
                .param("limit", "10")
                .param("fiat", "GBP")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorisation", "Bearer token"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getMarketItem_returnsOk() throws Exception {
    mockMvc
        .perform(
            get("/api/markets/item")
                .param("assetId", "bitcoin")
                .param("fiat", "GBP")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorisation", "Bearer token"))
        .andExpect(status().isOk());
  }

  @Test
  void getMarketItem_returnsNotFound() throws Exception {
    mockMvc
        .perform(
            get("/api/markets/itemInBaggingArea")
                .param("assetId", "bitcoin")
                .param("fiat", "GBP")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorisation", "Bearer token"))
        .andExpect(status().isNotFound());
  }
}
