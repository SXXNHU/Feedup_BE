package ll25.feedup.GeneratedSns.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.*;

@Component
public class OpenAiChatClient {
    private final RestClient restClient;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public OpenAiChatClient(RestClient openAiRestClient) {
        this.restClient = openAiRestClient;
    }

    /** 기존: system + user 두 개만 */
    public LlmCaptionResult generateJson(String system, String user, Duration timeout) {
        Map<String, Object> payload = new HashMap<>();
        String model = "gpt-4o-mini";
        payload.put("model", model);
        payload.put("temperature", 0.7);
        payload.put("response_format", Map.of("type", "json_object"));
        payload.put("messages", List.of(
                Map.of("role", "system", "content", system),
                Map.of("role", "user", "content", user)
        ));
        return doRequest(payload, timeout);
    }

    /** 오버로드: messages 전체를 외부에서 구성 */
    public LlmCaptionResult generateJsonWithMessages(
            List<Map<String, String>> messages,
            Double temperature,
            Double topP,
            Duration timeout
    ) {
        Map<String, Object> payload = new HashMap<>();
        String model = "gpt-4o-mini"; // 필요 시 yml로 분리
        payload.put("model", model);
        if (temperature != null) payload.put("temperature", temperature);
        if (topP != null) payload.put("top_p", topP);
        payload.put("response_format", Map.of("type", "json_object"));
        payload.put("messages", messages);
        return doRequest(payload, timeout);
    }

    private LlmCaptionResult doRequest(Map<String, Object> payload, Duration timeout) {
        String raw;
        try {
            raw = withTimeout(() ->
                            restClient.post()
                                    .uri("/chat/completions")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(payload)
                                    .retrieve()
                                    .body(String.class),
                    timeout
            );
        } catch (java.util.concurrent.TimeoutException e) {
            throw new IllegalStateException("OpenAI 요청 타임아웃: " + timeout.toMillis() + "ms", e);
        } catch (Exception e) {
            throw new IllegalStateException("OpenAI 요청 실패", e);
        }

        try {
            com.fasterxml.jackson.databind.JsonNode root = MAPPER.readTree(raw);
            String content = root.path("choices").get(0).path("message").path("content").asText("");
            com.fasterxml.jackson.databind.JsonNode node = MAPPER.readTree(content);
            String caption = node.path("content").asText("");
            java.util.List<String> media = node.has("media_urls")
                    ? MAPPER.convertValue(node.get("media_urls"),
                    MAPPER.getTypeFactory().constructCollectionType(java.util.List.class, String.class))
                    : java.util.List.of();
            return new LlmCaptionResult(caption, media);
        } catch (Exception e) {
            return new LlmCaptionResult(raw, java.util.List.of());
        }
    }

    /** RestClient 호출에 per-request 타임아웃 적용 */
    private static <T> T withTimeout(java.util.function.Supplier<T> supplier, Duration timeout) throws Exception {
        java.util.concurrent.CompletableFuture<T> future = java.util.concurrent.CompletableFuture.supplyAsync(supplier);
        try {
            return future.get(timeout.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
        } finally {
            future.cancel(true);
        }
    }

    @Getter
    public static class LlmCaptionResult {
        private final String content;
        private final List<String> mediaUrls;
        public LlmCaptionResult(String content, List<String> mediaUrls) {
            this.content = content;
            this.mediaUrls = mediaUrls;
        }
    }
}