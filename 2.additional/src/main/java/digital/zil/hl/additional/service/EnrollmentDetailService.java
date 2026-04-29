package digital.zil.hl.additional.service;

import digital.zil.hl.additional.cache.CourseCache;
import digital.zil.hl.additional.client.EnrollmentClient;
import digital.zil.hl.additional.model.Course;
import digital.zil.hl.additional.model.Enrollment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnrollmentDetailService {

    private final EnrollmentClient enrollmentClient;
    private final CourseCache courseCache;

    public EnrollmentDetailService(EnrollmentClient enrollmentClient,
                                   CourseCache courseCache) {
        this.enrollmentClient = enrollmentClient;
        this.courseCache = courseCache;
    }

    public Map<String, Double> averageStudentsPerCourse() {
        List<Enrollment> enrollments = enrollmentClient.getAll();

        Map<UUID, Long> countPerCourse = enrollments.stream()
            .collect(Collectors.groupingBy(Enrollment::getCourseId, Collectors.counting()));
        List<Course> courses = countPerCourse.keySet().stream()
            .map(courseCache::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return courses.stream()
            .collect(Collectors.groupingBy(
                Course::getCode,
                Collectors.averagingLong(c -> countPerCourse.getOrDefault(c.getId(), 0L))
            ));
    }
}
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