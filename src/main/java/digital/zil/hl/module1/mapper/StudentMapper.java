package digital.zil.hl.module1.mapper;

import digital.zil.hl.module1.entity.StudentEntity;
import digital.zil.hl.module1.model.Student;

public class StudentMapper {
    private StudentMapper() {}

    public static StudentEntity toEntity(Student model) {
        StudentEntity e = new StudentEntity();
        e.setId(model.getId());
        e.setFio(model.getFio());
        e.setStudentCard(model.getStudentCard());
        e.setEnrollmentYear(model.getEnrollmentYear());
        return e;
    }

    public static Student toModel(StudentEntity e) {
        Student s = new Student();
        s.setId(e.getId());
        s.setFio(e.getFio());
        s.setStudentCard(e.getStudentCard());
        s.setEnrollmentYear(e.getEnrollmentYear());
        return s;
    }
}
