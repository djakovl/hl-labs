package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Student;
import digital.zil.hl.module1.repository.RStudent;

import java.util.List;
import java.util.UUID;

public class SStudent {

    private final RStudent rStudent;

    public SStudent(RStudent rStudent) {
        this.rStudent = rStudent;
    }

    public List<Student> getAll() { return rStudent.findAll(); }

    public Student getById(String id) { return rStudent.findById(UUID.fromString(id)); }

    public Student save(Student student) { return rStudent.save(student); }

    public Student update(String id, Student student) {
        student.setId(UUID.fromString(id));
        return rStudent.update(student);
    }

    public void delete(String id) { rStudent.delete(UUID.fromString(id)); }
}
