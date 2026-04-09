package digital.zil.hl.module1.service;

import digital.zil.hl.module1.controller.exeption.AppException;
import digital.zil.hl.module1.entity.EnrollmentEntity;
import digital.zil.hl.module1.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<EnrollmentEntity> getAll() { return enrollmentRepository.findAllByDeletedFalse(); }

    public EnrollmentEntity getById(String id) {
        return enrollmentRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .orElseThrow(() -> new AppException("Enrollment not found: " + id));
    }

    public EnrollmentEntity enroll(UUID studentId, UUID courseId) {
        EnrollmentEntity e = new EnrollmentEntity();
        e.setStudentId(studentId);
        e.setCourseId(courseId);
        e.setEnrollmentDate(LocalDate.now());
        e.setStatus("ACTIVE");
        return enrollmentRepository.save(e);
    }

    public EnrollmentEntity complete(String id) {
        EnrollmentEntity e = getById(id);
        e.setStatus("COMPLETED");
        return enrollmentRepository.save(e);
    }

    public void delete(String id) {
        EnrollmentEntity e = getById(id);
        e.setDeleted(true);
        enrollmentRepository.save(e);
    }

    public double getAverageStudentsPerCourse() {
        return enrollmentRepository.averageStudentsPerCourse();
    }
}