package com.example.generativeai.semantic;

import com.example.generativeai.dto.SemanticOutput;
import com.example.generativeai.dto.SemanticRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semantic")
@RequiredArgsConstructor
public class SemanticController {

    private final SemanticService semanticService;

    @PostMapping("/match")
    public List<SemanticOutput> getSemanticMapper(@RequestBody SemanticRequest semanticRequest) {
        /*List<String> sourceList = List.of(
                "What is your  first name?",
                "When is your birthday?",
                "Where were you born?",
                "What is your favorite color?",
                "Which genre of music do you prefer?",
                "What type of cuisine do you enjoy the most?",
                "What's your hobby?",
                "What is your dream job?",
                "Which sport do you like watching?",
                "What's your go-to relaxation activity?"
        );

        List<String> destinationList = List.of(
                "Your date of birth, please?",
                "May I know your given name?",
                "The place of your birth?",
                "Your music taste?",
                "Your preferred color?",
                "Your favored culinary style?",
                "Any pastimes you engage in?",
                "Your idea career?",
                "Favorite sport to follow?",
                "How do you like to unwind?"
        );*/

        return semanticService.getSemanticMatch(semanticRequest.sourceList(), semanticRequest.destinationList());
    }
}
