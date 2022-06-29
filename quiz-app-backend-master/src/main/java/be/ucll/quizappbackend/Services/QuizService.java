package be.ucll.quizappbackend.Services;

import be.ucll.quizappbackend.Models.*;
import be.ucll.quizappbackend.Models.Requests.Student;
import be.ucll.quizappbackend.Models.Responses.Choice;
import be.ucll.quizappbackend.Models.Responses.Question;
import be.ucll.quizappbackend.Repositories.QuizRepository;
import be.ucll.quizappbackend.Util.Hasher;
import be.ucll.quizappbackend.Util.SpacedRepetition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.round;

@Service
public class QuizService {

    @Autowired
    private QuizRepository repository;

    @Autowired
    private UcllService ucllService;

    @Autowired
    private QuizAnswerService quizAnswerService;

    @Autowired
    private QuizQuestionService quizQuestionService;

    @Autowired
    private QuizUserDataService quizUserDataService;

    @Value("${quiz.usergradeifwrong}")
    private double usergradewrong;

    /**
     * Get a quiz by its id
     *
     * @param quizId the id of the quiz that needs to be fetched
     * @return the quiz (null if not found)
     */
    public Quiz getQuiz(Long quizId) {
        return repository.getById(quizId);
    }

    /**
     * Fetches the previous quiz for a given user
     *
     * @param lectorId the user to fetch the quiz for
     * @return the Quiz object for the previous quiz
     */
    public Quiz getPreviousQuiz(String lectorId) {
        return repository.findFirstByLectorIdOrderByIdDesc(lectorId).orElse(null);
    }

    /**
     * Creates a new quiz for a lector and creates all possible questions
     * with statistics from previous sessions. All data is saved to DB.
     *
     * @param type   type of quiz --> input, multiple-choice or touch
     * @param groups class groups that needed to be used in this quiz
     * @return the newly created quiz
     */
    public Quiz createQuiz(String lectorId, String token, String type, List<String> groups) {
        // Clean up DB if previous quiz was exited incorrectly
        Quiz prevQuiz = getPreviousQuiz(lectorId);
        if (prevQuiz != null && prevQuiz.getQuizQuestions().size() > 0) {
            endQuiz(token, prevQuiz.getId(), lectorId);
        }

        Quiz quiz = new Quiz(type, groups, lectorId);

        Map<Integer, QuizQuestion> questions = new HashMap<>();
        for (String group : quiz.getGroups()) {
            for (Student student : ucllService.fetchStudentsByGroup(token, group).values()) {
                // Student already in the set of questions?
                if (!questions.containsKey(student.getPersonId())) {
                    HashMap<String, Object> stats = calculateStudentStatistics(student, lectorId);
                    String hashed_student = Hasher.sha256(Integer.toString(student.getPersonId()));
                    boolean active = true;

                    double interval;
                    double ef;
                    // Student has been asked before, fetch spaced learning numbers
                    if (quizUserDataService.exists(hashed_student, lectorId)) {
                        QuizUserData qud = quizUserDataService.getQuizUserDataByStudentIdAndLectorId(hashed_student, lectorId);
                        active = qud.isActive();
                        interval = qud.getInterval();
                        ef = qud.getEf();
                    }
                    // Student hasn't been asked yet to the teacher
                    else {
                        QuizUserData qqd = new QuizUserData(hashed_student, lectorId);
                        quizUserDataService.add(qqd);
                        interval = qqd.getInterval();
                        ef = qqd.getEf();
                    }
                    if (active) {
                        questions.put(student.getPersonId(), new QuizQuestion(hashed_student, group, (int) stats.get("streak"), (double) stats.get("percentage"), interval, ef, (int) stats.get("asked"), quiz));
                    }
                }
            }
        }
        quiz.setQuizQuestions(new ArrayList<>(questions.values()));
        quiz = repository.save(quiz);
        return quiz;
    }

    /**
     * Create a question for a given quiz (by id) depending on the quiz type
     *
     * @param quizId the id of the quiz
     * @return a Question object containing all necessary info
     */
    public Question createQuestion(String token, Long quizId) {
        Map<String, Student> students = ucllService.fetchStudents(token);
        Quiz quiz = repository.getById(quizId);

        Student student = this.fetchRandomStudent(quiz, students);
        Question question = new Question(quiz.getId(), student.getPersonId(), quiz.getType(), student.getFirstName(), student.getLastName(), student.getPhotoUrl(), student.getGroup());
        if (quiz.getType().equals("multiple-choice")) {
            question.setChoices(this.createChoices(students, quiz.getQuizQuestions(), student, 3));
        }
        return question;
    }

