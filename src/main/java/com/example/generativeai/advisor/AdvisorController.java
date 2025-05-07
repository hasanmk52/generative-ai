package com.example.generativeai.advisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/advisor")
public class AdvisorController {

    private ChatClient chatClient;

    public AdvisorController(@Qualifier("advancedChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    //here query can be: "Which flight would be best for Hyderabad to Dubai?"
    //and then since we have memory chat advisor we can iterate and further query: "Which would offer the lowest cost ?"
    @GetMapping("/response")
    public String getResponse(@RequestParam String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .content();
    }
}
