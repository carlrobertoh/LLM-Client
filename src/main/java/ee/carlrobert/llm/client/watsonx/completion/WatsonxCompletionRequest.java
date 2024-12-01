package ee.carlrobert.llm.client.watsonx.completion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ee.carlrobert.llm.completion.CompletionRequest;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WatsonxCompletionRequest implements CompletionRequest {

  String input;
  @JsonProperty("project_id")
  String projectId;
  @JsonProperty("space_id")
  String spaceId;
  @JsonProperty("model_id")
  String modelId;
  String deploymentId;
  Boolean stream;
  WatsonxCompletionParameters parameters;

  public WatsonxCompletionRequest(Builder builder) {
    System.out.println("Model ID: " + builder.modelId);
    System.out.println("Deployment ID: " + builder.deploymentId);
    System.out.println("decodingMethod: " + builder.decodingMethod);
    System.out.println("maxNewTokens: " + builder.maxNewTokens);
    System.out.println("minNewTokens: " + builder.minNewTokens);
    System.out.println("randomSeed: " + builder.randomSeed);
    System.out.println("stopSequences: " + builder.stopSequences);
    System.out.println("timeLimit: " + builder.timeLimit);
    System.out.println("topK: " + builder.topK);
    System.out.println("topP: " + builder.topP);
    System.out.println("temperature: " + builder.temperature);
    System.out.println("rep penalty: " + builder.repetitionPenalty);
    System.out.println("include stop seq: " + builder.includeStopSequence);
    this.input = builder.input;
    this.stream = builder.stream;
    this.projectId = builder.projectId;
    this.spaceId = builder.spaceId;
    this.modelId = builder.modelId;
    this.deploymentId = builder.deploymentId;
    this.parameters = new WatsonxCompletionParameters(
        builder.decodingMethod,
        builder.maxNewTokens,
        builder.minNewTokens,
        builder.randomSeed,
        builder.stopSequences,
        builder.timeLimit,
        builder.topK,
        builder.topP,
        builder.temperature,
        builder.repetitionPenalty,
        builder.includeStopSequence);
  }

  public Boolean getStream() {
    return this.stream;
  }

  public String getModelId() {
    return modelId;
  }

  public String getDeploymentId() {
    return deploymentId;
  }

  public String getSpaceId() {
    return spaceId;
  }

  public String getProjectId() {
    return projectId;
  }

  public String getInput() {
    return input;
  }

  public WatsonxCompletionParameters getParameters() {
    return parameters;
  }

  public static class Builder {

    String input;
    String projectId;
    String spaceId;
    String modelId;
    String deploymentId;
    Boolean stream;
    String decodingMethod;
    Integer maxNewTokens;
    Integer minNewTokens;
    Integer randomSeed;
    String[] stopSequences;
    Integer timeLimit;
    Integer topK;
    Double topP;
    Double repetitionPenalty;
    Boolean includeStopSequence;
    Double temperature;

    public Builder(String prompt) {
      this.input = prompt;
    }

    public Builder setInput(String input) {
      this.input = input;
      return this;
    }

    public Builder setModelId(String modelId) {
      this.modelId = modelId;
      return this;
    }

    public Builder setDeploymentId(String deploymentId) {
      this.deploymentId = deploymentId;
      return this;
    }

    public Builder setSpaceId(String spaceId) {
      this.spaceId = spaceId;
      return this;
    }

    public Builder setProjectId(String projectId) {
      this.projectId = projectId;
      return this;
    }

    public Builder setStream(Boolean stream) {
      this.stream = stream;
      return this;
    }

    public Builder setMaxNewTokens(Integer maxNewTokens) {
      this.maxNewTokens = maxNewTokens;
      return this;
    }

    public Builder setMinNewTokens(Integer minNewTokens) {
      this.minNewTokens = minNewTokens;
      return this;
    }

    public Builder setTemperature(Double temperature) {
      this.temperature = temperature;
      return this;
    }

    public Builder setRepetitionPenalty(Double frequencyPenalty) {
      this.repetitionPenalty = frequencyPenalty;
      return this;
    }

    public Builder setDecodingMethod(String decodingMethod) {
      this.decodingMethod = decodingMethod;
      return this;
    }

    public Builder setStopSequences(String[] stopSequences) {
      this.stopSequences = stopSequences;
      return this;
    }

    public Builder setIncludeStopSequence(Boolean includeStopSequence) {
      this.includeStopSequence = includeStopSequence;
      return this;
    }

    public Builder setRandomSeed(Integer randomSeed) {
      this.randomSeed = randomSeed;
      return this;
    }

    public Builder setTopP(Double topP) {
      this.topP = topP;
      return this;
    }

    public Builder setTopK(Integer topK) {
      this.topK = topK;
      return this;
    }

    public WatsonxCompletionRequest build() {
      return new WatsonxCompletionRequest(this);
    }
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public class WatsonxCompletionParameters {

    @JsonProperty("decoding_method")
    String decodingMethod;
    @JsonProperty("max_new_tokens")
    Integer maxNewTokens;
    @JsonProperty("min_new_tokens")
    Integer minNewTokens;
    @JsonProperty("random_seed")
    Integer randomSeed;
    @JsonProperty("stop_sequences")
    String[] stopSequences;
    @JsonProperty("time_limit")
    Integer timeLimit;
    @JsonProperty("top_k")
    Integer topK;
    @JsonProperty("top_p")
    Double topP;
    Double temperature;
    @JsonProperty("repetition_penalty")
    Double repetitionPenalty;
    @JsonProperty("include_stop_sequence")
    Boolean includeStopSequence;

    public WatsonxCompletionParameters(
        String decodingMethod,
        Integer maxNewTokens,
        Integer minNewTokens,
        Integer randomSeed,
        String[] stopSequences,
        Integer timeLimit,
        Integer topK,
        Double topP,
        Double temperature,
        Double repetitionPenalty,
        Boolean includeStopSequence) {
      this.decodingMethod = decodingMethod;
      this.maxNewTokens = maxNewTokens;
      this.minNewTokens = minNewTokens;
      this.randomSeed = randomSeed;
      this.stopSequences = stopSequences;
      this.timeLimit = timeLimit;
      this.topK = topK;
      this.topP = topP;
      this.temperature = temperature;
      this.repetitionPenalty = repetitionPenalty;
      this.includeStopSequence = includeStopSequence;
    }
  }
}
