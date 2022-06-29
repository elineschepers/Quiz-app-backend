package be.ucll.quizappbackend.Controllers;

import be.ucll.quizappbackend.Models.Quiz;
import be.ucll.quizappbackend.Services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health(Model model, HttpServletRequest request) {
        return "OK";
    }

}


