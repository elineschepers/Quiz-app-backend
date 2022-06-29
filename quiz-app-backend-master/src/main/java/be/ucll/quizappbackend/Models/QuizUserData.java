package be.ucll.quizappbackend.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class QuizUserData implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String lectorId;
    private String studentId;
    private boolean active;
    private int timesAsked;
    //starting at 0
    private double i;
    //starting at 2.5
    private double ef;

    public QuizUserData() {}

    public QuizUserData(String studentId, String lectorId) {
        setLectorId(lectorId);
        setStudentId(studentId);
        setActive(true);
        setInterval(0);
        setEf(2.5);
    }

    public Long getId() {
        return id;
    }

    public String getLectorId() {
        return lectorId;
    }

    public void setLectorId(String user) {
        this.lectorId = user;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTimesAsked() {
        return timesAsked;
    }

    public void setTimesAsked(int timesAsked) {
        this.timesAsked = timesAsked;
    }

    public double getInterval() {
        return i;
    }

    public void setInterval(double interval) {
        this.i = interval;
    }

    public double getEf() {
        return ef;
    }

    public void setEf(double ef) {
        this.ef = ef;
    }
}
