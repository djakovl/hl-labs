package digital.zil.hl.module1.controller;

import digital.zil.hl.module1.model.Student;
import digital.zil.hl.module1.service.SStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class CStudent {

    private final SStudent sStudent;

    @Autowired
    public CStudent(SStudent sStudent) {
        this.sStudent = sStudent;
    }

    @GetMapping
    public List<Student> getAll() { return sStudent.getAll(); }

    @GetMapping("/{id}")
    public Student getById(@PathVariable String id) { return sStudent.getById(id); }

    @PostMapping("/")
    public Student save(@RequestBody Student student) { return sStudent.save(student); }

    @PutMapping("/{id}")
    public Student update(@PathVariable String id, @RequestBody Student student) {
        return sStudent.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { sStudent.delete(id); }
}
