package digital.zil.hl.additional.model;

import java.time.LocalDate;
import java.util.UUID;

public class EnrollmentDetail {
    private UUID enrollmentId;
    private String status;
    private LocalDate enrollmentDate;
    private String studentFio;
    private String studentCard;
    private String courseName;
    private String courseCode;
    private String teacher;

    public EnrollmentDetail() {}

    public EnrollmentDetail(Enrollment e, Student s, Course c) {
        this.enrollmentId = e.getId();
        this.status = e.getStatus();
        this.enrollmentDate = e.getEnrollmentDate();
        this.studentFio = s != null ? s.getFio() : "unknown";
        this.studentCard = s != null ? s.getStudentCard() : "unknown";
        this.courseName = c != null ? c.getName() : "unknown";
        this.courseCode = c != null ? c.getCode() : "unknown";
        this.teacher = c != null ? c.getTeacher() : "unknown";
    }

    public UUID getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(UUID enrollmentId) { this.enrollmentId = enrollmentId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public String getStudentFio() { return studentFio; }
    public void setStudentFio(String studentFio) { this.studentFio = studentFio; }
    public String getStudentCard() { return studentCard; }
    public void setStudentCard(String studentCard) { this.studentCard = studentCard; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
}