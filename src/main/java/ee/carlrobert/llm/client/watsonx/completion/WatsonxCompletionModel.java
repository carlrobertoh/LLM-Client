package ee.carlrobert.llm.client.watsonx.completion;

import ee.carlrobert.llm.completion.CompletionModel;
import java.util.Arrays;

public enum WatsonxCompletionModel implements CompletionModel {

  GRANITE_3B_CODE_INSTRUCT("ibm/granite-3b-code-instruct", "IBM Granite 3B Code Instruct", 8192),
  GRANITE_8B_CODE_INSTRUCT("ibm/granite-8b-code-instruct", "IBM Granite 8B Code Instruct", 8192),
  GRANITE_20B_CODE_INSTRUCT("ibm/granite-20b-code-instruct", "IBM Granite 20B Code Instruct", 8192),
  GRANITE_34B_CODE_INSTRUCT("ibm/granite-34b-code-instruct", "IBM Granite 34B Code Instruct", 8192),
  CODELLAMA_34_B_INSTRUCT("codellama/codellama-34b-instruct-hf", "Code Llama 34B Instruct", 8192),
  MIXTRAL_8_7B("mistralai/mixtral-8x7b-instruct-v01", "Mixtral (8x7B)", 32768),
  MIXTRAL_LARGE("mistralai/mistral-large", "Mistral Large", 128000),
  LLAMA_3_1_70B("meta-llama/llama-3-1-70b-instruct", "Llama 3.1 Instruct (70B)", 128000),
  LLAMA_3_1_8B("meta-llama/llama-3-1-8b-instruct", "Llama 3.1 Instruct (8B)", 128000),
  LLAMA_2_7B("meta-llama/llama-2-70b-chat", "Llama 2 Chat (70B)", 4096),
  LLAMA_2_13B("meta-llama/llama-2-13b-chat", "Llama 2 Chat (13B)", 4096),
  GRANITE_13B_INSTRUCT_V2("ibm/granite-13b-instruct-v2", "IBM Granite 13B Instruct V2", 8192),
  GRANITE_13B_CHAT_V2("ibm/granite-13b-chat-v2", "IBM Granite 13B Chat V2", 8192),
  GRANITE_20B_MULTILINGUAL("ibm/granite-20b-multilingual", "IBM Granite 20B Multilingual", 8192);

  private final String code;
  private final String description;
  private final int maxTokens;

  WatsonxCompletionModel(String code, String description, int maxTokens) {
    this.code = code;
    this.description = description;
    this.maxTokens = maxTokens;
  }

  public static WatsonxCompletionModel findByCode(String code) {
    return Arrays.stream(WatsonxCompletionModel.values())
        .filter(item -> item.getCode().equals(code))
        .findFirst().orElseThrow();
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public int getMaxTokens() {
    return maxTokens;
  }

  @Override
  public String toString() {
    return description;
  }
}

