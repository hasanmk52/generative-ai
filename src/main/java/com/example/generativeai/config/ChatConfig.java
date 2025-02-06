package com.example.generativeai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class ChatConfig {

    @Bean
    @Primary
    public ChatModel chatModel(OpenAiChatModel openAiChatModel) {
        return openAiChatModel;
    }

    @Bean
    public ChatClient basicChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public ChatClient advancedChatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultSystem("You are a helpful travelling assistant.")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SafeGuardAdvisor(List.of("illegal", "smuggling", "weapons", "drugs")),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }
}
