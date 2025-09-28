package uk.casey.cryptodash.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class WatchListItemModel {

  private String id;
  private String name;
  private String type;
  private String provider;
  private String category;
  private BigDecimal value;
  private Timestamp updated_at;

  public WatchListItemModel(
      String id,
      String name,
      String type,
      String provider,
      String category,
      BigDecimal value,
      Timestamp updated_at) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.provider = provider;
    this.category = category;
    this.value = value;
    this.updated_at = updated_at;
  }

  public WatchListItemModel() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public Timestamp getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(Timestamp updated_at) {
    this.updated_at = updated_at;
  }
}
