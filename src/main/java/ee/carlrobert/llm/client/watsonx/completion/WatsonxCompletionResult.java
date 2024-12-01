package ee.carlrobert.llm.client.watsonx.completion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WatsonxCompletionResult {

  String generatedText;
  String stopReason;
  int generatedTokenCount;
  int inputTokenCount;
  int seed;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public WatsonxCompletionResult(
      @JsonProperty("generated_text") String generatedText,
      @JsonProperty("stop_reason") String stopReason,
      @JsonProperty("generated_token_count") int generatedTokenCount,
      @JsonProperty("input_token_count") int inputTokenCount,
      @JsonProperty("seed") int seed) {
    this.generatedText = generatedText;
    this.stopReason = stopReason;
    this.generatedTokenCount = generatedTokenCount;
    this.inputTokenCount = inputTokenCount;
    this.seed = seed;
  }

  public String getGeneratedText() {
    return this.generatedText;
  }
}