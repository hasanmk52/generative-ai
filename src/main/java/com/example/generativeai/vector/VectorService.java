package com.example.generativeai.vector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonMetadataGenerator;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VectorService {

    private final @Qualifier("openAiVectorStore") VectorStore openAiVectorStore;

    @Value("classpath:/data/bikes.json")
    Resource bikesResource;

    @Value("classpath:/data/story.txt")
    Resource storyResource;

    @Value("classpath:/prompt/system.st")
    Resource systemPrompt;

    public List<Document> simpleVector(String query){
        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("The World is Big"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

        //add documents to the vector store
        openAiVectorStore.add(documents);

        // retrieve documents similar to the given query
        return openAiVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(2)
                        .similarityThresholdAll()
                        .build()
                       // .similarityThreshold(0.8)//80%
        );
    }

    public List<Document> queryJSONVector(String query) {
        // read json file
        JsonReader jsonReader = new JsonReader(bikesResource,
                new ProductMetadataGenerator(),
                "name", "shortDescription", "description", "price", "tags");

        // create document object
        List<Document> documents = jsonReader.get();

        // add to vector store
        openAiVectorStore.add(documents);

        // query vector search
        List<Document> results = openAiVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(1)
                        .similarityThresholdAll()
                        .build()
        );
        return results ;
    }

    //We are defining this so that in the output we are only interested in name, shortDescription and price and not everything else that comes in the response
    public class ProductMetadataGenerator implements JsonMetadataGenerator {
        @Override
        public Map<String, Object> generate(Map<String, Object> jsonMap) {
            return Map.of("name", jsonMap.get("name"),
                    "shortDescription", jsonMap.get("shortDescription"),
                    "price", jsonMap.get("price"));
        }
    }

    public List<Document> getQueryDocs(String query) {
        //create vector store
        VectorStore vectorStore = loadDataInVectorStore();

        // vector search
        List<Document> results = vectorStore.similaritySearch(query);
        return results;
    }

    public List<Document> getQueryDocsSR(String query) {
        //create vector store
        VectorStore vectorStore = loadDataInVectorStore();

        // vector search
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(1)
                        .similarityThreshold(0.8)
                        .build()
        );
        return results;
    }

    public Message getSystemMessage(List<Document> similarDocuments) {
        String documents = similarDocuments
                .stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);

        Message systemMessage = systemPromptTemplate.createMessage(Map.of("documents", documents));
        log.info("systemMessageContent: {}", systemMessage.getText());

        return systemMessage;
    }

    public VectorStore loadDataInVectorStore() {
        // read text file
        TextReader textReader = new TextReader(storyResource);
        textReader.getCustomMetadata().put("filename", "story.txt");

        // convert the text into Document object
        List<Document> documents = textReader.get();
        log.info("documents: {}", documents);

        // chunking of text
        var textSplitter = new TokenTextSplitter();

        // add it into vector store
        openAiVectorStore.add(textSplitter.apply(documents));

        return openAiVectorStore;
    }
}
