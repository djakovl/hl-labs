package digital.zil.hl.module1.mapper;

import digital.zil.hl.module1.entity.CourseEntity;
import digital.zil.hl.module1.model.Course;

public class CourseMapper {
    private CourseMapper() {}

    public static CourseEntity toEntity(Course model) {
        CourseEntity e = new CourseEntity();
        e.setId(model.getId());
        e.setCode(model.getCode());
        e.setName(model.getName());
        e.setTeacher(model.getTeacher());
        e.setCredits(model.getCredits());
        return e;
    }

    public static Course toModel(CourseEntity e) {
        Course c = new Course();
        c.setId(e.getId());
        c.setCode(e.getCode());
        c.setName(e.getName());
        c.setTeacher(e.getTeacher());
        c.setCredits(e.getCredits());
        return c;
    }
}
