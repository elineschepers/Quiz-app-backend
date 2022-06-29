/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 19/02/2022 21:47
 */

package be.ucll.quizappbackend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String studentHash;
    private String studentGroup;

    private int streak;
    private double percentage;
    private int timesAsked;

    private double i;
    private double ef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Quiz quiz;

    public QuizQuestion() {}

    public QuizQuestion(String studentHash, String studentGroup, int streak, double percentage,double i,double ef, int timesAsked, Quiz quiz) {
        this.studentHash = studentHash;
        this.setStudentGroup(studentGroup);
        this.streak = streak;
        this.percentage = percentage;
        this.timesAsked = timesAsked;
        this.setQuiz(quiz);
        this.i=i;
        this.ef=ef;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getInterval() {
        return i;
    }

    public double getEf() {
        return ef;
    }

    public void setEf(double ef) {
        this.ef = ef;
    }

    public void setInterval(double interval) {
        this.i = interval;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public String getStudentHash() {
        return studentHash;
    }

    public void setStudentHash(String studentHash) {
        this.studentHash = studentHash;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getTimesAsked() {
        return timesAsked;
    }

    public void setTimesAsked(int timesAsked) {
        this.timesAsked = timesAsked;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void increaseTimesAsked() {
        this.timesAsked++;
    }

    public void increaseStreak() {
        this.streak++;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "id=" + id +
                ", studentHash='" + studentHash + '\'' +
                ", studentGroup='" + studentGroup + '\'' +
                ", streak=" + streak +
                ", percentage=" + percentage +
                ", timesAsked=" + timesAsked +
                ", i=" + i +
                '}';
    }
}
