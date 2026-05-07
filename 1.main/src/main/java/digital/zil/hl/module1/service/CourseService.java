package digital.zil.hl.module1.service;

import digital.zil.hl.module1.controller.exeption.AppException;
import digital.zil.hl.module1.mapper.CourseMapper;
import digital.zil.hl.module1.model.Course;
import digital.zil.hl.module1.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAll() {
        return courseRepository.findAllByDeletedFalse().stream()
                .map(CourseMapper::toModel)
                .collect(Collectors.toList());
    }

    public Course getById(String id) {
        return courseRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .map(CourseMapper::toModel)
                .orElseThrow(() -> new AppException("Course not found: " + id));
    }

    @Transactional
    public Course save(Course course) {
        if (course.getId() != null && courseRepository.existsById(course.getId())) {
            return course;
        }
        return CourseMapper.toModel(courseRepository.save(CourseMapper.toEntity(course)));
    }

    public Course update(String id, Course course) {
        course.setId(UUID.fromString(id));
        return CourseMapper.toModel(courseRepository.save(CourseMapper.toEntity(course)));
    }

    public void delete(String id) {
        var entity = courseRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .orElseThrow(() -> new AppException("Course not found: " + id));
        entity.setDeleted(true);
        courseRepository.save(entity);
    }

    public void clear() { courseRepository.clearAll(); }
}
