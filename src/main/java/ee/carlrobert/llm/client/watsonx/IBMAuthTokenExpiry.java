package ee.carlrobert.llm.client.watsonx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IBMAuthTokenExpiry {

  @JsonProperty("exp")
  Integer expiry;

  Integer getExpiry() {
    return this.expiry;
  }

  public void setExpiry(int expiry) {
    this.expiry = expiry;
  }
}