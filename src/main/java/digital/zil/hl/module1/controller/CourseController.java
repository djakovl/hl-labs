package digital.zil.hl.module1.controller;

import digital.zil.hl.module1.model.Course;
import digital.zil.hl.module1.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAll() { return courseService.getAll(); }

    @GetMapping("/{id}")
    public Course getById(@PathVariable String id) { return courseService.getById(id); }

    @PostMapping("/")
    public Course save(@RequestBody Course course) { return courseService.save(course); }

    @PutMapping("/{id}")
    public Course update(@PathVariable String id, @RequestBody Course course) {
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { courseService.delete(id); }
}
