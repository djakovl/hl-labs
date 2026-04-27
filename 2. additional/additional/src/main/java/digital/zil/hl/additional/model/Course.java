package digital.zil.hl.additional.model;

import java.util.UUID;

public class Course {
    private UUID id;
    private String code;
    private String name;
    private String teacher;
    private int credits;
    private Integer year;
    private boolean deleted;

    public Course() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
