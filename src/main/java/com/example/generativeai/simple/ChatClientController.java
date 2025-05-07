package com.example.generativeai.simple;

import com.example.generativeai.dto.BooksInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simple-chat")
@RequiredArgsConstructor
public class ChatClientController {

    private final @Qualifier("openAIChatClient") ChatClient chatClient;

    // using Prompt and JSON object
    @GetMapping("/v0")
    public String getResponse(@RequestParam String category, String year) {
        return chatClient.prompt()
                .user(u-> u.text("Please provide the book details for the given {category} and {year} in the JSON format.")
                        .param("category", category)
                        .param("year", year))
                .call()
                .content();
    }

    //using BeanOutputConverter
    @GetMapping("/v1")
    public BooksInfo getBeanResponse(@RequestParam String category, String year) {
        return chatClient.prompt()
                .user(u-> u.text("Please provide the book details for the given {category} and {year}.")
                        .param("category", category)
                        .param("year", year))
                .call()
                .entity(BooksInfo.class);
    }

    // ParameterizedTypeReference to handle generic case
    @GetMapping("/v2")
    public List<BooksInfo> getListBeanResponse(@RequestParam String category, String year) {
        return chatClient.prompt()
                .user(u-> u.text("Please provide 2 book details for the given {category} and {year}.")
                        .param("category", category)
                        .param("year", year))
                .call()
                .entity(new ParameterizedTypeReference<>() { });
    }

    // using ListOutputConverter
    @GetMapping("/v3")
    public List<String> getListResponse(@RequestParam String category, String year) {
        return chatClient.prompt()
                .user(u-> u.text("Please provide the names of  5 best books for the given {category} and the {year}")
                        .param("category", category)
                        .param("year", year))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
    }

    // using MapOutputConverter
    @GetMapping("/v4")
    public Map<String, Object> getMapResponse(@RequestParam String category, String year) {
        return chatClient.prompt()
                .user(u-> u.text("Please provide me best book for the given {category} and the {year}.\n" +
                                "                Please do provide a summary of the book as well, the information should be \n" +
                                "                limited and not much in depth. The response should be in the JSON format " +
                                "                containing this information:\n" +
                                "                category, book, year, review, author, summary" +
                                "                Please remove ```json from the final output"
                        )
                        .param("category", category)
                        .param("year", year))
                .call()
                .entity(new ParameterizedTypeReference<>() { });
    }

}
