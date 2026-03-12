package digital.zil.hl.module1.controller;

import digital.zil.hl.module1.model.Enrollment;
import digital.zil.hl.module1.service.SEnrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
public class CEnrollment {

    private final SEnrollment sEnrollment;

    @Autowired
    public CEnrollment(SEnrollment sEnrollment) {
        this.sEnrollment = sEnrollment;
    }

    @GetMapping
    public List<Enrollment> getAll() { return sEnrollment.getAll(); }

    @GetMapping("/{id}")
    public Enrollment getById(@PathVariable String id) { return sEnrollment.getById(id); }

    // body: { "studentId": "uuid", "courseId": "uuid" }
    @PostMapping("/")
    public Enrollment enroll(@RequestBody Map<String, String> body) {
        return sEnrollment.enroll(
                UUID.fromString(body.get("studentId")),
                UUID.fromString(body.get("courseId"))
        );
    }

    @PatchMapping("/{id}/complete")
    public Enrollment complete(@PathVariable String id) {
        return sEnrollment.complete(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { sEnrollment.delete(id); }

    // Статистика: среднее количество студентов на курсе за всё время
    @GetMapping("/stats/average")
    public Map<String, Double> averageStudentsPerCourse() {
        return Map.of("averageStudentsPerCourse", sEnrollment.getAverageStudentsPerCourse());
    }
}
