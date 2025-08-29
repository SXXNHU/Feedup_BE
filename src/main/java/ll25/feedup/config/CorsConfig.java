package ll25.feedup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        corsConfiguration.setAllowCredentials(true);

        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedOrigin("https://feedup.cloud");

        corsConfiguration.addAllowedMethod(HttpMethod.GET);
        corsConfiguration.addAllowedMethod(HttpMethod.POST);
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);

        corsConfiguration.addAllowedHeader(HttpHeaders.AUTHORIZATION);
        corsConfiguration.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
        corsConfiguration.addAllowedHeader(HttpHeaders.ACCEPT);

        corsConfiguration.addExposedHeader(HttpHeaders.AUTHORIZATION);

        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}