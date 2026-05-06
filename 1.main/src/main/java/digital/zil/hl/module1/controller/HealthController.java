package digital.zil.hl.module1.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final AdminClient adminClient;
    private final DataSource dataSource;

    public HealthController(AdminClient adminClient, DataSource dataSource) {
        this.adminClient = adminClient;
        this.dataSource = dataSource;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> body = new LinkedHashMap<>();
        boolean healthy = true;

        // Kafka
        try {
            var nodes = adminClient.describeCluster()
                    .nodes()
                    .get(5, TimeUnit.SECONDS);
            if (nodes.isEmpty()) {
                body.put("kafka", Map.of("status", "DOWN", "reason", "no brokers"));
                healthy = false;
            } else {
                body.put("kafka", Map.of("status", "UP", "brokers", nodes.size()));
            }
        } catch (Exception e) {
            body.put("kafka", Map.of("status", "DOWN", "error", e.getMessage()));
            healthy = false;
        }

        // DB
        try (Connection conn = dataSource.getConnection()) {
            boolean valid = conn.isValid(3);
            if (valid) {
                body.put("db", Map.of("status", "UP"));
            } else {
                body.put("db", Map.of("status", "DOWN", "reason", "connection invalid"));
                healthy = false;
            }
        } catch (Exception e) {
            body.put("db", Map.of("status", "DOWN", "error", e.getMessage()));
            healthy = false;
        }

        body.put("status", healthy ? "UP" : "DOWN");

        return healthy
                ? ResponseEntity.ok(body)
                : ResponseEntity.status(503).body(body);
    }
}