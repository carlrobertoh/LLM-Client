package ee.carlrobert.llm.client.openai.completion;

import com.fasterxml.jackson.annotation.JsonProperty;
import ee.carlrobert.llm.completion.CompletionRequest;

public abstract class OpenAICompletionRequest implements CompletionRequest {

  @JsonProperty("max_tokens")
  private final int maxTokens;
  private final double temperature;
  @JsonProperty("frequency_penalty")
  private final double frequencyPenalty;
  @JsonProperty("presence_penalty")
  private final double presencePenalty;
  private final boolean stream;

  protected OpenAICompletionRequest(Builder builder) {
    this.maxTokens = builder.maxTokens;
    this.temperature = builder.temperature;
    this.frequencyPenalty = builder.frequencyPenalty;
    this.presencePenalty = builder.presencePenalty;
    this.stream = builder.stream;
  }

  public int getMaxTokens() {
    return maxTokens;
  }

  public double getTemperature() {
    return temperature;
  }

  public double getFrequencyPenalty() {
    return frequencyPenalty;
  }

  public double getPresencePenalty() {
    return presencePenalty;
  }

  public boolean isStream() {
    return stream;
  }

  public abstract static class Builder {

    private int maxTokens = 1000;
    private double temperature = 0.9;
    private double frequencyPenalty = 0;
    private double presencePenalty = 0.6;
    private boolean stream = true;

    public Builder setMaxTokens(int maxTokens) {
      this.maxTokens = maxTokens;
      return this;
    }

    public Builder setTemperature(double temperature) {
      this.temperature = temperature;
      return this;
    }

    public Builder setFrequencyPenalty(double frequencyPenalty) {
      this.frequencyPenalty = frequencyPenalty;
      return this;
    }

    public Builder setPresencePenalty(double presencePenalty) {
      this.presencePenalty = presencePenalty;
      return this;
    }

    public Builder setStream(boolean stream) {
      this.stream = stream;
      return this;
    }

    public abstract OpenAICompletionRequest build();
  }
}