    /**
     * Calculates the statistics for a given student in quizzes by a given lector.
     *
     * @param student  the student to calculate statistics for
     * @param lectorId the user who filled out the quizzes.
     * @return a hashmap with statistics where the key is the type of statistic
     */
    public HashMap<String, Object> calculateStudentStatistics(Student student, String lectorId) {
        List<QuizAnswer> quizAnswers = quizAnswerService.findQuizAnswersForLector(Hasher.sha256(Integer.toString(student.getPersonId())), lectorId);
        boolean streaking = true;
        int timesAsked = quizAnswers.size();
        int streak = 0;
        int correct = 0;
        for (QuizAnswer quizAnswer : quizAnswers) {
            if (quizAnswer.getGuessed()) {
                correct++;
                if (streaking) {
                    streak++;
                }
            } else {
                streaking = false;
            }
        }
        HashMap<String, Object> results = new HashMap<>();
        results.put("streak", streak);
        // Prevent division by zero
        if (timesAsked == 0) {
            results.put("percentage", (double) 0);
        } else {
            results.put("percentage", (double) correct / (double) timesAsked);
        }
        results.put("asked", timesAsked);
        return results;
    }

    /**
     * Update the statistics for a given student after a new answer has been submitted
     *
     * @param quiz       the quiz to which the answer belongs
     * @param quizAnswer the submitted answer
     */
    public QuizQuestion updateStatistics(Quiz quiz, QuizAnswer quizAnswer) {
        QuizQuestion question = quizQuestionService.getQuestionByQuizAndStudentHash(quiz, quizAnswer.getStudentId());
        if (quizAnswer.getGuessed()) {
            question.increaseStreak();
            question.setPercentage(((question.getPercentage() * question.getTimesAsked()) + 1) / (question.getTimesAsked() + 1));
        } else {
            question.setStreak(0);
            question.setPercentage((question.getPercentage() * question.getTimesAsked()) / (question.getTimesAsked() + 1));
        }
        question.increaseTimesAsked();
        return quizQuestionService.updateQuizQuestion(question);
    }

    /**
     * Calculate the max streak of a quiz
     *
     * @param quiz the quiz to calculate the streak for
     * @return the maximum streak
     */
    public HashMap<String, Object> calculateQuizStatistics(Quiz quiz, String prefix) {
        int askedQuestions = quiz.getQuizAnswers().size();
        int maxStreak = 0;
        int streak = 0;
        int correct = 0;
        for (QuizAnswer quizAnswer : quiz.getQuizAnswers()) {
            if (quizAnswer.getGuessed()) {
                streak++;
                correct++;
            } else {
                streak = 0;
            }
            if (streak > maxStreak) {
                maxStreak = streak;
            }
        }
        int percentage = (int) round(((double) correct / (double) askedQuestions) * 100);
        HashMap<String, Object> results = new HashMap<>();
        results.put(prefix + "quizId", quiz.getId());
        results.put(prefix + "askedquestions", askedQuestions);
        results.put(prefix + "streak", maxStreak);
        results.put(prefix + "correct", correct);
        results.put(prefix + "percentage", percentage);
        return results;
    }

    /**
     * Calculate statistics to use and showcase during a quiz
     *
     * @param quizId the quize to calculate for
     * @return hashmap with statistics
     */
    public Map<String, Object> getLiveQuizStatistics(Long quizId) {
        Map<String, Object> result = new HashMap<>();
        Quiz quiz = repository.getById(quizId);

        long total = quizQuestionService.countAllByQuiz(quiz);
        long known = quizQuestionService.countAllByQuizAndIGreaterThan(quiz, 100);

        result.put("percentageKnown", (int) round(((double) known / (double) total) * 100));
        result.put("streak", quiz.getStreak());
        return result;
    }

    /**
     * End a quiz, take care of all calculations and return the statistics
     *
     * @param quizId id of the quiz to end
     * @return a map conataining usefull statistics
     */
    public HashMap<String, Object> endQuiz(String token, Long quizId, String lectorId) {
        HashMap<String, Object> res = new HashMap<>();
        Quiz quiz = getQuiz(quizId);

        if (quiz.getQuizAnswers().size() > 0) {
            long total = 0;
            long guessed = 0;
            for (String group : quiz.getGroups()) {
                Map<String, BigInteger> map = repository.findScorePrevQuizForGroupAndLector(lectorId, quizId, group);
                // When guessed is null, no questions on this group where asked in the previous quiz.
                if (map.get("guessed") != null) {
                    total += map.get("total").longValue();
                    guessed += map.get("guessed").longValue();
                }
            }

            res.put("currQuiz", this.calculateQuizStatistics(quiz, ""));
            if (total > 0) {
                Map<String, Object> prevQuiz = new HashMap<>();
                prevQuiz.put("percentage", (int) round(((double) guessed / (double) total) * 100));
                prevQuiz.put("askedquestions", total);
                prevQuiz.put("correct", guessed);
                res.put("prevQuiz", prevQuiz);
            }
            this.deleteQuizQuestions(quiz);
        }
        // Quiz started by accident, no answers --> delete quiz
        else {
            this.deleteQuizQuestions(quiz);
            this.deleteQuiz(quiz);
        }

        return res;
    }

