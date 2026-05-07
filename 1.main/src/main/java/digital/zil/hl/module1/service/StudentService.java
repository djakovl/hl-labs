package digital.zil.hl.module1.service;

import digital.zil.hl.module1.controller.exeption.AppException;
import digital.zil.hl.module1.mapper.StudentMapper;
import digital.zil.hl.module1.model.Student;
import digital.zil.hl.module1.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAll() {
        return studentRepository.findAllByDeletedFalse().stream()
                .map(StudentMapper::toModel)
                .collect(Collectors.toList());
    }

    public Student getById(String id) {
        return studentRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .map(StudentMapper::toModel)
                .orElseThrow(() -> new AppException("Student not found: " + id));
    }

    @Transactional
    public Student save(Student student) {
        if (student.getId() != null && studentRepository.existsById(student.getId())) {
            return student;
        }
        return StudentMapper.toModel(studentRepository.save(StudentMapper.toEntity(student)));
    }
    
    public Student update(String id, Student student) {
        student.setId(UUID.fromString(id));
        return StudentMapper.toModel(studentRepository.save(StudentMapper.toEntity(student)));
    }

    public void delete(String id) {
        var entity = studentRepository.findByIdAndDeletedFalse(UUID.fromString(id))
                .orElseThrow(() -> new AppException("Student not found: " + id));
        entity.setDeleted(true);
        studentRepository.save(entity);
    }
    
    public void clear() { studentRepository.clearAll(); }
}
