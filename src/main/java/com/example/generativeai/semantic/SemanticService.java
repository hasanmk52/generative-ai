package com.example.generativeai.semantic;

import com.example.generativeai.dto.SemanticOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemanticService {

    private final ChatModel chatModel;

    public List<SemanticOutput> getSemanticMatch(final List<String> sourceList, List<String> destinationList) {

        BeanOutputConverter<List<SemanticOutput>> beanOutputConverter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<>() { }
        );

        String format = beanOutputConverter.getFormat();

        var template = """
                You will be provided a source and destination list of strings.
                You need to take a string one by one from the source list until
                its empty and try to match it with the string in the destination list.
                
                Please do note that you need to match the strings based on semantic mapping/similarity
                and not just based on string similarity. If you are not able to find any match for the
                given text in the source list, please do say "No match found" in the destination string
                only.
                
                Please also mention the matching score based on your confidence on the mapping. The match
                score should be a number between 0-100 where 0 means no match and 100 means perfect match.
                
                source list questions:
                {source_list}
                
                destination list questions:
                {destination_list}
                
                Your response:
                {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        promptTemplate.add("source_list", sourceList);
        promptTemplate.add("destination_list", destinationList);
        promptTemplate.add("format", format);

        Prompt prompt = promptTemplate.create();

        return beanOutputConverter.convert(chatModel.call(prompt).getResult().getOutput().getContent());
    }
}
