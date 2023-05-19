package pl.masters.coding.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.masters.coding.lesson.model.Lesson;
import pl.masters.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    boolean existsByTeacherAndDateBetween(Teacher teacher, LocalDateTime from, LocalDateTime to);

}
