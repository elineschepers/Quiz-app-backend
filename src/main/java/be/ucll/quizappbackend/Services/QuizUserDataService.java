package be.ucll.quizappbackend.Services;

import be.ucll.quizappbackend.Models.QuizUserData;
import be.ucll.quizappbackend.Models.Requests.Student;
import be.ucll.quizappbackend.Repositories.QuizUserDataRepository;
import be.ucll.quizappbackend.Util.Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

@Service
public class QuizUserDataService {

    @Autowired
    QuizUserDataRepository repository;

    @Value("${quiz.intervalKnown}")
    private int interval;

    public QuizUserData getQuizUserDataByStudentIdAndLectorId(String studentId, String lectorId) {
        return repository.getQuizUserDataByStudentIdAndLectorId(studentId, lectorId);
    }

    public void updateQuizUserData(QuizUserData quizUserData) {
        this.repository.save(quizUserData);
    }

    public QuizUserData add(QuizUserData quizUserData) {
        return this.repository.save(quizUserData);
    }

    public boolean exists(String studentId, String lectorId) {
        return getQuizUserDataByStudentIdAndLectorId(studentId, lectorId) != null;
    }

    public Long countAllByLectorIdAndStudentIdInAndIsActive(String lectorId, List<String> students) {
        return repository.countAllByLectorIdAndStudentIdInAndActive(lectorId, students, true);
    }

    public Long countAllByLectorIdAndStudentIdInAndIGreaterThanAndIsActive(String lectorId, List<String> students, double interval) {
        return repository.countAllByLectorIdAndStudentIdInAndIGreaterThanAndActive(lectorId, students, interval, true);
    }

    public Long countAllByLectorIdAndStudentIdInAndTimesAskedGreaterThanAndIsActive(String lectorId, List<String> students, int timesAsked) {
        return this.repository.countAllByLectorIdAndStudentIdInAndTimesAskedGreaterThanAndActive(lectorId, students, timesAsked, true);
    }

    public int getPercentageKnownForStudents(List<Student> students, String lectorId) {
        List<String> hashes = new ArrayList<>();

        for (Student student : students) {
            hashes.add(Hasher.sha256(Integer.toString(student.getPersonId())));
        }

        return (int) round(((double) this.countAllByLectorIdAndStudentIdInAndIGreaterThanAndIsActive(lectorId, hashes, this.interval) /
                (double) this.countAllByLectorIdAndStudentIdInAndIsActive(lectorId, hashes)) * 100);
    }

    public double getPercentageSeenStudents(List<Student> students, String lectorId) {
        List<String> hashes = new ArrayList<>();

        for (Student student : students) {
            hashes.add(Hasher.sha256(Integer.toString(student.getPersonId())));
        }

        return (int) round(((double) this.countAllByLectorIdAndStudentIdInAndTimesAskedGreaterThanAndIsActive(lectorId, hashes, 0) /
                (double) this.countAllByLectorIdAndStudentIdInAndIsActive(lectorId, hashes)) * 100);
    }

    public void enableStudentForUser(String studentId, String lectorId) {
        QuizUserData quizUserData = this.getQuizUserDataByStudentIdAndLectorId(Hasher.sha256(studentId), lectorId);
        quizUserData.setActive(true);
        this.repository.save(quizUserData);
    }

    public void disableStudentHashForUser(String studentId, String lectorId) {
        QuizUserData quizUserData = this.getQuizUserDataByStudentIdAndLectorId(studentId, lectorId);
        quizUserData.setActive(false);
        this.repository.save(quizUserData);
    }

    public List<QuizUserData> getDisabledStudentsForLector(String lectorId) {
        return this.repository.getQuizUserDataByActiveAndLectorId(false, lectorId);
    }
}
