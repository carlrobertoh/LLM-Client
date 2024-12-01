package ee.carlrobert.llm.client.watsonx.completion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ee.carlrobert.llm.client.openai.completion.ErrorDetails;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WatsonxCompletionResponseError {

  private final List<WatsonxCompletionErrorDetails> error;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public WatsonxCompletionResponseError(
      @JsonProperty("error") List<WatsonxCompletionErrorDetails> error) {
    this.error = error;
  }

  public ErrorDetails getError() {
    return (error == null || error.isEmpty()) ? new ErrorDetails("") : error.get(0).getDetails();
  }
}