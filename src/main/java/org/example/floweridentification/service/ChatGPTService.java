package org.example.floweridentification.service;

import org.example.floweridentification.dto.ChatRequest;
import org.example.floweridentification.dto.Content;
import org.example.floweridentification.dto.ImageUrl;
import org.example.floweridentification.dto.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ChatGPTService {
    @Value("${openai.api.key}")
    private String apikey;

    public String getApikey() {
        return apikey;
    }
    public ChatRequest createFlowerIdentificationRequest(String imageUrl) {

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-4o");
        // Create text content
        Content textContent = new Content();
        textContent.setType("text");
        textContent.setText("Identify the flower in the image");

        // Create image URL content
        Content imageContent = new Content();
        imageContent.setType("image_url");
        imageContent.setImageUrl(new ImageUrl(imageUrl));

        // Create and populate the message
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        contentList.add(imageContent);

        Message message = new Message();
        message.setRole("user");
        message.setContent(contentList);

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        chatRequest.setMessages(messages);
        chatRequest.setMaxTokens(300);
        return chatRequest;
    }
}
