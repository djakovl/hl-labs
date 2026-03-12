package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Enrollment;
import digital.zil.hl.module1.repository.REnrollment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class SEnrollment {

    private final REnrollment rEnrollment;

    public SEnrollment(REnrollment rEnrollment) {
        this.rEnrollment = rEnrollment;
    }

    public List<Enrollment> getAll() { return rEnrollment.findAll(); }

    public Enrollment getById(String id) { return rEnrollment.findById(UUID.fromString(id)); }

    public Enrollment enroll(UUID studentId, UUID courseId) {
        Enrollment e = new Enrollment(null, studentId, courseId, LocalDate.now(), "ACTIVE");
        return rEnrollment.save(e);
    }

    public Enrollment complete(String id) {
        Enrollment e = rEnrollment.findById(UUID.fromString(id));
        e.setStatus("COMPLETED");
        return rEnrollment.save(e);
    }

    public void delete(String id) { rEnrollment.delete(UUID.fromString(id)); }

    public double getAverageStudentsPerCourse() {
        return rEnrollment.averageStudentsPerCourse();
    }
}
