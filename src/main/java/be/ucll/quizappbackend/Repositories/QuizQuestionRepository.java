/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 20/02/2022 12:17
 */

package be.ucll.quizappbackend.Repositories;

import be.ucll.quizappbackend.Models.Quiz;
import be.ucll.quizappbackend.Models.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizQuestionRepository  extends JpaRepository<QuizQuestion,Long> {

    QuizQuestion getQuizQuestionByQuizEqualsAndStudentHashIs(Quiz quiz, String studentHash);


    @Query(
            value = "SELECT * FROM QUIZ_QUESTION WHERE QUIZ_ID = ?1 AND NOT STUDENT_HASH = ?2 ORDER BY I LIMIT 1",
            nativeQuery = true)
    QuizQuestion getNextQuestion(long quizId, String hashedStudent);

    @Query(
            value = "SELECT * FROM QUIZ_QUESTION WHERE QUIZ_ID = ?1 ORDER BY I LIMIT 1",
            nativeQuery = true)
    QuizQuestion getNextQuestion(long quizId);

    Long countAllByQuiz(Quiz quiz);

    Long countAllByQuizAndIGreaterThan(Quiz quiz, double interval);

    QuizQuestion findQuizQuestionById(long id);
}

