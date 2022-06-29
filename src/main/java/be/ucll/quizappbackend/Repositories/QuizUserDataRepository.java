package be.ucll.quizappbackend.Repositories;

import be.ucll.quizappbackend.Models.QuizUserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizUserDataRepository extends JpaRepository<QuizUserData,Long> {

    QuizUserData getQuizUserDataByStudentIdAndLectorId(String studentId, String lectorId);

    Long countAllByLectorIdAndStudentIdInAndActive(String lectorId, List<String> students, boolean active);

    Long countAllByLectorIdAndStudentIdInAndIGreaterThanAndActive(String lectorId, List<String> students, double interval, boolean active);

    Long countAllByLectorIdAndStudentIdInAndTimesAskedGreaterThanAndActive(String lectorId, List<String> students, int timesAsked, boolean active);

    List<QuizUserData> getQuizUserDataByActiveAndLectorId(boolean active, String lectorId);
}
