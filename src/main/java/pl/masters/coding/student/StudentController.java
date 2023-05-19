package pl.masters.coding.student;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.masters.coding.student.model.dto.StudentDto;
import pl.masters.coding.student.model.command.CreateStudentCommand;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public StudentDto findById(@PathVariable int id) {
        return studentService.findById(id);
    }

    @PostMapping
    public StudentDto create(@RequestBody @Valid CreateStudentCommand command) {
        return studentService.create(command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        studentService.delete(id);
    }

    @GetMapping(params = "teacherId")
    public List<StudentDto> findAllByTeacherId(@RequestParam int teacherId) {
        return studentService.findAllByTeacherId(teacherId);
    }

    @PatchMapping(value = "/{studentId}", params = "teacherId")
    public StudentDto editTeacher(@PathVariable int studentId, @RequestParam int teacherId) {
        return studentService.editTeacher(studentId, teacherId);
    }
}