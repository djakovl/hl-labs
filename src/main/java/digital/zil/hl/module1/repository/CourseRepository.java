package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {
    List<CourseEntity> findAllByDeletedFalse();
    Optional<CourseEntity> findByIdAndDeletedFalse(UUID id);
}