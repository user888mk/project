package pl.masters.coding.student;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.masters.coding.student.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findAllByTeacherId(int teacherId);
}