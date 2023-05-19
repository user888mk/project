package pl.masters.coding.student.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.masters.coding.common.Language;
import pl.masters.coding.lesson.model.Lesson;
import pl.masters.coding.teacher.model.Teacher;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@SQLDelete(sql = "UPDATE student SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Language language;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(mappedBy = "student")
    private Set<Lesson> lessons;

    private boolean deleted = false;

    @Override
    public String toString(){
        return firstName + " " + lastName;
    }
}