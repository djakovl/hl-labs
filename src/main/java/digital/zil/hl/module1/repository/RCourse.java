package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.model.Course;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RCourse {

    public static final String NOT_FOUND = "Course with id %s not found";
    public static final String ALREADY_EXISTS = "Course with id %s already exists";

    private static final Map<UUID, Course> storage = new HashMap<>();

    public List<Course> findAll() {
        return storage.values().stream()
                .filter(s -> !s.isDeleted())
                .collect(java.util.stream.Collectors.toList());
    }

    public Course findById(UUID id) {
        Course c = storage.get(id);
        if (c == null) throw new RuntimeException(String.format(NOT_FOUND, id));
        return c;
    }

    public Course save(Course course) {
        if (course.getId() == null) course.setId(UUID.randomUUID());
        if (storage.containsKey(course.getId()))
            throw new RuntimeException(String.format(ALREADY_EXISTS, course.getId()));
        storage.put(course.getId(), course);
        return course;
    }

    public Course update(Course course) {
        if (!storage.containsKey(course.getId()))
            throw new RuntimeException(String.format(NOT_FOUND, course.getId()));
        storage.put(course.getId(), course);
        return course;
    }

    public void delete(UUID id) {
        if (storage.remove(id) == null)
            throw new RuntimeException(String.format(NOT_FOUND, id));
        Course s = storage.get(id);
        if (s == null) throw new RuntimeException(String.format(NOT_FOUND, id));
        s.setDeleted(true);
    }

    public void clear() { storage.clear(); }
}
