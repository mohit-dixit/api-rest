package com.alloymobile.restapi.resource;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIChatController {

    private final OpenAiChatClient chatClient;

    public AIChatController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/chat")
    public ChatResponse chatAI(@RequestBody ChatRequest chatRequest) throws Exception {
         return new ChatResponse("AI chat sample response...");
    }

    // @GetMapping("/chat")// not being used as the OPEN need the credit balance.
    // public String streamChat(@RequestParam String message) {
    //     try {
    //         if (message.length() > 1000) {
    //             throw new IllegalArgumentException("Message exceeds maximum length of 1000 characters.");
    //         }
    //         return chatClient.call(message);
    //     } catch (NonTransientAiException ex) {
    //         if (ex.getMessage().contains("429")) {
    //             return "Quota exceeded. Try again later or upgrade plan.";
    //         }
    //         throw ex;
    //     }
    // }
}

class ChatRequest {
    private String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}

class ChatResponse {
    private String response;  

    public ChatResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}

