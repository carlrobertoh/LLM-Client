package ee.carlrobert.llm.client.azure;

public class AzureCompletionRequestParams {

  private final String resourceName;
  private final String deploymentId;
  private final String apiVersion;


  public AzureCompletionRequestParams(
      String resourceName,
      String completionDeploymentId,
      String apiVersion) {
    this.resourceName = resourceName;
    this.deploymentId = completionDeploymentId;
    this.apiVersion = apiVersion;
  }

  public String getResourceName() {
    return resourceName;
  }

  public String getDeploymentId() {
    return deploymentId;
  }

  public String getApiVersion() {
    return apiVersion;
  }
}
