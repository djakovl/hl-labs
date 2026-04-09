package digital.zil.hl.module1.service;

import digital.zil.hl.module1.controller.exeption.AppException;
import digital.zil.hl.module1.entity.StudentEntity;
import digital.zil.hl.module1.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentEntity> getAll() { return studentRepository.findAllByDeletedFalse(); }

    public StudentEntity getById(String id) {
        return studentRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .orElseThrow(() -> new AppException("Student not found: " + id));
    }

    public StudentEntity save(StudentEntity student) { return studentRepository.save(student); }

    public StudentEntity update(String id, StudentEntity student) {
        student.setId(UUID.fromString(id));
        return studentRepository.save(student);
    }

    public void delete(String id) {
        StudentEntity s = getById(id);
        s.setDeleted(true);
        studentRepository.save(s);
    }
}