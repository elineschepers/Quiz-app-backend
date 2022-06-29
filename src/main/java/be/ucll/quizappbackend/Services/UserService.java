/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 27/02/2022 18:24
 */

package be.ucll.quizappbackend.Services;

import be.ucll.quizappbackend.Models.QuizUserData;
import be.ucll.quizappbackend.Models.Requests.Course;
import be.ucll.quizappbackend.Models.Requests.Serie;
import be.ucll.quizappbackend.Models.Requests.Student;
import be.ucll.quizappbackend.Util.Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UcllService ucllService;

    @Autowired
    QuizUserDataService quizUserDataService;

    public Map<Integer, Course> fetchCoursesOfUser(String lectorId, String token) {
        Map<Integer, Course> coursesMap = new HashMap<>();
        for (Course course : ucllService.fetchCourses(token).values()) {
            for (Serie serie : course.getEventsOfOla()) {
                serie.setScore(quizUserDataService.getPercentageKnownForStudents(serie.getStudents(), lectorId));
                serie.setSeen(quizUserDataService.getPercentageSeenStudents(serie.getStudents(), lectorId));
                // Save bandwidth
                serie.removeStudents();
            }
            coursesMap.put(course.getOlaId(), course);
        }
        return coursesMap;
    }

    public void enableStudentForLector(String studentId, String lectorId) {
        quizUserDataService.enableStudentForUser(studentId, lectorId);
    }

    public void disableStudentForLector(String studentId, String lectorId) {
        quizUserDataService.disableStudentHashForUser(Hasher.sha256(studentId), lectorId);
    }

    public List<Student> getDisabledStudents(String lectorId, String token) {
        List<Student> students = new ArrayList<>();
        Map<String, Student> studentMap = ucllService.fetchStudents(token);
        List<QuizUserData> data = quizUserDataService.getDisabledStudentsForLector(lectorId);
        for (QuizUserData quizUserData : data) {
            if(studentMap.containsKey(quizUserData.getStudentId())) {
                students.add(studentMap.get(quizUserData.getStudentId()));
            }
        }
        students.sort(Comparator.comparing(Student::getFirstName));
        return students;
    }
}
