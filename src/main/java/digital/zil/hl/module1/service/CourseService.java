package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Course;
import digital.zil.hl.module1.repository.CourseRepository;

import java.util.List;
import java.util.UUID;

public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAll() { return courseRepository.findAll(); }

    public Course getById(String id) { return courseRepository.findById(UUID.fromString(id)); }

    public Course save(Course course) { return courseRepository.save(course); }

    public Course update(String id, Course course) {
        course.setId(UUID.fromString(id));
        return courseRepository.update(course);
    }

    public void delete(String id) { courseRepository.delete(UUID.fromString(id)); }
}
