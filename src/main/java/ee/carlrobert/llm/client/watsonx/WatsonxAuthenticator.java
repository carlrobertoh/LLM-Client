package ee.carlrobert.llm.client.watsonx;

import static ee.carlrobert.llm.client.DeserializationUtil.OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WatsonxAuthenticator {

  IBMAuthBearerToken bearerToken;
  OkHttpClient client;
  Request request;
  Request expiryRequest;
  Boolean isZenApiKey = false;

  // Watsonx SaaS
  public WatsonxAuthenticator(String apiKey) {
    this.client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    RequestBody body = RequestBody.create(mediaType,
        "grant_type=urn:ibm:params:oauth:grant-type:apikey&apikey=" + apiKey);
    this.request = new Request.Builder()
        .url("https://iam.cloud.ibm.com/identity/token")
        .method("POST", body)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .build();
    try {
      Response response = client.newCall(request).execute();
      this.bearerToken = OBJECT_MAPPER.readValue(response.body().string(),
          IBMAuthBearerToken.class);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // Zen API Key
  public WatsonxAuthenticator(String username, String zenApiKey) {
    IBMAuthBearerToken token = new IBMAuthBearerToken();
    String tokenStr = Base64.getEncoder().encode((username + ":" + zenApiKey).getBytes())
        .toString();
    token.setAccessToken(tokenStr);
    this.bearerToken = token;
    this.isZenApiKey = true;
  }

  // Watsonx API Key
  public WatsonxAuthenticator(String username, String apiKey,
      String host) { // TODO add support for password
    this.client = new OkHttpClient().newBuilder()
        .build();
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> authParams = new LinkedHashMap<>();
    authParams.put("username", username);
    authParams.put("api_key", apiKey);

    String authParamsStr = "";
    try {
      authParamsStr = mapper.writeValueAsString(authParams);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, authParamsStr);
    // TODO add support for IAM endpoint v1/auth/identitytoken
    this.request = new Request.Builder()
        .url(host
            + "/icp4d-api/v1/authorize")
        .method("POST", body)
        .addHeader("Content-Type", "application/json")
        .build();

    this.expiryRequest = new Request.Builder()
        .url(host + "/usermgmt/v1/user/tokenExpiry")
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", "Bearer " + this.bearerToken.getAccessToken())
        .build();

    try {
      Response response = client.newCall(request).execute();
      this.bearerToken = OBJECT_MAPPER.readValue(response.body().string(),
          IBMAuthBearerToken.class);

      Response expiry = client.newCall(request).execute();
      this.bearerToken.setExpiration(
          OBJECT_MAPPER.readValue(expiry.body().string(), IBMAuthTokenExpiry.class)
              .getExpiry());

    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void generateNewBearerToken() {
    try {
      Response response = client.newCall(request).execute();
      this.bearerToken = OBJECT_MAPPER.readValue(response.body().string(),
          IBMAuthBearerToken.class);
      if (this.bearerToken.getExpiration() == null) {
        Response expiry = client.newCall(expiryRequest).execute();
        this.bearerToken.setExpiration(
            OBJECT_MAPPER.readValue(expiry.body().string(), IBMAuthTokenExpiry.class)
                .getExpiry());
      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public String getBearerTokenValue() {
    if (!isZenApiKey && (this.bearerToken == null || (this.bearerToken.getExpiration() * 1000)
        < (new Date().getTime() + 60000))) {
      generateNewBearerToken();
    }
    return this.bearerToken.getAccessToken();
  }
}
