package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Student;
import digital.zil.hl.module1.repository.StudentRepository;

import java.util.List;
import java.util.UUID;

public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAll() { return studentRepository.findAll(); }

    public Student getById(String id) { return studentRepository.findById(UUID.fromString(id)); }

    public Student save(Student student) { return studentRepository.save(student); }

    public Student update(String id, Student student) {
        student.setId(UUID.fromString(id));
        return studentRepository.update(student);
    }

    public void delete(String id) { studentRepository.delete(UUID.fromString(id)); }
}
