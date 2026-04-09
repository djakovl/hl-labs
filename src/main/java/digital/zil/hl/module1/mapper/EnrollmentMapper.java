package digital.zil.hl.module1.mapper;

import digital.zil.hl.module1.entity.EnrollmentEntity;
import digital.zil.hl.module1.model.Enrollment;

public class EnrollmentMapper {
    private EnrollmentMapper() {}

    public static EnrollmentEntity toEntity(Enrollment model) {
        EnrollmentEntity e = new EnrollmentEntity();
        e.setId(model.getId());
        e.setStudentId(model.getStudentId());
        e.setCourseId(model.getCourseId());
        e.setEnrollmentDate(model.getEnrollmentDate());
        e.setStatus(model.getStatus());
        return e;
    }

    public static Enrollment toModel(EnrollmentEntity e) {
        Enrollment m = new Enrollment();
        m.setId(e.getId());
        m.setStudentId(e.getStudentId());
        m.setCourseId(e.getCourseId());
        m.setEnrollmentDate(e.getEnrollmentDate());
        m.setStatus(e.getStatus());
        return m;
    }
}
