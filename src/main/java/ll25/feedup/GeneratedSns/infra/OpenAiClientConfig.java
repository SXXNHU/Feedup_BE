package ll25.feedup.GeneratedSns.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiClientConfig {

    @Bean
    public org.springframework.web.client.RestClient openAiRestClient(
            @org.springframework.beans.factory.annotation.Value("${openai.base-url:https://api.openai.com/v1}") String baseUrl,
            @org.springframework.beans.factory.annotation.Value("${openai.api-key}") String apiKey) {

        return org.springframework.web.client.RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}