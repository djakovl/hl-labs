package digital.zil.hl.module1.model;

import java.util.UUID;

public class Course {

    private UUID id;
    private String code;
    private String name;
    private String teacher;
    private int credits;
    private boolean deleted = false;
    private Integer year;

    public Course() {}

    public Course(UUID id, String code, String name, String teacher, int credits) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.teacher = teacher;
        this.credits = credits;
    }

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

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    @Override
    public String toString() {
        return "Course{id=" + id + ", code='" + code + "', name='" + name + "', teacher='" + teacher + "', credits=" + credits + '}';
    }
}
