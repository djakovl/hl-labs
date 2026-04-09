package digital.zil.hl.module1.controller;

import digital.zil.hl.module1.model.Enrollment;
import digital.zil.hl.module1.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<Enrollment> getAll() { return enrollmentService.getAll(); }

    @GetMapping("/{id}")
    public Enrollment getById(@PathVariable String id) { return enrollmentService.getById(id); }

    @PostMapping("/")
    public Enrollment enroll(@RequestBody Map<String, String> body) {
        return enrollmentService.enroll(
                UUID.fromString(body.get("studentId")),
                UUID.fromString(body.get("courseId"))
        );
    }

    @PatchMapping("/{id}/complete")
    public Enrollment complete(@PathVariable String id) {
        return enrollmentService.complete(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { enrollmentService.delete(id); }

    @GetMapping("/stats/average")
    public double statsAverage() {
        return enrollmentService.getAverageStudentsPerCourse();
    }
}
