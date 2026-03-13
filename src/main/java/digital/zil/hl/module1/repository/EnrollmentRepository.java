package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EnrollmentRepository {

    public static final String NOT_FOUND = "Enrollment with id %s not found";

    private static final Map<UUID, Enrollment> storage = new HashMap<>();

    public List<Enrollment> findAll() {
        return storage.values().stream()
                .filter(s -> !s.isDeleted())
                .collect(java.util.stream.Collectors.toList());
    }

    public Enrollment findById(UUID id) {
        Enrollment e = storage.get(id);
        if (e == null) throw new RuntimeException(String.format(NOT_FOUND, id));
        return e;
    }

    public List<Enrollment> findByCourseId(UUID courseId) {
        return storage.values().stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getId() == null) enrollment.setId(UUID.randomUUID());
        storage.put(enrollment.getId(), enrollment);
        return enrollment;
    }

    public void delete(UUID id) {
        if (storage.remove(id) == null)
            throw new RuntimeException(String.format(NOT_FOUND, id));
        Enrollment s = storage.get(id);
        if (s == null) throw new RuntimeException(String.format(NOT_FOUND, id));
        s.setDeleted(true);
    }

    // Среднее количество студентов на курсе за всё время
    public double averageStudentsPerCourse() {
        if (storage.isEmpty()) return 0.0;

        // Группируем ВСЕ записи (включая deleted/completed) по курсу
        Map<UUID, Long> perCourse = storage.values().stream()
                .collect(Collectors.groupingBy(
                        Enrollment::getCourseId,
                        Collectors.counting()
                ));

        return perCourse.values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }


    public void clear() { storage.clear(); }
}
