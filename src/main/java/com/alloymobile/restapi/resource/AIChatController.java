package com.alloymobile.restapi.resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIChatController {
    @PostMapping("/chat")
    public ChatResponse chatAI(@RequestBody ChatRequest chatRequest) throws Exception {
         return new ChatResponse("AI chat sample response...");
    }

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
