package digital.zil.hl.additional.service;

import digital.zil.hl.additional.client.CourseClient;
import digital.zil.hl.additional.client.EnrollmentClient;
import digital.zil.hl.additional.client.StudentClient;
import digital.zil.hl.additional.model.Course;
import digital.zil.hl.additional.model.Enrollment;
import digital.zil.hl.additional.model.EnrollmentDetail;
import digital.zil.hl.additional.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnrollmentDetailService {

    private final EnrollmentClient enrollmentClient;
    private final CourseClient courseClient;

    public EnrollmentDetailService(EnrollmentClient enrollmentClient,
                                   CourseClient courseClient) {
        this.enrollmentClient = enrollmentClient;
        this.courseClient = courseClient;
    }

    public Map<String, Double> averageStudentsPerCourse() {
        /*List<Enrollment> enrollments = enrollmentClient.getAll();
        List<Course> courses = courseClient.getAll();


        Map<UUID, Long> countPerCourse = enrollments.stream()
            .collect(Collectors.groupingBy(Enrollment::getCourseId, Collectors.counting()));

        return courses.stream()
            .filter(c -> countPerCourse.containsKey(c.getId()))
            .collect(Collectors.groupingBy(
                Course::getCode,
                Collectors.averagingLong(c -> countPerCourse.getOrDefault(c.getId(), 0L))
            ));*/
        // гадость 2
        List<Enrollment> enrollments = enrollmentClient.getAll();

        // Сначала считаем количество по уникальным courseId
        Map<UUID, Long> countPerCourse = enrollments.stream()
            .collect(Collectors.groupingBy(Enrollment::getCourseId, Collectors.counting()));

        // Запросы только по уникальным ID — вместо N запросов делаем M (M <= N)
        List<Course> courses = countPerCourse.keySet().stream()
            .map(courseClient::getById)
            .filter(c -> c != null)
            .collect(Collectors.toList());

        return courses.stream()
            .collect(Collectors.groupingBy(
                Course::getCode,
                Collectors.averagingLong(c -> countPerCourse.getOrDefault(c.getId(), 0L))
            ));
    }
}
