package pl.masters.coding.student;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.masters.coding.common.Language;
import pl.masters.coding.student.model.Student;
import pl.masters.coding.student.model.dto.StudentDto;
import pl.masters.coding.student.model.command.CreateStudentCommand;
import pl.masters.coding.teacher.TeacherRepository;
import pl.masters.coding.teacher.model.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;

    @Test
    void testFindAll_ResultsInStudentDtoListBeingReturned() {
        //given
        Student student = new Student();

        //when
        when(studentRepository.findAll()).thenReturn(List.of(student));
        List<StudentDto> students = studentService.findAll();

        //then
        verify(studentRepository).findAll();
        assertEquals(student.getId(), students.get(0).getId());
        assertEquals(student.getFirstName(), students.get(0).getFirstName());
        assertEquals(student.getLastName(), students.get(0).getLastName());
        assertEquals(student.getLanguage(), students.get(0).getLanguage());
    }

    @Test
    void testDelete_ResultsInRepositoryDeleteBeingInvoked() {
        //given
        int studentId = 1;

        //when
        studentService.delete(studentId);

        //then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void testCreate_shouldSaveStudentAndReturnDto() {
        //given
        CreateStudentCommand command = new CreateStudentCommand();
        command.setFirstName("John");
        command.setLastName("Doe");
        command.setLanguage(Language.ENGLISH);

        Teacher teacher = new Teacher();
        teacher.setLanguages(Set.of(Language.ENGLISH));
        command.setTeacher(teacher);

        Student student = Student.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .language(Language.ENGLISH)
                .teacher(teacher)
                .build();
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        StudentDto expectedDto = StudentDto.fromEntity(student);

        //when
        StudentDto actualDto = studentService.create(command);

        //then
        assertEquals(actualDto.getFirstName(), expectedDto.getFirstName()); // TODO: 5/6/2023 zamienić miejscami
        assertEquals(actualDto.getLastName(), expectedDto.getLastName());
        assertEquals(actualDto.getLanguage(), expectedDto.getLanguage());
        assertEquals(actualDto.getId(), expectedDto.getId());
    }

    @Test
    void testCreate_shouldThrowExceptionWhenTeacherDoesNotTeachStudentLanguage() {
        //given
        CreateStudentCommand command = new CreateStudentCommand();
        command.setFirstName("John");
        command.setLastName("Doe");
        command.setLanguage(Language.SPANISH);

        Teacher teacher = new Teacher();
        teacher.setLanguages(Set.of(Language.ENGLISH));
        command.setTeacher(teacher);

        //then
        assertThrows(IllegalArgumentException.class, () -> studentService.create(command));
        // TODO: 5/6/2023 zweryfikować komunikat błędu
    }

    // TODO: 5/6/2023 przypadek create gdzie nie znaleziono nauczyciela

    @Test
    void testFindAllByTeacherId_ResultsInStudentListBeingReturned() {
        //given
        int teacherId = 1;
        List<Student> studentsFromRepo = List.of(new Student());

        //when
        when(studentRepository.findAllByTeacherId(teacherId)).thenReturn(studentsFromRepo);
        List<StudentDto> returned = studentService.findAllByTeacherId(teacherId);

        //then
        assertEquals(studentsFromRepo.get(0).getFirstName(), returned.get(0).getFirstName());
        assertEquals(studentsFromRepo.get(0).getLastName(), returned.get(0).getLastName());
        assertEquals(studentsFromRepo.get(0).getLanguage(), returned.get(0).getLanguage());
        assertEquals(studentsFromRepo.get(0).getId(), returned.get(0).getId());
        verify(studentRepository).findAllByTeacherId(teacherId);
    }

    @Test
    void testEditTeacher_ResultsInNewTeacherForStudentBeingSet() {
        //given
        int id = 1;
        Student student = Student.builder().build();
        Teacher newTeacher = Teacher.builder().build();

        //when
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(teacherRepository.findById(id)).thenReturn(Optional.of(newTeacher));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.editTeacher(id, id);

        //then
        verify(studentRepository).findById(id);
        verify(teacherRepository).findById(id);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student saved = studentArgumentCaptor.getValue();
        assertEquals(newTeacher, saved.getTeacher());
    }



    @Test
    void testEditTeacher_ResultsInExceptionBeingThrownWhenStudentNotFound() {
        //given
        int studentId = 1;
        int teacherId = 2;
        String expectedMessage = "Student with id=" + studentId + " has not been found";
        Teacher newTeacher = new Teacher();
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(newTeacher));

        //when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> studentService.editTeacher(studentId, teacherId));

        assertEquals(expectedMessage, exception.getMessage());
        verify(studentRepository).findById(studentId);
    }

    @Test
    void testEditTeacher_ResultsInExceptionBeingThrownWhenTeacherNotFound() {
        //given
        int studentId = 1;
        int teacherId = 2;
        String expectedMessage = "Teacher with id=" + teacherId + " has not been found";
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> studentService.editTeacher(studentId, teacherId));

        assertEquals(expectedMessage, exception.getMessage());
        verify(teacherRepository).findById(teacherId);
        verifyNoInteractions(studentRepository);
    }
    // TODO: 5/6/2023 przypadek editTeacher gdzie języki nauczyciela nie obejmują języka studenta

    @Test
    void testFindById_ResultsInStudentBeingReturned() {
        //given
        int studentId = 1;
        Student student = new Student();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        StudentDto repoDto = StudentDto.fromEntity(student);

        //when
        StudentDto returned = studentService.findById(studentId);

        //then
        assertEquals(repoDto.getFirstName(), returned.getFirstName());
        assertEquals(repoDto.getLastName(), returned.getLastName());
        assertEquals(repoDto.getLanguage(), returned.getLanguage());
        assertEquals(repoDto.getId(), returned.getId());
        verify(studentRepository).findById(studentId);
    }

    @Test
    void testFindStudentById_ResultsInExceptionBeingThrown() {
        //given
        int studentId = 1;
        String expectedMessage = "Student with id=" + studentId + " has not been found";

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> studentService.findById(studentId));
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(studentRepository).findById(studentId);
    }

    // TODO: 5/6/2023 captory gdzie trzeba
}