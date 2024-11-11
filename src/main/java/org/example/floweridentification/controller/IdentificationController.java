package org.example.floweridentification.controller;

import org.example.floweridentification.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/identify")
    public Map<String, Object> chatWithGPT() {
        String imageUrl = "https://harkness-roses.s3.amazonaws.com/700/530920.jpg";
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-4o");
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "You are a flower expert and your purpose is to help me identify a flower."));
        messages.add(new Message("user", "I have a picture of a flower. Can you help me identify it?"));

        // Correct way to include image
        Message userMessageWithImage = new Message("user", "What's in this image?");
        userMessageWithImage.setImageUrl(new ImageURL(imageUrl));
        messages.add(userMessageWithImage);

        chatRequest.setMessages(messages);
        chatRequest.setMaxTokens(300);

        ChatResponse response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(apikey))
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        List<Choice> lst = response.getChoices();
        Usage usg = response.getUsage();

        Map<String, Object> map = new HashMap<>();
        map.put("request", chatRequest);
        map.put("Usage", usg);
        map.put("Choices", lst);

        return map;
    }
}
