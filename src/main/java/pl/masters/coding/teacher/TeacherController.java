package pl.masters.coding.teacher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.masters.coding.teacher.model.command.CreateTeacherCommand;
import pl.masters.coding.teacher.model.dto.TeacherDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherDto> findAll() {
        return teacherService.findAll();
    }

    @GetMapping("/{id}")
    public TeacherDto findById(@PathVariable int id) {
        return teacherService.findById(id);
    }

    @PostMapping
    public TeacherDto create(@RequestBody @Valid CreateTeacherCommand command) {
        return teacherService.create(command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        teacherService.delete(id);
    }
}