package digital.zil.hl.module1.model;

import java.time.LocalDate;
import java.util.UUID;

public class Enrollment {

    private UUID id;
    private UUID studentId;
    private UUID courseId;
    private LocalDate enrollmentDate;
    private String status;

    public Enrollment() {}

    public Enrollment(UUID id, UUID studentId, UUID courseId, LocalDate enrollmentDate, String status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }

    public UUID getCourseId() { return courseId; }
    public void setCourseId(UUID courseId) { this.courseId = courseId; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Enrollment{id=" + id + ", studentId=" + studentId + ", courseId=" + courseId
                + ", date=" + enrollmentDate + ", status='" + status + "'}";
    }
}
