package uk.casey.cryptodash.models;

public class WatchResponseModel {

  private String id;
  private String amount;

  public WatchResponseModel(String id, String amount) {
    this.id = id;
    this.amount = amount;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }
}
