package uk.casey.cryptodash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import uk.casey.cryptodash.models.MarketListResponseModel;

@Configuration
public class SpringConfiguration {

  @Bean
  public MarketListResponseModel marketListResponseModel() {
    return new MarketListResponseModel();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
