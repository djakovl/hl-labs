package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.entity.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, UUID> {
    List<EnrollmentEntity> findAllByDeletedFalse();
    Optional<EnrollmentEntity> findByIdAndDeletedFalse(UUID id);
    List<EnrollmentEntity> findByCourseIdAndDeletedFalse(UUID courseId);
}
