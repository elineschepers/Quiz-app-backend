/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 17/02/2022 11:41
 */

package be.ucll.quizappbackend.Services;

import be.ucll.quizappbackend.Models.Requests.Course;
import be.ucll.quizappbackend.Models.Requests.Serie;
import be.ucll.quizappbackend.Models.Requests.Student;
import be.ucll.quizappbackend.Util.Hasher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UcllService {

    private final String mockUrl = "http://mocker:3030/student/my";
    private final String apiUrl = "https://papi.q.ucll.be/student/my";

    /**
     * Fetch all students from a given group and cache it to save on server resources
     *
     * @param group the id of the group to be fetched
     * @return a map of all students where the key is the hash of their uuid
     */
    @Cacheable("studentsByGroup")
    public Map<String, Student> fetchStudentsByGroup(String token, String group) {
        List<Student> students = new ArrayList<>();
        for (Course course : fetchCourses(token).values()) {
            for(Serie serie : course.getEventsOfOla()) {
                if (serie.getEventSummary().equals(group)) {
                    students = serie.getStudents();
                }
            }
        }
        Map<String, Student> studentMap = new HashMap<>();
        for(Student student: students){
            studentMap.put(Hasher.sha256(Integer.toString(student.getPersonId())),student);
        }
        return studentMap;
    }

    /**
     * Fetch all students and cache it in a map to save on server resources
     *
     * @return a map of all students where the key is the hash of their uuid
     */
    @Cacheable("students")
    public Map<String, Student> fetchStudents(String token) {
        Map<String, Student> studentMap = new HashMap<>();

        for(Course course : requestInfoFromUcll(token)) {
            for(Serie serie : course.getEventsOfOla()) {
                for (Student student : serie.getStudents()) {
                    student.setGroup(serie.getEventSummary());
                    studentMap.put(Hasher.sha256(Integer.toString(student.getPersonId())), student);
                }
            }
        }
        return studentMap;
    }

    /**
     * Fetch all courses available to this user and inject some statistics
     *
     * @param token OAuth token
     * @return A map of courses with their id as a key
     */
    @Cacheable("courses")
    public Map<Integer, Course> fetchCourses(String token) {
        Map<Integer, Course> coursesMap = new HashMap<>();
        for (Course course : requestInfoFromUcll(token)) {
            coursesMap.put(course.getOlaId(), course);
        }
        return coursesMap;
    }

    /**
     * Fetch all available data for this user through the UCLL API
     *
     * @param token OAuth token
     * @return a list of courses containing series and students
     */
    public List<Course> requestInfoFromUcll(String token) {
        System.out.println("------- REQUEST TO UCLL SERVERS -------");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization" , "Bearer "+ token);
        HttpEntity<List<Course>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<List<Course>> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET,httpEntity, new ParameterizedTypeReference<>(){});

        List<Course> courses = response.getBody();
        courses.addAll(requestInfoFromMock());
        return courses;
    }

    /**
     * Inject dummy data int application
     *
     * @return a list of Mock loaded courses
     */
    public List<Course> requestInfoFromMock() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<Course>> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Course>> response = restTemplate.exchange(mockUrl,
                HttpMethod.GET,httpEntity, new ParameterizedTypeReference<>(){});

        return response.getBody();
    }

}
