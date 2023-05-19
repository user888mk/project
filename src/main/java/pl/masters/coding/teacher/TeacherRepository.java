package pl.masters.coding.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.masters.coding.common.Language;
import pl.masters.coding.teacher.model.Teacher;

import java.util.List;


public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    List<Teacher> findAllByLanguagesContaining(Language language);
}
