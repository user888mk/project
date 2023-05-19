package pl.masters.coding.lesson.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.masters.coding.lesson.model.Lesson;
import java.time.LocalDateTime;

@Getter
@Builder
public class LessonDto {

    private int id;

    private LocalDateTime date;

    public static LessonDto fromEntity(Lesson lesson) {
        return LessonDto.builder()
                .id(lesson.getId())
                .date(lesson.getDate())
                .build();
    }
}
