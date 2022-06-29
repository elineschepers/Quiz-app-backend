package be.ucll.quizappbackend.Controllers;

import be.ucll.quizappbackend.Models.QuizAnswer;
import be.ucll.quizappbackend.Response.ResponseHandler;
import be.ucll.quizappbackend.Services.QuizService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Creates quiz with given settings and returns Quiz ID to frontend
    @PostMapping("/start/")
    public ResponseEntity<Object> startQuiz(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, @RequestBody HashMap<String, Object> params) {
        return ResponseHandler.generateResponse("", quizService.createQuiz(authorizedClient.getPrincipalName(), authorizedClient.getAccessToken().getTokenValue(), (String) params.get("type"), (List<String>) params.get("groups")));
    }

    @PostMapping("/{quizId}/getQuestion/")
    public ResponseEntity<Object> getQuestion(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, @PathVariable Long quizId) {
        Map<String, Object> response = new HashMap<>();
        response.put("question", quizService.createQuestion(authorizedClient.getAccessToken().getTokenValue(), quizId));
        response.put("statistics", quizService.getLiveQuizStatistics(quizId));
        return ResponseHandler.generateResponse("", response);
    }

    @PostMapping("/{quizId}/submitAnswer/")
    public ResponseEntity<Object> submitAnswer(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, @PathVariable Long quizId, @Valid @RequestBody QuizAnswer quizAnswer) {
        return ResponseHandler.generateResponse("", quizService.submitAnswer(quizId, quizAnswer));
    }

    @PostMapping("/{quizId}/end/")
    public ResponseEntity<Object> endQuiz(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, @PathVariable Long quizId){
        return ResponseHandler.generateResponse("", quizService.endQuiz(authorizedClient.getAccessToken().getTokenValue(), quizId, authorizedClient.getPrincipalName()));
    }

    @PostMapping("/{quizId}/student/{studentId}/disable/")
    public ResponseEntity<Object> disableStudent(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, @PathVariable Long quizId, @PathVariable String studentId){
        quizService.disableStudentForUser(studentId, authorizedClient.getPrincipalName(), quizId);
        return ResponseHandler.generateResponse("", "success");
    }
}


