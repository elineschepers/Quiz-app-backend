package be.ucll.quizappbackend.Repositories;

import be.ucll.quizappbackend.Models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz,Long> {

    Optional<Quiz> findFirstByLectorIdOrderByIdDesc(String lectorId);

    @Query(value = "SELECT COUNT(*) AS total, SUM(CASE WHEN  GUESSED = TRUE THEN 1 ELSE 0 END) AS guessed FROM QUIZ q " +
            "INNER JOIN QUIZ_ANSWER s ON q.ID = s.QUIZ_ID WHERE q.ID IN (SELECT q.ID FROM QUIZ q " +
            "INNER JOIN QUIZ_ANSWER s ON q.ID = s.QUIZ_ID WHERE q.LECTOR_ID = ?1 AND NOT q.ID = ?2 " +
            "AND s.GROUP_ID = ?3 ORDER BY q.ID DESC LIMIT 1)", nativeQuery = true)
    Map<String, BigInteger> findScorePrevQuizForGroupAndLector(String lectorId, long quizId, String groupId);

}
