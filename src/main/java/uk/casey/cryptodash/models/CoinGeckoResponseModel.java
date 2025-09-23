package uk.casey.cryptodash.models;

import java.math.BigDecimal;
import jdk.jfr.Timestamp;

public class CoinGeckoResponseModel {

  private String id;
  private String symbol;
  private String name;
  private BigDecimal current_price;
  private BigDecimal market_cap;
  private BigDecimal price_change_24h;
  private BigDecimal price_change_percentage_24h;
  private Timestamp last_updated;

  public CoinGeckoResponseModel(
      String id,
      String symbol,
      String name,
      BigDecimal current_price,
      BigDecimal price_change_24h,
      BigDecimal price_change_percentage_24h,
      Timestamp last_updated) {
    this.id = id;
    this.symbol = symbol;
    this.name = name;
    this.current_price = current_price;
    this.price_change_24h = price_change_24h;
    this.price_change_percentage_24h = price_change_percentage_24h;
    this.last_updated = last_updated;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getCurrent_price() {
    return current_price;
  }

  public void setCurrent_price(BigDecimal current_price) {
    this.current_price = current_price;
  }

  public BigDecimal getMarket_cap() {
    return market_cap;
  }

  public void setMarket_cap(BigDecimal market_cap) {
    this.market_cap = market_cap;
  }

  public BigDecimal getPrice_change_24h() {
    return price_change_24h;
  }

  public void setPrice_change_24h(BigDecimal price_change_24h) {
    this.price_change_24h = price_change_24h;
  }

  public BigDecimal getPrice_change_percentage_24h() {
    return price_change_percentage_24h;
  }

  public Timestamp getLast_updated() {
    return last_updated;
  }

  public void setLast_updated(Timestamp last_updated) {
    this.last_updated = last_updated;
  }
}
