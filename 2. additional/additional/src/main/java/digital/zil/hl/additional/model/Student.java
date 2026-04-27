package digital.zil.hl.additional.model;

import java.util.UUID;

public class Student {
    private UUID id;
    private String fio;
    private String studentCard;
    private int enrollmentYear;
    private boolean deleted;

    public Student() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; }
    public String getStudentCard() { return studentCard; }
    public void setStudentCard(String studentCard) { this.studentCard = studentCard; }
    public int getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(int enrollmentYear) { this.enrollmentYear = enrollmentYear; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
