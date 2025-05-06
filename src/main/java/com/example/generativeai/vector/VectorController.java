package com.example.generativeai.vector;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("vector")
@RequiredArgsConstructor
public class VectorController {

    private final VectorService vectorService;
    private final ChatModel chatModel;
    private final @Qualifier("basicChatClient") ChatClient basicChatClient;

    //The query param here for example can be: Spring AI
    @GetMapping("/gen")
    public String genVectorSearch(@RequestParam String query) {
        return vectorService.simpleVector(query).getFirst().getText();
    }

    //The query param here for example can be : I am looking for an e-bike. Which bike would be good for me?
    //Another example param: I am looking for a road bike. Which bike would be good for me?
    @GetMapping("/json/query")
    public String getQueryResults(@RequestParam String query) {
        return vectorService.queryJSONVector(query).getFirst().getMetadata().toString();
    }

    //The query param here for example can be : What did the author do while growing up ?
    @GetMapping("/text/v1/query")
    public String getQueryRespV1(@RequestParam String query) {
        return vectorService.getQueryDocs(query).getFirst().getText();
        //return vectorService.getQueryDocs(query);

    }

    @GetMapping("/text/v2/query")
    public String getQueryRespV2(@RequestParam String query) {
        return vectorService.getQueryDocsSR(query).getFirst().getText();
    }

    @GetMapping("/text/v3/query")
    public Map<String, String> getQueryRespV3(@RequestParam String query) {
        List<Document> similarDocuments = vectorService.getQueryDocsSR(query);

        UserMessage userMessage = new UserMessage(query);
        Message systemMessage = vectorService.getSystemMessage(similarDocuments);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return Map.of("response", chatModel.call(prompt).getResult().getOutput().getText());
    }

    @GetMapping("/text/v4/query")
    public Map<String, String> getQueryRespV4(@RequestParam String query) {
        VectorStore vectorStore = vectorService.loadDataInVectorStore();

        return Map.of("response", basicChatClient.prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().similarityThresholdAll().topK(4).build()))
                .user(query)
                .call()
                .content());

    }
}
