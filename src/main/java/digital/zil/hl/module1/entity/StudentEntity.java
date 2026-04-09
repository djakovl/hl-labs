package digital.zil.hl.module1.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "t_student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fio;
    @Column(name = "student_card")
    private String studentCard;
    @Column(name = "enrollment_year")
    private int enrollmentYear;
    private boolean deleted = false;

    public StudentEntity() {}
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; }
    public String getStudentCard() { return studentCard; }
    public void setStudentCard(String sc) { this.studentCard = sc; }
    public int getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(int y) { this.enrollmentYear = y; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}