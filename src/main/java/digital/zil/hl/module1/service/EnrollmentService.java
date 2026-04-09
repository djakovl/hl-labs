package digital.zil.hl.module1.service;

import digital.zil.hl.module1.controller.exeption.AppException;
import digital.zil.hl.module1.entity.EnrollmentEntity;
import digital.zil.hl.module1.mapper.EnrollmentMapper;
import digital.zil.hl.module1.model.Enrollment;
import digital.zil.hl.module1.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> getAll() {
        return enrollmentRepository.findAllByDeletedFalse().stream()
                .map(EnrollmentMapper::toModel)
                .collect(Collectors.toList());
    }

    public Enrollment getById(String id) {
        return enrollmentRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .map(EnrollmentMapper::toModel)
                .orElseThrow(() -> new AppException("Enrollment not found: " + id));
    }

    public Enrollment enroll(UUID studentId, UUID courseId) {
        EnrollmentEntity e = new EnrollmentEntity();
        e.setStudentId(studentId);
        e.setCourseId(courseId);
        e.setEnrollmentDate(LocalDate.now());
        e.setStatus("ACTIVE");
        return EnrollmentMapper.toModel(enrollmentRepository.save(e));
    }

    public Enrollment complete(String id) {
        var entity = enrollmentRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .orElseThrow(() -> new AppException("Enrollment not found: " + id));
        entity.setStatus("COMPLETED");
        return EnrollmentMapper.toModel(enrollmentRepository.save(entity));
    }

    public void delete(String id) {
        var entity = enrollmentRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .orElseThrow(() -> new AppException("Enrollment not found: " + id));
        entity.setDeleted(true);
        enrollmentRepository.save(entity);
    }

    public double getAverageStudentsPerCourse() {
        return enrollmentRepository.averageStudentsPerCourse();
    }
}