    /**
     * Fetches a random student out of a list of possible questions.
     *
     * @param quiz all possible questions
     * @return The randomly selected student object
     */
    private Student fetchRandomStudent(Quiz quiz, Map<String, Student> students) {
        QuizQuestion res = quizQuestionService.getNextQuestion(quiz.getId(), quiz.getLastStudentHash());
        return students.get(res.getStudentHash());
    }

    /**
     * Creates choices for a multiple-choice question out of a selected
     * student and a list of possible alternatives. The options will be
     * shuffled upon return.
     *
     * @param questions all possible choices
     * @param student   the student selected for the question
     * @param max       amount of alternate choices to render
     * @return a list of choices for the multiple-choice question
     */
    private List<Choice> createChoices(Map<String, Student> students, List<QuizQuestion> questions, Student student, int max) {
        Map<String, QuizQuestion> map = questions.stream().collect(Collectors.toMap(QuizQuestion::getStudentHash, Function.identity()));
        List<String> keys = new ArrayList<>(map.keySet());
        keys.remove(Hasher.sha256(Integer.toString(student.getPersonId())));

        List<Choice> names = new ArrayList<>();
        names.add(new Choice(student.getFirstName(), student.getLastName()));
        for (int i = 0; i < max && keys.size() > 0; i++) {
            QuizQuestion quizQuestion = map.get(keys.get(ThreadLocalRandom.current().nextInt(0, keys.size())));
            Student student_rand = students.get(quizQuestion.getStudentHash());
            names.add(new Choice(student_rand.getFirstName(), student_rand.getLastName()));
            keys.remove(quizQuestion.getStudentHash());
        }
        Collections.shuffle(names);
        return names;
    }

    /**
     * Submits a given answer to a given quiz
     *
     * @param quizId     the id of the quiz
     * @param quizAnswer the answer
     * @return the submitted answer
     */
    public QuizAnswer submitAnswer(Long quizId, QuizAnswer quizAnswer) {
        Quiz quiz = this.getQuiz(quizId);
        quizAnswer.setQuiz(quiz);
        quiz.addQuizAnswer(quizAnswer);
        quiz = this.updateQuiz(quiz);

        QuizQuestion quizQuestion = this.updateStatistics(quiz, quizAnswer);

        // Calculate spaced repetition and Update I and EF for QuizUserData and QuizQuestion
        double q = ((quizQuestion.getStreak() > 0) ? 5 : usergradewrong);
        double getAmountRight = quizQuestion.getPercentage() * quizQuestion.getTimesAsked();

        HashMap<String, Double> res = SpacedRepetition.calculate(q, getAmountRight, quizQuestion.getEf(), quizQuestion.getInterval());

        //updating interval of quizQuestion
        quizQuestion.setInterval(res.get("i"));
        quizQuestion.setEf(res.get("ef"));
        quizQuestionService.updateQuizQuestion(quizQuestion);

        return quizAnswer;
    }

    /**
     * Delete all questions for a given quiz also update
     * temporarily saved data to the QuizUserData tabel
     *
     * @param quiz the quiz to delete from
     */
    public void deleteQuizQuestions(Quiz quiz) {
        if (quiz != null) {
            for (QuizQuestion quizQuestion : quiz.getQuizQuestions()) {
                //updating QuizUserData
                QuizUserData quizUserData = quizUserDataService.getQuizUserDataByStudentIdAndLectorId(quizQuestion.getStudentHash(), quiz.getLectorId());
                quizUserData.setEf(quizQuestion.getEf());
                quizUserData.setInterval(quizQuestion.getInterval());
                quizUserData.setTimesAsked(quizQuestion.getTimesAsked());
                quizUserDataService.updateQuizUserData(quizUserData);
            }
            quiz.deleteAllQuestions();
            this.updateQuiz(quiz);
        }
    }

    /**
     * Update a given quiz to the database
     *
     * @param quiz the quiz to be updated
     */
    private Quiz updateQuiz(Quiz quiz) {
        return repository.save(quiz);
    }

    /**
     * Delete a given quiz from the database
     *
     * @param quiz the quiz to delete
     */
    private void deleteQuiz(Quiz quiz) {
        repository.delete(quiz);
    }

    /**
     * Get all quizzes from the database.
     *
     * @return all quizzes stored
     */
    public Iterable<Quiz> getAll() {
        return repository.findAll();
    }

    /**
     * Disable a given student for the currentin upcoming questions.
     *
     * @param studentId the id of the student to disable
     * @param lectorId the id of the user
     * @param quizId the quiz that is currently active
     */
    public void disableStudentForUser(String studentId, String lectorId, long quizId) {
        studentId = Hasher.sha256(studentId);
        Quiz quiz = getQuiz(quizId);
        quiz.deleteQuestionByStudentHash(studentId);
        this.updateQuiz(quiz);
        quizUserDataService.disableStudentHashForUser(studentId, lectorId);
    }

}
