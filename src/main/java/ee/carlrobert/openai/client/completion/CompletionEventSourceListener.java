package ee.carlrobert.openai.client.completion;

import static java.lang.String.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.Consumer;
import okhttp3.Response;
import okhttp3.internal.http2.StreamResetException;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CompletionEventSourceListener extends EventSourceListener {

  private static final Logger LOG = LoggerFactory.getLogger(CompletionEventSourceListener.class);

  private final CompletionEventListener listeners;
  private final StringBuilder messageBuilder = new StringBuilder();
  private final boolean retryOnReadTimeout;
  private final Consumer<String> onRetry;

  public CompletionEventSourceListener(CompletionEventListener listeners, boolean retryOnReadTimeout, Consumer<String> onRetry) {
    this.listeners = listeners;
    this.retryOnReadTimeout = retryOnReadTimeout;
    this.onRetry = onRetry;
  }

  protected abstract String getMessage(String data) throws JsonProcessingException;

  protected abstract ErrorDetails getErrorDetails(String data) throws JsonProcessingException;

  public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
    LOG.info("Request opened.");
  }

  public void onClosed(@NotNull EventSource eventSource) {
    LOG.info("Request closed.");
    listeners.onComplete(messageBuilder);
  }

  public void onEvent(
      @NotNull EventSource eventSource,
      String id,
      String type,
      @NotNull String data) {
    try {
      // Redundant end signal so just ignore
      if ("[DONE]".equals(data)) {
        return;
      }

      var message = getMessage(data);
      if (message != null) {
        messageBuilder.append(message);
        listeners.onMessage(message);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Unable to deserialize payload.", e);
    }
  }

  public void onFailure(
      @NotNull EventSource eventSource,
      Throwable ex,
      Response response) {
    if (ex instanceof StreamResetException) {
      LOG.info("Stream was cancelled");
      listeners.onComplete(messageBuilder);
      return;
    }

    if (ex instanceof SocketTimeoutException) {
      if (retryOnReadTimeout) {
        LOG.info("Retrying request.");
        onRetry.accept(messageBuilder.toString());
        return;
      }

      listeners.onError(new ErrorDetails("Request timed out. This may be due to the server being overloaded."));
      return;
    }

    try {
      if (response == null) {
        throw new IOException(ex);
      }

      var body = response.body();
      if (body != null) {
        var jsonBody = body.string();
        try {
          var errorDetails = getErrorDetails(jsonBody);
          if (errorDetails == null ||
              errorDetails.getMessage() == null || errorDetails.getMessage().isEmpty()) {
            listeners.onError(toUnknownErrorResponse(response, jsonBody));
          } else {
            listeners.onError(errorDetails);
          }
        } catch (JsonProcessingException e) {
          LOG.error("Could not serialize error response", ex);
          listeners.onError(toUnknownErrorResponse(response, jsonBody));
        }
      }
    } catch (IOException e) {
      LOG.error("Something went wrong.", e);
      listeners.onError(ErrorDetails.DEFAULT_ERROR);
    }
  }

  private ErrorDetails toUnknownErrorResponse(Response response, String jsonBody) {
    return new ErrorDetails(format("Unknown API response. Code: %s, Body: %s", response.code(), jsonBody));
  }
}
