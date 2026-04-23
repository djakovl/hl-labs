package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {
    List<StudentEntity> findAllByDeletedFalse();
    Optional<StudentEntity> findByIdAndDeletedFalse(UUID id);
    
    
    @Modifying  
    @Query("DELETE FROM StudentEntity s")
    void clearAll();
}