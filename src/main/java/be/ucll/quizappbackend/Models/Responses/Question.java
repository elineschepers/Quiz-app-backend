/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 18/02/2022 00:22
 */

package be.ucll.quizappbackend.Models.Responses;

import java.util.List;

public class Question {

    private Long quizId;
    private int personId;
    private String img;
    private String questionType;
    private String firstname;
    private String lastname;
    private List<Choice> choices;
    private String group;

    public Question(Long quizId, int personId, String type, String firstname, String lastname, String url, String group) {
        this.setQuizId(quizId);
        this.setPersonId(personId);
        this.setQuestionType(type);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setImg(url);
        this.setGroup(group);
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
