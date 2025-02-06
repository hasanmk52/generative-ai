package com.example.generativeai.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/prompt")
    public String generateResponse(@RequestParam String category, String year){
        return chatService.gentChatResponse(category, year).getResult().getOutput().getContent();
    }

    @GetMapping("/generate-image")
    public String generateRespImage(@RequestParam String query) {
        return chatService.getImageChatReader(query);
    }

}
