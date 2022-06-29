package be.ucll.quizappbackend.Repositories;

import be.ucll.quizappbackend.Models.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {

    List<QuizAnswer> findAllByGuessed(Boolean guessed);

    @Query(
            value = "SELECT * FROM QUIZ_ANSWER WHERE STUDENT_ID = ?1 AND QUIZ_ID IN (SELECT ID FROM QUIZ WHERE LECTOR_ID = ?2) ORDER BY ID DESC",
            nativeQuery = true)
    List<QuizAnswer> findQuizAnswersForLector(String studentId, String lectorId);
}
