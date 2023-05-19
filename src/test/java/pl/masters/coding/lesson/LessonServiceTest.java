package pl.masters.coding.lesson;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.masters.coding.lesson.model.Lesson;
import pl.masters.coding.lesson.model.command.CreateLessonCommand;
import pl.masters.coding.lesson.model.dto.LessonDto;
import pl.masters.coding.student.model.Student;
import pl.masters.coding.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @Test
    void testFindAll_ResultsInLessonsDtoBeingReturned() {
        //given
        Lesson lesson = Lesson.builder().build();

        //when
        when(lessonRepository.findAll()).thenReturn(List.of(lesson));
        List<LessonDto> returned = lessonService.findAll();

        //then
        assertEquals(lesson.getDate(), returned.get(0).getDate());
        assertEquals(lesson.getId(), returned.get(0).getId());
    }

    @Test
    void testDelete_ResultsInDateExceptionIllegalArgumentException() {
        //given
        int id = 1;
        Lesson lesson = Mockito.mock(Lesson.class);
        when(lesson.getDate()).thenReturn(LocalDateTime.now().minusDays(1));
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        //then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.delete(id));
        assertEquals("Cannot delete lesson which already started!", exception.getMessage());
    }

    @Test
    void testDelete_ResultsInRepositoryDeleteBeingInvoked() {
        //given
        int id = 1;
        Lesson lesson = Lesson.builder()
                .date(LocalDateTime.now().plusDays(1))
                .build();

        //when
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
        lessonService.delete(id);

        //then
        verify(lessonRepository).deleteById(id);
    }

    // write tests - happy path - to delete method
    //describe below method
    //testDelete_ResultsInRepositoryDeleteBeingInvoked_whenLessonIsInTheFuture
    //testDelete_ResultsInLessonExceptionBeingThrown_whenLessonIsInThePast
    //testDelete_ResultsInLessonExceptionBeingThrown_whenLessonDoesNotExist
    //testCreate_ResultsInLessonBeingSaved_whenLessonIsInTheFuture
    //testCreate_ResultsInLessonExceptionBeingThrown_whenLessonIsInThePast


    @Test
    void testDelete_ResultsInRepositoryDeleteBeingInvoked_whenLessonIsInTheFuture() {
        //given
        int id = 1;
        Lesson lesson = Lesson.builder()
                .date(LocalDateTime.now().plusDays(1))
                .build();

        //when
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
        lessonService.delete(id);

        //then
        verify(lessonRepository).deleteById(id);
    }
//    @Test
//    void testDelete_ResultsInLessonExceptionBeingThrown() {
//        //given
//        int id = 1;
//        when(lessonRepository.findById(id)).thenReturn(Optional.empty());
//
//        //then
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> lessonService.delete(id));
//        assertEquals("Lesson with id=" + id + " not found", exception.getMessage());
//    }

    @Test
    void testCreate_ResultsInLessonBeingCreated() {
        //given
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        Teacher teacher = Teacher.builder().build();
        Student student = Student.builder().build();

        CreateLessonCommand command = new CreateLessonCommand();
        command.setDate(date);
        command.setStudent(student);
        command.setTeacher(teacher);

        //when
        when(lessonRepository.save(any(Lesson.class))).thenReturn(command.toEntity());
        LessonDto returned = lessonService.create(command);

        //then
        assertEquals(date, returned.getDate());
    }

    @Test
    void testCreate_DateInThePast_ResultsIllegalArgumentException() {
        //given
        CreateLessonCommand command = new CreateLessonCommand();
        command.setDate(LocalDateTime.now().minusDays(1));

        //then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.create(command));
        assertEquals("Cannot select a date which is in the past", exception.getMessage());
    }

    // TODO: 5/6/2023 dopisac test kiedy nie mamy dodanego studenta
    @Test
    void testCreate_DateInThePast_ResultsIllegalArgumentException_whenStudentIsNull() {
        //given
        CreateLessonCommand command = new CreateLessonCommand();
        command.setStudent(null);
        command.setDate(LocalDateTime.now().minusDays(1));
        //then

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.create(command));
        assertEquals("Cannot select a date which is in the past", exception.getMessage());
    }

    // TODO: 5/6/2023 dopisac test kiedy nie mamy dodanego teachera
    @Test
    void testCreate_DateInThePast_ResultsIllegalArgumentException_whenTeacherIsNull() {
        //given
        CreateLessonCommand command = new CreateLessonCommand();
        command.setTeacher(null);
        command.setDate(LocalDateTime.now().minusDays(1));
        //then

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.create(command));
        assertEquals("Cannot select a date which is in the past", exception.getMessage());
    }

    // TODO: 5/6/2023 przypadek testowy jak sie terminy pokrywaja (nauczyciela)
    @Test
    void testCreate_TheSameTerms_ResultsIllegalArgumentException_whenTeacherIsNull() {
        //given
        CreateLessonCommand command = new CreateLessonCommand();
        command.setTeacher(null);
        command.setDate(LocalDateTime.now().minusDays(1));
        //then

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.create(command));
        assertEquals("Cannot select a date which is in the past", exception.getMessage());
    }

    @Test
    void testEditDate_ResultsInDateBeingEdited() {
        //given
        int id = 1;
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        Lesson lesson = Lesson.builder().build();

        //when
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        lessonService.editDate(id, newDate);

        //then
        assertEquals(newDate, lesson.getDate());
    }

    @Test
    void testEditDate_DateInThePast_ResultsInIllegalArgumentException() {
        //given
        int id = 1;
        LocalDateTime newDate = LocalDateTime.now().minusDays(1);

        //then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.editDate(id, newDate));
        assertEquals("Cannot select a date which is in the past", exception.getMessage());
    }

    // TODO: 5/6/2023 termin sie pokrywa

    @Test
    void testEditDate_ResultsInLessonExceptionBeingThrown() {   // TODO: 5/6/2023 nazwa do zmiany
        //given
        int id = 1;
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> lessonService.editDate(id, newDate));
        assertEquals("Lesson with id=" + id + " not found", exception.getMessage());
    }

    @Test
    void testFindById_ResultsInLessonExceptionBeingThrown() {   // TODO: 5/6/2023 zmiana nazwy
        //given
        int id = 1;
        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> lessonService.delete(id));
        assertEquals("Lesson with id=" + id + " not found", exception.getMessage());
    }

    // TODO: 5/6/2023 test findById happyPath

    @Test
    void findById_ResultsInLessonBeingFound() {
        //given
        int id = 1;
        Lesson lesson = Lesson.builder().build();
        //when
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
        LessonDto returned = lessonService.findById(id);
        //then
        assertEquals(lesson.getDate(), returned.getDate());
        assertEquals(lesson.getId(), returned.getId());
    }


    //todo dopisac testy do delete, przetestowac inne mozliwosci metody delete

    // TODO: 5/6/2023 captory gdzie trzeba
    //mock mvc

}