package org.example.floweridentification.controller;

import org.example.floweridentification.dto.*;
import org.example.floweridentification.service.ChatGPTService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
//https://www.jsonschema2pojo.org/
@RestController
@RequestMapping("/")
public class IdentificationController {


    private final WebClient webClient;
    private final ChatGPTService gptService;

    public IdentificationController(WebClient.Builder webClientBuilder, ChatGPTService gptService) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.gptService = gptService;
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/identify")
    public Map<String, Object> chatWithGPT(@RequestBody Map<String, String> requestBody) {
        String imageUrl = requestBody.get("imageUrl");

        ChatRequest chatRequest = gptService.createFlowerIdentificationRequest(imageUrl);

        ChatResponse response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(gptService.getApikey()))
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        List<Choice> lst = response.getChoices();
        Usage usg = response.getUsage();

        Map<String, Object> map = new HashMap<>();
        //map.put("request", chatRequest); DEBUG TING
        map.put("Usage", usg);
        map.put("Choices", lst);

        return map;
    }
}
