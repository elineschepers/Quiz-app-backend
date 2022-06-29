package be.ucll.quizappbackend.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Quiz implements Serializable {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String lectorId;

    private String type;

    @ElementCollection
    private List<String> groups;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonManagedReference
    private List<QuizAnswer> quizAnswers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<QuizQuestion> quizQuestions = new ArrayList<>();

    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

    public Quiz() {}

    public Quiz(String type, List<String> groups, String lectorId) {
        this.setType(type);
        this.setGroups(groups);
        this.setLectorId(lectorId);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getLectorId() {
        return lectorId;
    }

    public void setLectorId(String lectorId) {
        this.lectorId = lectorId;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<QuizAnswer> getQuizAnswers() {
        return quizAnswers;
    }

    public void setQuizAnswers(List<QuizAnswer> quizAnswers) {
        this.quizAnswers = quizAnswers;
    }

    public void addQuizAnswer(QuizAnswer quizAnswers) {
        this.getQuizAnswers().add(quizAnswers);
    }

    public LocalDateTime getCreatedAt() {
        if (createdAt == null) {
            return null;
        }
        return createdAt.toLocalDateTime();
    }

    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    public void addQuizQuestion(QuizQuestion quizQuestion) {
        this.getQuizQuestions().add(quizQuestion);
    }

    public void deleteAllQuestions() {
        while (getQuizQuestions().size()!=0) {
            QuizQuestion quizQuestion = quizQuestions.get(0);
            quizQuestions.remove(quizQuestion);
            quizQuestion.setQuiz(null);
        }
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id: " + id +
                ", lectorId:'" + lectorId + '\'' +
                ", type:'" + type + '\'' +
                ", groups:" + groups +
                ", quizAnswers:" + quizAnswers +
                ", createdAt:" + createdAt +
                '}';
    }


    public String getLastStudentHash() {

        if(this.getQuizAnswers().size() == 0)
            return "";

        return this.getQuizAnswers().get(this.getQuizAnswers().size()-1).getStudentId();
    }

    public int getStreak() {
        int streak = 0;
        for (int i = getQuizAnswers().size()-1;i>=0; i--) {
            if (!getQuizAnswers().get(i).getGuessed())
                break;
            streak++;
        }
        return streak;
    }

    public void deleteQuestionByStudentHash(String studentId) {
        for (QuizQuestion quizQuestion : quizQuestions) {
            if (quizQuestion.getStudentHash().equals(studentId)) {
                quizQuestions.remove(quizQuestion);
                break;
            }
        }
    }
}
