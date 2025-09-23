package uk.casey.cryptodash.models;

import java.util.ArrayList;

public class MarketListResponseModel {

  private ArrayList<MarketListResponseModel> marketListResponseModels;

  public MarketListResponseModel() {}

  public MarketListResponseModel(ArrayList<MarketListResponseModel> marketListResponseModels) {
    this.marketListResponseModels = marketListResponseModels;
  }

  public ArrayList<MarketListResponseModel> getMarketListResponseModels() {
    return marketListResponseModels;
  }

  public void setMarketListResponseModels(
      ArrayList<MarketListResponseModel> marketListResponseModels) {
    this.marketListResponseModels = marketListResponseModels;
  }
}
