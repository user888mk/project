package pl.masters.coding.lesson.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.masters.coding.student.model.Student;
import pl.masters.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE lesson SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted = false")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Teacher teacher;

    private boolean deleted = false;

    private LocalDateTime date;
}