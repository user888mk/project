package pl.masters.coding.student;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.masters.coding.student.model.Student;
import pl.masters.coding.student.model.dto.StudentDto;
import pl.masters.coding.student.model.command.CreateStudentCommand;
import pl.masters.coding.teacher.TeacherRepository;
import pl.masters.coding.teacher.model.Teacher;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public List<StudentDto> findAll() {
        return studentRepository.findAll().stream()
                .map(StudentDto::fromEntity)
                .toList();
    }

    public void delete(int id) {
        studentRepository.deleteById(id);
    }

    public StudentDto create(CreateStudentCommand command) {
        if (!command.getTeacher().getLanguages().contains(command.getLanguage())) {
            throw new IllegalArgumentException("This teacher does not teach this language");
        }
        return StudentDto.fromEntity(studentRepository.save(command.toEntity()));
    }

    public List<StudentDto> findAllByTeacherId(int teacherId) {
        return studentRepository.findAllByTeacherId(teacherId)
                .stream()
                .map(StudentDto::fromEntity)
                .toList();
    }

    public StudentDto editTeacher(int studentId, int teacherId) {
        Teacher newTeacher = teacherRepository.findById(teacherId).
                orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} has not been found", teacherId)));
        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} has not been found", studentId)));
        student.setTeacher(newTeacher);
        return StudentDto.fromEntity(studentRepository.save(student));
    }

    public StudentDto findById(int studentId) {
        return StudentDto.fromEntity(studentRepository.findById(studentId).
                orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} has not been found", studentId))));
    }
}