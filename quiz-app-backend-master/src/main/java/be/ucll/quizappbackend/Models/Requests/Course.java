/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 17/02/2022 16:55
 */

package be.ucll.quizappbackend.Models.Requests;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash("Course")
public class Course implements Serializable {

    @Id
    private int olaId;
    private String olaSummary;
    private List<Serie> eventsOfOla;

    public Course() {}

    public Course(String courseName) {
        this.setOlaSummary(courseName);
        this.setEventsOfOla(new ArrayList<>());
    }

    public int getOlaId() {
        return olaId;
    }

    public void setOlaId(int olaId) {
        this.olaId = olaId;
    }

    public String getOlaSummary() {
        return olaSummary;
    }

    public void setOlaSummary(String olaSummary) {
        this.olaSummary = olaSummary;
    }

    public List<Serie> getEventsOfOla() {
        return eventsOfOla;
    }

    public void setEventsOfOla(List<Serie> eventsOfOla) {
        this.eventsOfOla = eventsOfOla;
    }

    public void addSeries(Serie serie) {
        this.getEventsOfOla().add(serie);
    }
}
