package be.ucll.quizappbackend.Models.Requests;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;

@RedisHash("Student")
public class Student implements Serializable {

    @Id 
    private int personId;
    private String lastName;
    private String firstName;
    private String photoUrl;
    private String group;

    public Student(int personId, String firstName, String lastName, String photoUrl){
        setPersonId(personId);
        setFirstName(firstName);
        setLastName(lastName);
        setPhotoUrl(photoUrl);
    }

    public Student(){}

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
