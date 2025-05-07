package com.example.generativeai.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final @Qualifier("openAIChatClient") ChatClient chatClient;

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

        return chatClient.prompt(prompt).call().chatResponse();
    }

    public String getImageChatReader(String query) {
        Resource resource = new ClassPathResource("/image/code.png");

        UserMessage userMessage = new UserMessage(query,
                List.of(new Media(MimeTypeUtils.IMAGE_PNG, resource)));


        var response = chatClient.prompt(new Prompt(userMessage)).call().chatResponse();
        return response.getResult().getOutput().getText();
    }

    public ChatResponse getWeatherInfo(String query) {

        Prompt prompt = new Prompt(new UserMessage(query),
                OpenAiChatOptions.builder().function("currentWeather").build());

        return chatClient.prompt(prompt).call().chatResponse();
    }
}
