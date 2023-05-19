package pl.masters.coding.lesson;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.masters.coding.lesson.model.command.CreateLessonCommand;
import pl.masters.coding.lesson.model.dto.LessonDto;
import pl.masters.coding.lesson.model.Lesson;
import pl.masters.coding.teacher.TeacherRepository;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    private final TeacherRepository teacherRepository;

    public List<LessonDto> findAll() {
        return lessonRepository.findAll().stream()
                .map(LessonDto::fromEntity)
                .toList();
    }

    public void delete(int id) {
        LessonDto lessonDto = findById(id);
        if (lessonDto.getDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot delete lesson which already started!");
        }
        lessonRepository.deleteById(id);
    }

    public LessonDto create(CreateLessonCommand command) {
        LocalDateTime dateTime = command.getDate();
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot select a date which is in the past");
        }
        if (lessonRepository.existsByTeacherAndDateBetween(
                command.getTeacher(), dateTime.minusMinutes(59), dateTime.plusMinutes(59))) {
            throw new IllegalArgumentException("Selected data is not available for this teacher.");
        }
        return LessonDto.fromEntity(lessonRepository.save(command.toEntity()));
    }

    public LessonDto editDate(int id, LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot select a date which is in the past");
        }
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageFormat
                .format("Lesson with id={0} not found", id)));
        if (lessonRepository.existsByTeacherAndDateBetween(
                lesson.getTeacher(), date.minusMinutes(59), date.plusMinutes(59))) {
            throw new IllegalArgumentException("Selected data is not available for this teacher.");
        }
        lesson.setDate(date);
        return LessonDto.fromEntity(lessonRepository.save(lesson));
    }

    public LessonDto findById(int id) {
        return lessonRepository.findById(id)
                .map(LessonDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Lesson with id={0} not found", id)));
    }
}
