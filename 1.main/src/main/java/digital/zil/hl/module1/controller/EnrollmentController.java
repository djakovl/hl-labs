package digital.zil.hl.module1.controller;

import digital.zil.hl.module1.model.Enrollment;
import digital.zil.hl.module1.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Value("${additional-service.url}")
    private String additionalUrl;

    @Autowired
    private RestTemplate restTemplate;

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
    public Map<String, Double> statsAverage() {
        return restTemplate.exchange(
            additionalUrl + "/additional/stats/average",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Double>>() {}
        ).getBody();
    }

    @DeleteMapping("/clear")
    @Transactional
    public void clear() { enrollmentService.clear(); }
}