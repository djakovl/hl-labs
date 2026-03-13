package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.model.Student;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StudentRepository {

    public static final String NOT_FOUND = "Student with id %s not found";
    public static final String ALREADY_EXISTS = "Student with id %s already exists";

    private static final Map<UUID, Student> storage = new HashMap<>();

    public List<Student> findAll() {
        return storage.values().stream()
                .filter(s -> !s.isDeleted())
                .collect(java.util.stream.Collectors.toList());
    }

    public Student findById(UUID id) {
        Student s = storage.get(id);
        if (s == null || s.isDeleted()) throw new RuntimeException(String.format(NOT_FOUND, id));
        return s;
    }

    public Student save(Student student) {
        if (student.getId() == null) student.setId(UUID.randomUUID());
        if (storage.containsKey(student.getId()))
            throw new RuntimeException(String.format(ALREADY_EXISTS, student.getId()));
        storage.put(student.getId(), student);
        return student;
    }

    public Student update(Student student) {
        if (!storage.containsKey(student.getId()))
            throw new RuntimeException(String.format(NOT_FOUND, student.getId()));
        storage.put(student.getId(), student);
        return student;
    }

    public void delete(UUID id) {
        Student s = storage.get(id);
        if (s == null) throw new RuntimeException(String.format(NOT_FOUND, id));
        s.setDeleted(true);
    }

    public void clear() { storage.clear(); }
}
