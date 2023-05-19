package pl.masters.coding.student.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import pl.masters.coding.common.Language;
import pl.masters.coding.lesson.model.Lesson;
import pl.masters.coding.student.model.Student;
import pl.masters.coding.teacher.model.Teacher;

import java.util.Set;

@Data
public class CreateStudentCommand {

    @NotNull(message = "VALUE_NULL")
    @Pattern(regexp = "[A-Z][a-z]{1,19}", message = "VALUE_PATTERN_NOT_MATCHED {regexp}")
    private String firstName;
    @NotNull(message = "VALUE_NULL")
    @Pattern(regexp = "[A-Z][a-z]{1,39}", message = "VALUE_PATTERN_NOT_MATCHED {regexp}")
    private String lastName;
    @NotBlank(message = "VALUE_BLANK")
    private Language language;
    @NotBlank(message = "VALUE_BLANK")
    private Teacher teacher;
    private Set<Lesson> lessons;

    public Student toEntity() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .language(language)
                .teacher(teacher)
                .build();
    }
}
