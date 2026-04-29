package digital.zil.hl.module1.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.zil.hl.module1.model.Course;
import digital.zil.hl.module1.model.Student;
import digital.zil.hl.module1.service.CourseService;
import digital.zil.hl.module1.service.EnrollmentService;
import digital.zil.hl.module1.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(StudentService studentService,
                         CourseService courseService,
                         EnrollmentService enrollmentService,
                         ObjectMapper objectMapper) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
        topics = "${kafka.topic.name}",
        groupId = "${spring.kafka.consumer.group-id}",
        concurrency = "${kafka.consumer.concurrency}"
    )
    public void consume(String rawMessage) {
        log.info("Kafka raw message received: {}", rawMessage);
        try {
            KafkaMessage msg = objectMapper.readValue(rawMessage, KafkaMessage.class);
            log.info("Kafka parsed: {}", msg);
            route(msg);
        } catch (Exception e) {
            log.error("Failed to process kafka message: {}", rawMessage, e);
        }
    }

    private void route(KafkaMessage msg) throws Exception {
        switch (msg.getEntity()) {
            case "STUDENT"    -> handleStudent(msg);
            case "COURSE"     -> handleCourse(msg);
            case "ENROLLMENT" -> handleEnrollment(msg);
            default           -> log.warn("Unknown entity: {}", msg.getEntity());
        }
    }

    private void handleStudent(KafkaMessage msg) throws Exception {
        switch (msg.getOperation()) {
            case "POST" -> {
                Student s = objectMapper.readValue(msg.getPayload(), Student.class);
                studentService.save(s);
                log.info("STUDENT POST processed: {}", s.getId());
            }
            case "PUT" -> {
                Student s = objectMapper.readValue(msg.getPayload(), Student.class);
                studentService.update(s.getId().toString(), s);
                log.info("STUDENT PUT processed: {}", s.getId());
            }
            case "DEL" -> {
                studentService.delete(msg.getPayload().trim());
                log.info("STUDENT DEL processed: {}", msg.getPayload());
            }
            default -> log.warn("Unknown operation for STUDENT: {}", msg.getOperation());
        }
    }

    private void handleCourse(KafkaMessage msg) throws Exception {
        switch (msg.getOperation()) {
            case "POST" -> {
                Course c = objectMapper.readValue(msg.getPayload(), Course.class);
                courseService.save(c);
                log.info("COURSE POST processed: {}", c.getId());
            }
            case "PUT" -> {
                Course c = objectMapper.readValue(msg.getPayload(), Course.class);
                courseService.update(c.getId().toString(), c);
                log.info("COURSE PUT processed: {}", c.getId());
            }
            case "DEL" -> {
                courseService.delete(msg.getPayload().trim());
                log.info("COURSE DEL processed: {}", msg.getPayload());
            }
            default -> log.warn("Unknown operation for COURSE: {}", msg.getOperation());
        }
    }

    private void handleEnrollment(KafkaMessage msg) throws Exception {
        switch (msg.getOperation()) {
            case "POST" -> {
                var node = objectMapper.readTree(msg.getPayload());
                UUID studentId = UUID.fromString(node.get("studentId").asText());
                UUID courseId  = UUID.fromString(node.get("courseId").asText());
                enrollmentService.enroll(studentId, courseId);
                log.info("ENROLLMENT POST processed: student={} course={}", studentId, courseId);
            }
            case "DEL" -> {
                enrollmentService.delete(msg.getPayload().trim());
                log.info("ENROLLMENT DEL processed: {}", msg.getPayload());
            }
            default -> log.warn("Unknown operation for ENROLLMENT: {}", msg.getOperation());
        }
    }
}