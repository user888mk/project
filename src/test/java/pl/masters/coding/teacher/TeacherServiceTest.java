package pl.masters.coding.teacher;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.masters.coding.common.Language;
import pl.masters.coding.teacher.model.Teacher;
import pl.masters.coding.teacher.model.command.CreateTeacherCommand;
import pl.masters.coding.teacher.model.dto.TeacherDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //zaprzęga do pracy Mockito (adnotacje @Mock oraz @InjectMocks)
class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @Captor
    private ArgumentCaptor<Teacher> teacherArgumentCaptor;

    @Test
    void testFindAll_ResultsInTeacherDtoListBeingReturned() {
        //given
        Teacher teacher = Teacher.builder().build();
        TeacherDto dto = TeacherDto.fromEntity(Teacher.builder().build());
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        //when
        List<TeacherDto> returned = teacherService.findAll();

        //then
        assertEquals(dto.getFirstName(), returned.get(0).getFirstName());
        assertEquals(dto.getLastName(), returned.get(0).getLastName());
        assertEquals(dto.getLanguages(), returned.get(0).getLanguages());
        assertEquals(dto.getId(), returned.get(0).getId());
    }

    @Test
    void testDelete_ResultsInRepositoryDeleteBeingInvoked() {
        //given
        int teacherId = 1;

        //when
        teacherService.delete(teacherId);

        //then
        verify(teacherRepository).deleteById(teacherId);
    }

    @Test
    void testCreate_ResultsInTeacherBeingSaved() {
        //given
        CreateTeacherCommand command = new CreateTeacherCommand();
        command.setFirstName("Jan");
        command.setLastName("Kowalski");
        command.setLanguages(Set.of(Language.ENGLISH, Language.GERMAN));

        Teacher expectedTeacher = Teacher.builder()
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .languages(command.getLanguages())
                .build();

        when(teacherRepository.save(any(Teacher.class))).thenReturn(expectedTeacher);

        //when
        TeacherDto teacherDto = teacherService.create(command);

        //then
        assertEquals(expectedTeacher.getId(), teacherDto.getId());
        assertEquals(expectedTeacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(expectedTeacher.getLastName(), teacherDto.getLastName());
        assertEquals(expectedTeacher.getLanguages(), teacherDto.getLanguages());

        verify(teacherRepository).save(teacherArgumentCaptor.capture());
        Teacher saved = teacherArgumentCaptor.getValue();
        assertEquals(command.getFirstName(), saved.getFirstName());
        assertEquals(command.getLastName(), saved.getLastName());
        assertEquals(command.getLanguages(), saved.getLanguages());
    }

    @Test
    void testFindById_ResultsInExceptionBeingThrown() {
        //given
        int id = 1;
        String exceptionMsg = "Teacher with id=1 not found";
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> teacherService.findById(id))
                .withMessage(exceptionMsg);

        verify(teacherRepository).findById(id);
    }

    @Test
    void testFindById_ResultsInTeacherDtoBeingReturned() {
        //given
        int id = 1;
        Teacher teacher = Teacher.builder().build();
        TeacherDto dto = TeacherDto.fromEntity(teacher);
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        //when
        TeacherDto returned = teacherService.findById(id);

        //then
        assertEquals(dto.getFirstName(), returned.getFirstName());
        assertEquals(dto.getLastName(), returned.getLastName());
        assertEquals(dto.getLanguages(), returned.getLanguages());
        assertEquals(dto.getId(), returned.getId());
    }


    @Test
    void testFindAllByLanguage_ResultsInTeacherDtoListBeingReturned() {
        //given
        Language language = Language.ENGLISH;
        Teacher teacher = Teacher.builder().build();
        TeacherDto dto = TeacherDto.fromEntity(Teacher.builder().build());
        when(teacherRepository.findAllByLanguagesContaining(language)).thenReturn(List.of(teacher));

        //when
        List<TeacherDto> returned = teacherService.findAllByLanguage(language);

        //then
        assertEquals(dto.getFirstName(), returned.get(0).getFirstName());
        assertEquals(dto.getLastName(), returned.get(0).getLastName());
        assertEquals(dto.getLanguages(), returned.get(0).getLanguages());
        assertEquals(dto.getId(), returned.get(0).getId());
    }
}
