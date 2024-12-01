package ee.carlrobert.llm.client.watsonx.completion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WatsonxCompletionStreamResponse {

  private final String modelId;
  private List<WatsonxCompletionResponse> items;
  private String createdAt;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public WatsonxCompletionStreamResponse(
      @JsonProperty("model_id") String modelId,
      @JsonProperty("created_at") String createdAt,
      @JsonProperty("items") List<WatsonxCompletionResponse> items) {
    this.modelId = modelId;
    this.createdAt = createdAt;
    this.items = items;
  }

  public String getModelId() {
    WatsonxCompletionResponse firstItem = items.get(0);
    return firstItem.getModelId();
  }

  public String getCreatedAt() {
    WatsonxCompletionResponse firstItem = items.get(0);
    return firstItem.getCreatedAt();
  }

  public List<WatsonxCompletionResponse> getItems() {
    return items;
  }
}


