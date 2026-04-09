package digital.zil.hl.module1.controller;

import digital.zil.hl.module1.model.Student;
import digital.zil.hl.module1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAll() { return studentService.getAll(); }

    @GetMapping("/{id}")
    public Student getById(@PathVariable String id) { return studentService.getById(id); }

    @PostMapping("/")
    public Student save(@RequestBody Student student) { return studentService.save(student); }

    @PutMapping("/{id}")
    public Student update(@PathVariable String id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { studentService.delete(id); }
}
