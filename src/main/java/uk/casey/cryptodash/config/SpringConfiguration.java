package uk.casey.cryptodash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.casey.cryptodash.models.MarketListResponseModel;

@Configuration
public class SpringConfiguration {

  @Bean
  public MarketListResponseModel marketListResponseModel() {
    return new MarketListResponseModel();
  }
}
