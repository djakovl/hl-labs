package digital.zil.hl.module1.model;

import java.util.UUID;

public class Student {

    private UUID id;
    private String fio;
    private String studentCard;
    private int enrollmentYear;
    private boolean deleted = false;


    public Student() {}

    public Student(UUID id, String fio, String studentCard, int enrollmentYear) {
        this.id = id;
        this.fio = fio;
        this.studentCard = studentCard;
        this.enrollmentYear = enrollmentYear;
    }

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

    @Override
    public String toString() {
        return "Student{id=" + id + ", fio='" + fio + "', studentCard='" + studentCard + "', enrollmentYear=" + enrollmentYear + '}';
    }
}
