package ee.carlrobert.llm.client.watsonx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IBMAuthBearerToken {

  @JsonProperty("token")
  String token;
  @JsonProperty("access_token")
  String accessToken;
  @JsonProperty("expiration")
  Integer expiration;

  String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  String getAccessToken() {
    return this.accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  Integer getExpiration() {
    return this.expiration;
  }

  public void setExpiration(int expiration) {
    this.expiration = expiration;
  }
}