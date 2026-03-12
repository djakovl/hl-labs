package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Course;
import digital.zil.hl.module1.repository.RCourse;

import java.util.List;
import java.util.UUID;

public class SCourse {

    private final RCourse rCourse;

    public SCourse(RCourse rCourse) {
        this.rCourse = rCourse;
    }

    public List<Course> getAll() { return rCourse.findAll(); }

    public Course getById(String id) { return rCourse.findById(UUID.fromString(id)); }

    public Course save(Course course) { return rCourse.save(course); }

    public Course update(String id, Course course) {
        course.setId(UUID.fromString(id));
        return rCourse.update(course);
    }

    public void delete(String id) { rCourse.delete(UUID.fromString(id)); }
}
