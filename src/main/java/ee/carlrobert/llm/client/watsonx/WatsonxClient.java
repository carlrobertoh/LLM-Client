package ee.carlrobert.llm.client.watsonx;

import static ee.carlrobert.llm.client.DeserializationUtil.OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import ee.carlrobert.llm.PropertiesLoader;
import ee.carlrobert.llm.client.DeserializationUtil;
import ee.carlrobert.llm.client.openai.completion.ErrorDetails;
import ee.carlrobert.llm.client.watsonx.completion.WatsonxCompletionRequest;
import ee.carlrobert.llm.client.watsonx.completion.WatsonxCompletionResponse;
import ee.carlrobert.llm.client.watsonx.completion.WatsonxCompletionResponseError;
import ee.carlrobert.llm.completion.CompletionEventListener;
import ee.carlrobert.llm.completion.CompletionEventSourceListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

public class WatsonxClient {

  private static final MediaType APPLICATION_JSON = MediaType.parse("application/json");
  private final OkHttpClient httpClient;
  private final String host;
  private final String apiVersion;
  private final WatsonxAuthenticator authenticator;

  private WatsonxClient(Builder builder, OkHttpClient.Builder httpClientBuilder) {
    this.httpClient = httpClientBuilder.build();
    this.apiVersion = builder.apiVersion;
    this.host = builder.host;
    if (builder.isOnPrem) {
      if (builder.isZenApiKey) {
        this.authenticator = new WatsonxAuthenticator(builder.username, builder.apiKey);
      } else {
        this.authenticator = new WatsonxAuthenticator(builder.username, builder.apiKey,
            builder.host);
      }
    } else {
      this.authenticator = new WatsonxAuthenticator(builder.apiKey);
    }
  }

  public EventSource getCompletionAsync(
      WatsonxCompletionRequest request,
      CompletionEventListener<String> eventListener) {
    return EventSources.createFactory(httpClient).newEventSource(
        buildCompletionRequest(request),
        getCompletionEventSourceListener(eventListener));
  }

  public WatsonxCompletionResponse getCompletion(WatsonxCompletionRequest request) {
    try (var response = httpClient.newCall(buildCompletionRequest(request)).execute()) {
      return DeserializationUtil.mapResponse(response, WatsonxCompletionResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected Request buildCompletionRequest(WatsonxCompletionRequest request) {
    var headers = new HashMap<>(getRequiredHeaders());
    if (request.getStream()) {
      headers.put("Accept", "text/event-stream");
    }
    try {
      String path =
          (request.getDeploymentId() == null || request.getDeploymentId().isEmpty()) ? "text/"
              : "deployments/" + request.getDeploymentId() + "/";
      String generation = request.getStream() ? "generation_stream" : "generation";
      return new Request.Builder()
          .url(host + "/ml/v1/" + path + generation + "?version=" + apiVersion)
          .headers(Headers.of(headers))
          .post(RequestBody.create(OBJECT_MAPPER.writeValueAsString(request), APPLICATION_JSON))
          .build();
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Unable to process request", e);
    }
  }

  private Map<String, String> getRequiredHeaders() {
    return new HashMap<>(Map.of("Authorization",
        (this.authenticator.isZenApiKey ? "ZenApiKey " : "Bearer ")
            + authenticator.getBearerTokenValue()));
  }

  private CompletionEventSourceListener<String> getCompletionEventSourceListener(
      CompletionEventListener<String> eventListener) {
    return new CompletionEventSourceListener<>(eventListener) {
      @Override
      protected String getMessage(String data) {
        try {
          return OBJECT_MAPPER.readValue(data, WatsonxCompletionResponse.class)
              .getResults().get(0).getGeneratedText();
        } catch (Exception e) {
          try {
            String message = OBJECT_MAPPER.readValue(data, WatsonxCompletionResponseError.class)
                .getError()
                .getMessage();
            return message == null ? "" : message;
          } catch (Exception ex) {
            System.out.println(ex);
            return "";
          }
        }
      }

      @Override
      protected ErrorDetails getErrorDetails(String error) {
        try {
          return OBJECT_MAPPER.readValue(error, WatsonxCompletionResponseError.class).getError();
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  public static class Builder {

    private final String apiKey;
    private String host = PropertiesLoader.getValue("watsonx.baseUrl");
    private String apiVersion = "2024-03-14";
    private Boolean isOnPrem;
    private Boolean isZenApiKey;
    private String username;

    public Builder(String apiKey) {
      this.apiKey = apiKey;
    }

    public Builder setApiVersion(String apiVersion) {
      this.apiVersion = apiVersion;
      return this;
    }

    public Builder setHost(String host) {
      this.host = host;
      return this;
    }

    public Builder setIsZenApiKey(Boolean isZenApiKey) {
      this.isZenApiKey = isZenApiKey;
      return this;
    }

    public Builder setIsOnPrem(Boolean isOnPrem) {
      this.isOnPrem = isOnPrem;
      return this;
    }

    public Builder setUsername(String username) {
      this.username = username;
      return this;
    }

    public WatsonxClient build(OkHttpClient.Builder builder) {
      return new WatsonxClient(this, builder);
    }

    public WatsonxClient build() {
      return build(new OkHttpClient.Builder());
    }
  }
}






