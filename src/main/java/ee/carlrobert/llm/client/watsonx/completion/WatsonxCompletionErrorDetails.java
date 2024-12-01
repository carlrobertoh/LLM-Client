package ee.carlrobert.llm.client.watsonx.completion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ee.carlrobert.llm.client.openai.completion.ErrorDetails;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WatsonxCompletionErrorDetails {

  private static final String DEFAULT_ERROR_MSG = "Something went wrong. Please try again later.";
  public static WatsonxCompletionErrorDetails DEFAULT_ERROR = new WatsonxCompletionErrorDetails(
      DEFAULT_ERROR_MSG, null);
  String code;
  String message;
  ErrorDetails details;


  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public WatsonxCompletionErrorDetails(
      @JsonProperty("message") String message,
      @JsonProperty("code") String code) {
    this.message = message;
    this.code = code;
    this.details = new ErrorDetails(message, null, null, code);
  }

  public String getMessage() {
    return message;
  }

  public String getCode() {
    return code;
  }

  public ErrorDetails getDetails() {
    return details;
  }

}
