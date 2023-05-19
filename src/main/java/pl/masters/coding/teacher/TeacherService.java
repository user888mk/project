package pl.masters.coding.teacher;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.masters.coding.common.Language;
import pl.masters.coding.teacher.model.command.CreateTeacherCommand;
import pl.masters.coding.teacher.model.dto.TeacherDto;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public List<TeacherDto> findAll() {
        return teacherRepository.findAll().stream()
                .map(TeacherDto::fromEntity)
                .toList();
    }

    public void delete(int id) {
        teacherRepository.deleteById(id);
    }

    public TeacherDto create(CreateTeacherCommand command) {
        return TeacherDto.fromEntity(teacherRepository.save(command.toEntity()));
    }

    public TeacherDto findById(int id) {
        return teacherRepository.findById(id)
                .map(TeacherDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} not found", id)));
    }

    public List<TeacherDto> findAllByLanguage(Language language) {
        return teacherRepository.findAllByLanguagesContaining(language).stream()
                .map(TeacherDto::fromEntity)
                .toList();
    }
}
