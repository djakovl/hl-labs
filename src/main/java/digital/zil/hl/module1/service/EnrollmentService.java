package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Enrollment;
import digital.zil.hl.module1.repository.CourseRepository;
import digital.zil.hl.module1.repository.EnrollmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
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
}
