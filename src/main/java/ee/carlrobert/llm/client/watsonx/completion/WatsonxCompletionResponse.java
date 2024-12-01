package ee.carlrobert.llm.client.watsonx.completion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ee.carlrobert.llm.completion.CompletionResponse;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WatsonxCompletionResponse implements CompletionResponse {

  private final String modelId;
  private String createdAt;
  private List<WatsonxCompletionResult> results;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public WatsonxCompletionResponse(
      @JsonProperty("model_id") String modelId,
      @JsonProperty("created_at") String createdAt,
      @JsonProperty("results") List<WatsonxCompletionResult> results) {
    this.modelId = modelId;
    this.createdAt = createdAt;
    this.results = results;
  }

  public String getModelId() {
    return modelId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public List<WatsonxCompletionResult> getResults() {
    return results;
  }
}


