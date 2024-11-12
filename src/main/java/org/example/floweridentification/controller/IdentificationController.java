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
    public Map<String, Object> chatWithGPT() { // der her skal være i metode for sig selv
        String imageUrl = "https://harkness-roses.s3.amazonaws.com/700/530920.jpg";
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-4o");
        // Create text content
        Content textContent = new Content();
        textContent.setType("text");
        textContent.setText("What’s in this image?");

        // Create image URL content
        Content imageContent = new Content();
        imageContent.setType("image_url");
        imageContent.setImageUrl((new ImageUrl());

        // Create and populate the message
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        contentList.add(imageContent);

        Message message = new Message();
        message.setRole("user");
        message.setContent(contentList);

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
        //map.put("request", chatRequest);
        map.put("Usage", usg);
        map.put("Choices", lst);

        return map;
    }
}
