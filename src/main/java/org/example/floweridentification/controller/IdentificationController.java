package org.example.floweridentification.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/")
public class IdentificationController {
    @Value("${openai.api.key}")
    private String apikey;

    private final WebClient webClient;

    public IdentificationController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
    }
    @GetMapping("/apikey")
    public String getApikey() {
        return apikey;
    }
}
