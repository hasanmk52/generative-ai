package com.example.generativeai.functioncalling;

import com.example.generativeai.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private ChatService chatService;

    @GetMapping("/query") //query here can be "What's the temperature in London ?"
    public Map<String, String> getWeatherDetails(@RequestParam String query) {
        return Map.of("response",
                chatService.getWeatherInfo(query).getResult().getOutput().getContent()
        );
    }

}
