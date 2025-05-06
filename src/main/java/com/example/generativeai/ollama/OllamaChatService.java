package com.example.generativeai.ollama;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OllamaChatService {

    private final OllamaChatModel chatModel;
    private final @Qualifier("ollamaVectorStore") VectorStore ollamaVectorStore;

    @Value("classpath:/data/story.txt")
    Resource storyResource;

    // generate response in JSON format
    public String getResponse(String category, String year) {
        String template = """
                 Please provide me best book for the given {category} and the {year}.
                 Please do provide a summary of the book as well, the information should be\s
                 limited and not much in depth. The response should be in the JSON format
                 containing this information:
                 category, book, year, review, author, summary
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        promptTemplate.add("category", category);
        promptTemplate.add("year", year);

        Prompt prompt = promptTemplate.create();

        return chatModel.call(prompt).getResult().getOutput().getText();
    }


    // summarize text in the structured format
    public String getSummarizeText(String text){
        String template = """
                 You will be given an {article}. 
                 You need to summarize it and provide the
                 output in the JSON format with these keys : 
                 topic, highlights.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        promptTemplate.add("article", text);

        Prompt prompt = promptTemplate.create();

        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    // multimodal - read images
    public String getMultiModalResponse() throws IOException {
        byte[] imageData = new ClassPathResource("/image/sample.png").getContentAsByteArray();

        var userMessage = new UserMessage("Explain what do you see on this picture?",
                List.of(Media.builder().mimeType(MimeTypeUtils.IMAGE_PNG).data(imageData).build())
        );

        return chatModel.call(new Prompt(userMessage)).getResult().getOutput().getText();
    }

    public VectorStore loadDataInVectorStore() {
        // read text file
        TextReader textReader = new TextReader(storyResource);
        textReader.getCustomMetadata().put("filename", "story.txt");

        // convert the text into Document object
        List<Document> documents = textReader.get();

        // chunking of text
        var textSplitter = new TokenTextSplitter();

        // add it into vector store
        ollamaVectorStore.add(textSplitter.apply(documents));

        return ollamaVectorStore;
    }
}
