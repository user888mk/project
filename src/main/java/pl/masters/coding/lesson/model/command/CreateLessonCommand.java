package pl.masters.coding.lesson.model.command;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pl.masters.coding.lesson.model.Lesson;
import pl.masters.coding.student.model.Student;
import pl.masters.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

@Data
public class CreateLessonCommand {
    // TODO: 5/6/2023  nie działac na encjach -  student/teacher podmienic na id'ki

      @NotBlank(message = "VALUE_BLANK")
     private Student student;

       @NotBlank(message = "VALUE_BLANK")
      private Teacher teacher;

    @FutureOrPresent(message = "DATE_IN_PAST")
    private LocalDateTime date;

    public Lesson toEntity() {
        return Lesson.builder()
                .student(student)
                .teacher(teacher)
                .date(date)
                .build();
    }
}
