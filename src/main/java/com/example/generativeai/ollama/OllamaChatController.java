package com.example.generativeai.ollama;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ollama")
public class OllamaChatController {

    private final OllamaChatService ollamaChatService;
    private final @Qualifier("ollamaChatClient") ChatClient chatClient;

    @GetMapping("/chat")
    public String getResponse(@RequestParam String category, String year){
        return ollamaChatService.getResponse(category, year);
    }

    @PostMapping("/ask")
    public String getResponse(@RequestBody String query){
        return ollamaChatService.getSummarizeText(query);
    }

    @GetMapping("/image/chat")
    public String getMultiModalResp() throws IOException {
        return ollamaChatService.getMultiModalResponse();
    }

    //Here query can be 'What did the author do while growing up ?'
    @GetMapping("/text/query")
    public Map<String, String> getQueryResp(@RequestParam String query) {
        VectorStore vectorStore = ollamaChatService.loadDataInVectorStore();

        return Map.of("response", chatClient.prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().similarityThresholdAll().topK(4).build()))
                .user(query)
                .call()
                .content());

    }

}
