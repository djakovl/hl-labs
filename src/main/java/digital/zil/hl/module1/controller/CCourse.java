package digital.zil.hl.module1.controller;

import digital.zil.hl.module1.model.Course;
import digital.zil.hl.module1.service.SCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CCourse {

    private final SCourse sCourse;

    @Autowired
    public CCourse(SCourse sCourse) {
        this.sCourse = sCourse;
    }

    @GetMapping
    public List<Course> getAll() { return sCourse.getAll(); }

    @GetMapping("/{id}")
    public Course getById(@PathVariable String id) { return sCourse.getById(id); }

    @PostMapping("/")
    public Course save(@RequestBody Course course) { return sCourse.save(course); }

    @PutMapping("/{id}")
    public Course update(@PathVariable String id, @RequestBody Course course) {
        return sCourse.update(id, course);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { sCourse.delete(id); }
}
