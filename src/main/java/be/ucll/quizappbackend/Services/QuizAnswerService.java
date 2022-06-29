package be.ucll.quizappbackend.Services;

import be.ucll.quizappbackend.Models.QuizAnswer;
import be.ucll.quizappbackend.Repositories.QuizAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizAnswerService {

    @Autowired
    private QuizAnswerRepository quizAnswerRepository;

    /**
     * Returns all answers submitted for a given lector concerning a certain student
     *
     * @param studentId the student mentioned in the answers
     * @param lectorId the lector who filled out the quizzes
     * @return a list of all answers submitted
     */
    public List<QuizAnswer> findQuizAnswersForLector(String studentId, String lectorId) {
        return quizAnswerRepository.findQuizAnswersForLector(studentId, lectorId);
    }

}
