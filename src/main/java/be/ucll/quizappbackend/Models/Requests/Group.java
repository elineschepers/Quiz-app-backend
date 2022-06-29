package be.ucll.quizappbackend.Models.Requests;

import javax.persistence.*;

public class Group {
    @Id
    private int id;
    private String courseName;
    private String classGroupName;

    public Group() {}

    public Group(int id, String courseName, String classGroupName) {
        setId(id);
        setCourseName(courseName);
        setClassGroupName(classGroupName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassGroupName() {
        return classGroupName;
    }

    public void setClassGroupName(String classGroupName) {
        this.classGroupName = classGroupName;
    }
}
