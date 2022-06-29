/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 20/02/2022 12:16
 */

package be.ucll.quizappbackend.Services;

import be.ucll.quizappbackend.Models.Quiz;
import be.ucll.quizappbackend.Models.QuizQuestion;
import be.ucll.quizappbackend.Repositories.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizQuestionService {

    @Autowired
    QuizQuestionRepository repository;

    /**
     * Returns a QuizQuestion object by a student hash and a Quiz
     *
     * @param quiz the quiz to which the question belongs
     * @param studentHash the hashed student id
     * @return the QuizQuestion object
     */
    public QuizQuestion getQuestionByQuizAndStudentHash(Quiz quiz, String studentHash) {
        return repository.getQuizQuestionByQuizEqualsAndStudentHashIs(quiz, studentHash);
    }

    /**
     * Update a given QuizQuestion to the db
     *
     * @param quizQuestion the object to update
     * @return the updated object
     */
    public QuizQuestion updateQuizQuestion(QuizQuestion quizQuestion) {
        return this.repository.save(quizQuestion);
    }

    /**
     * Count all existing QuizQuestions for a given Quiz
     *
     * @param quiz the Quiz object
     * @return the amount of existing QuizQuestions
     */
    public Long countAllByQuiz(Quiz quiz) {
        return this.repository.countAllByQuiz(quiz);
    }

    /**
     * Count all existing QuizQuestion for a given Quiz
     * where the interval is larger than a given number
     *
     * @param quiz the Quiz object
     * @param interval the minimum interval
     * @return the amount of existing QuizQuestions with I > interval
     */
    public Long countAllByQuizAndIGreaterThan(Quiz quiz, long interval) {
        return this.repository.countAllByQuizAndIGreaterThan(quiz, interval);
    }

    /**
     * Get the next question for a given Quiz
     *
     * @param quizId the quiz to fetch a question for
     * @param hashedStudent the previously asked question
     * @return the next QuizQuestion
     */
    public QuizQuestion getNextQuestion(long quizId, String hashedStudent){return this.repository.getNextQuestion(quizId, hashedStudent);}

    /**
     * Delete a QuizQuestion from the database
     *
     * @param quizQuestion the QuizQuestion to delete
     */
    public void deleteQuestion(QuizQuestion quizQuestion) {
        QuizQuestion deleteMe = repository.findQuizQuestionById(quizQuestion.getId());
        if (deleteMe!=null) {
            this.repository.delete(quizQuestion);
        }
    }
}
