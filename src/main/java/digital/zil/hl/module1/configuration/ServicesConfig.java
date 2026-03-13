package digital.zil.hl.module1.configuration;

import digital.zil.hl.module1.model.Course;
import digital.zil.hl.module1.model.Student;
import digital.zil.hl.module1.repository.RCourse;
import digital.zil.hl.module1.repository.REnrollment;
import digital.zil.hl.module1.repository.RStudent;
import digital.zil.hl.module1.service.SCourse;
import digital.zil.hl.module1.service.SEnrollment;
import digital.zil.hl.module1.service.SStudent;
import digital.zil.hl.module1.service.SStatistics;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ServicesConfig {

    @Bean
    SStudent sStudent(RStudent rStudent) {
        SStudent service = new SStudent(rStudent);
        rStudent.save(new Student(UUID.randomUUID(), "Иванов Иван Иванович", "СТ-001", 2022));
        rStudent.save(new Student(UUID.randomUUID(), "Петрова Анна Сергеевна", "СТ-002", 2023));
        return service;
    }

    @Bean
    SCourse sCourse(RCourse rCourse) {
        SCourse service = new SCourse(rCourse);
        rCourse.save(new Course(UUID.randomUUID(), "CS101", "Основы программирования", "Смирнов А.В.", 4));
        rCourse.save(new Course(UUID.randomUUID(), "MATH201", "Высшая математика", "Козлов Д.П.", 5));
        return service;
    }

    @Bean
    SEnrollment sEnrollment(REnrollment rEnrollment) {
        return new SEnrollment(rEnrollment);
    }

    @Bean
    @ConditionalOnProperty(prefix = "statistics", name = "service", havingValue = "console2000")
    SStatistics sStatistics2000(SStudent sStudent) {
        return new SStatistics(2000, sStudent);
    }

    @Bean
    @ConditionalOnProperty(prefix = "statistics", name = "service", havingValue = "console1000")
    SStatistics sStatistics1000(SStudent sStudent) {
        return new SStatistics(1000, sStudent);
    }

}
