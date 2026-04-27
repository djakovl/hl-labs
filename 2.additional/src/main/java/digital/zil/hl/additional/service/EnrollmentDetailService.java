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
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    public EnrollmentDetailService(EnrollmentClient enrollmentClient,
                                   StudentClient studentClient,
                                   CourseClient courseClient) {
        this.enrollmentClient = enrollmentClient;
        this.studentClient = studentClient;
        this.courseClient = courseClient;
    }

    // JOIN на стороне Java — студент + курс + запись
    public List<EnrollmentDetail> getEnrollmentsWithDetails() {
        List<Enrollment> enrollments = enrollmentClient.getAll();
        
        Map<UUID, Student> studentMap = studentClient.getAll()
            .stream()
            .collect(Collectors.toMap(Student::getId, s -> s));

        Map<UUID, Course> courseMap = courseClient.getAll()
            .stream()
            .collect(Collectors.toMap(Course::getId, c -> c));

        return enrollments.stream()
            .filter(e -> !e.isDeleted())
            .map(e -> new EnrollmentDetail(
                e,
                studentMap.get(e.getStudentId()),
                courseMap.get(e.getCourseId())
            ))
            .collect(Collectors.toList());
    }
}
