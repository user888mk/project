package pl.masters.coding.lesson;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.masters.coding.lesson.model.command.CreateLessonCommand;
import pl.masters.coding.lesson.model.dto.LessonDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public List<LessonDto> findAll() {
        return lessonService.findAll();
    }

    @GetMapping("/{id}")
    public LessonDto findById(@PathVariable int id) {
        return lessonService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDto create(@RequestBody @Valid CreateLessonCommand command) {
        return lessonService.create(command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        lessonService.delete(id);
    }
    //mozna przez body ale to tylko bedzie jeden wierz w body
    @PatchMapping("/{id}")
    public LessonDto editDate(@PathVariable int id, @RequestParam LocalDateTime newDate) {
        return lessonService.editDate(id, newDate);
    }
}
//path variable jest zwykle czyms co definiuje zasób - konkretny
//request param - bardziej do doawania innych rzeczy, kryteria wyszukiwania, tzn jest to bardziej ogółne, wyszikiwanie po imieniu i nazwisku np nauczyciela
