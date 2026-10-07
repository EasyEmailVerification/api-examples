package com.example.signup;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class EmailVerificationClient {

    private final RestClient http;

    public EmailVerificationClient(RestClient.Builder builder,
                                   @Value("${eev.api-key:eev_sandbox_key}") String apiKey) {
        this.http = builder
                .baseUrl("https://api.easyemailverification.com/v1")
                .defaultHeader("X-API-Key", apiKey)
                .build();
    }

    public EmailVerification verify(String email) {
        return http.get()
                .uri(uri -> uri.path("/verify").queryParam("email", "{email}").build(email))
                .retrieve()
                .body(EmailVerification.class);
    }

    public List<EmailVerification> verifyAll(List<String> emails) { // up to 50 per request
        return http.post()
                .uri("/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("emails", emails))
                .retrieve()
                .body(new ParameterizedTypeReference<List<EmailVerification>>() {});
    }
}
