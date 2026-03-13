package digital.zil.hl.module1.configuration;

import digital.zil.hl.module1.model.Course;
import digital.zil.hl.module1.model.Student;
import digital.zil.hl.module1.repository.CourseRepository;
import digital.zil.hl.module1.repository.EnrollmentRepository;
import digital.zil.hl.module1.repository.StudentRepository;
import digital.zil.hl.module1.service.CourseService;
import digital.zil.hl.module1.service.EnrollmentService;
import digital.zil.hl.module1.service.StudentService;
import digital.zil.hl.module1.service.StatisticsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ServicesConfig {

    @Bean
    StudentService sStudent(StudentRepository studentRepository) {
        StudentService service = new StudentService(studentRepository);
        studentRepository.save(new Student(UUID.randomUUID(), "Иванов Иван Иванович", "СТ-001", 2022));
        studentRepository.save(new Student(UUID.randomUUID(), "Петрова Анна Сергеевна", "СТ-002", 2023));
        return service;
    }

    @Bean
    CourseService sCourse(CourseRepository courseRepository) {
        CourseService service = new CourseService(courseRepository);
        courseRepository.save(new Course(UUID.randomUUID(), "CS101", "Основы программирования", "Смирнов А.В.", 4));
        courseRepository.save(new Course(UUID.randomUUID(), "MATH201", "Высшая математика", "Козлов Д.П.", 5));
        return service;
    }

    @Bean
    @ConditionalOnProperty(prefix = "statistics", name = "service", havingValue = "console2000")
    StatisticsService sStatistics2000(StudentService studentService) {
        return new StatisticsService(2000, studentService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "statistics", name = "service", havingValue = "console1000")
    StatisticsService sStatistics1000(StudentService studentService) {
        return new StatisticsService(1000, studentService);
    }

    @Bean
    EnrollmentService enrollmentService(EnrollmentRepository enrollmentRepository,
                                        CourseRepository courseRepository) {
        return new EnrollmentService(enrollmentRepository, courseRepository);
    }


}
