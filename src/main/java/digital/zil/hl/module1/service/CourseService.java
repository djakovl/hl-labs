package digital.zil.hl.module1.service;

import digital.zil.hl.module1.controller.exeption.AppException;
import digital.zil.hl.module1.entity.CourseEntity;
import digital.zil.hl.module1.repository.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseEntity> getAll() { return courseRepository.findAllByDeletedFalse(); }

    public CourseEntity getById(String id) {
        return courseRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .orElseThrow(() -> new AppException("Course not found: " + id));
    }

    public CourseEntity save(CourseEntity course) { return courseRepository.save(course); }

    public CourseEntity update(String id, CourseEntity course) {
        course.setId(UUID.fromString(id));
        return courseRepository.save(course);
    }

    public void delete(String id) {
        CourseEntity c = getById(id);
        c.setDeleted(true);
        courseRepository.save(c);
    }
}