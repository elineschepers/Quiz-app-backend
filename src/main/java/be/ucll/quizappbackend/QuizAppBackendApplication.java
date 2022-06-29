package be.ucll.quizappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class QuizAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizAppBackendApplication.class, args);
    }

}
