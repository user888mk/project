package pl.masters.coding.teacher.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.masters.coding.common.Language;
import pl.masters.coding.lesson.model.Lesson;
import pl.masters.coding.student.model.Student;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@SQLDelete(sql = "UPDATE teacher SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted = false")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "teacher_language", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "language")
    private Set<Language> languages;

    @OneToMany(mappedBy = "teacher")
    private Set<Student> students;

    @OneToMany(mappedBy = "teacher")
    private Set<Lesson> lessons;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
