package be.ucll.quizappbackend.Models;


import be.ucll.quizappbackend.Util.Hasher;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class QuizAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Quiz quiz;

    private String groupId;

    private Boolean guessed;

    public QuizAnswer(String userId) {
        this.setStudentId(userId);
    }

    public QuizAnswer() {}

    public Boolean getGuessed() {
        return guessed;
    }

    public void setGuessed(Boolean guessed) {
        this.guessed = guessed;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String userId) {
        this.studentId = Hasher.sha256(userId);
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QuizAnswer{" +
                "id=" + id +
                ", userId='" + studentId + '\'' +
                ", quiz=" + quiz +
                ", guessed=" + guessed +
                '}';
    }
}
