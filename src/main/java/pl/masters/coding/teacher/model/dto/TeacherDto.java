package pl.masters.coding.teacher.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.masters.coding.common.Language;
import pl.masters.coding.teacher.model.Teacher;

import java.util.Set;

@Getter
@Builder
public class TeacherDto {

    private int id;
    private String firstName;
    private String lastName;
    private Set<Language> languages;

    public static TeacherDto fromEntity(Teacher teacher){
        return TeacherDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .languages(teacher.getLanguages())
                .build();
    }
}
