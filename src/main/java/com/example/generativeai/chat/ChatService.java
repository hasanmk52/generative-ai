package com.example.generativeai.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModel chatModel;

    public ChatResponse gentChatResponse(String category, String year) {

        PromptTemplate promptTemplate = new PromptTemplate(
                """
                Please provide me best book for the given {category} and the {year}.
                Please do provide a summary of the book as well, the information should be 
                limited and not much in depth. Please provide the details in the JSON format
                containing this information : category, book, year, review, author, summary
                """
        );

        promptTemplate.add("category", category);
        promptTemplate.add("year", year);

        Prompt prompt = promptTemplate.create();

        return chatModel.call(prompt);
    }

    public String getImageChatReader(String query) {
        Resource resource = new ClassPathResource("/image/code.png");

        UserMessage userMessage = new UserMessage(query,
                List.of(new Media(MimeTypeUtils.IMAGE_PNG, resource)));


        var response = chatModel.call(new Prompt(userMessage));
        return response.getResult().getOutput().getContent();
    }

    public ChatResponse getWeatherInfo(String query) {

        UserMessage userMessage = new UserMessage(query);

        return chatModel.call(new Prompt((userMessage),
                OpenAiChatOptions.builder().function("currentWeather").build()));

    }
}
