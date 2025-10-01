package uk.casey.netWorth.models;

public class LoginRequestModel {

  private String username;
  private String password;

  public LoginRequestModel(String username, String passcode) {
    this.username = username;
    this.password = password;
  }

  public LoginRequestModel() {}

  public boolean isValid() {
    return isNotBlank(username) && isNotBlank(password);
  }

  private boolean isNotBlank(String item) {
    return item != null && !item.trim().isEmpty();
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
