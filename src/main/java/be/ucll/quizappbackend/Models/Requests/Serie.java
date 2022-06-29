/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 17/02/2022 18:25
 */

package be.ucll.quizappbackend.Models.Requests;

import java.io.Serializable;
import java.util.List;

public class Serie implements Serializable {

    private int olaId;
    private String eventCode;
    private String eventSummary;
    List<Student> students;
    private double score;
    private double seen;

    public Serie() {}

    public Serie(String name, int groupId, int score) {
        this.setEventSummary(name);
        this.setOlaId(groupId);
        this.setScore(score);
    }

    public int getOlaId() {
        return olaId;
    }

    public void setOlaId(int olaId) {
        this.olaId = olaId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventSummary() {
        return eventSummary;
    }

    public void setEventSummary(String eventSummary) {
        this.eventSummary = eventSummary;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void removeStudents() {
        this.students = null;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getSeen() {
        return this.seen;
    }

    public void setSeen(double seen) {
        this.seen = seen;
    }
}
