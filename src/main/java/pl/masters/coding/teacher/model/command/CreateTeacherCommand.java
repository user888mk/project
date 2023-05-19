package pl.masters.coding.teacher.model.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import pl.masters.coding.common.Language;
import pl.masters.coding.teacher.model.Teacher;

import java.util.Set;

@Data
public class CreateTeacherCommand {

//    @NotBlank(message = "VALUE_BLANK")
    @NotNull(message = "VALUE_NULL")
    @Pattern(regexp = "[A-Z][a-z]{1,19}", message = "VALUE_PATTERN_NOT_MATCHED {regexp}")
    private String firstName;

//    @NotBlank(message = "VALUE_BLANK")
    @NotNull(message = "VALUE_NULL")
    @Pattern(regexp = "[A-Z][a-z]{1,39}", message = "VALUE_PATTERN_NOT_MATCHED {regexp}")
    private String lastName;

    @NotEmpty(message = "VALUE_EMPTY")
    private Set<Language> languages;

    public Teacher toEntity() {
        return Teacher.builder()
                .firstName(firstName)
                .lastName(lastName)
                .languages(languages)
                .build();
    }
}