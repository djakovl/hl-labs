package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Enrollment;
import digital.zil.hl.module1.repository.CourseRepository;
import digital.zil.hl.module1.repository.EnrollmentRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Enrollment> getAll() { return enrollmentRepository.findAll(); }

    public Enrollment getById(String id) { return enrollmentRepository.findById(UUID.fromString(id)); }

    public Enrollment enroll(UUID studentId, UUID courseId) {
        Enrollment e = new Enrollment(null, studentId, courseId, LocalDate.now(), "ACTIVE");
        return enrollmentRepository.save(e);
    }

    public Enrollment complete(String id) {
        Enrollment e = enrollmentRepository.findById(UUID.fromString(id));
        e.setStatus("COMPLETED");
        return enrollmentRepository.save(e);
    }

    public void delete(String id) { enrollmentRepository.delete(UUID.fromString(id)); }

    public double getAverageStudentsPerCourse() {
        return enrollmentRepository.averageStudentsPerCourse();
    }

    public Map<String, Long> getStatsPerCourse() {
        Map<UUID, Long> perCourse = enrollmentRepository.studentsPerCourse();
        Map<String, Long> result = new LinkedHashMap<>();
        perCourse.forEach((courseId, count) -> {
            try {
                String courseName = courseRepository.findById(courseId).getName();
                result.put(courseName, count);
            } catch (RuntimeException e) {
                result.put(courseId.toString(), count);
            }
        });
        return result;
    }
}
