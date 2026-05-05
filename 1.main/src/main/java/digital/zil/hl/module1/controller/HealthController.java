package digital.zil.hl.module1.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final AdminClient adminClient;

    public HealthController(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        try {
            var nodes = adminClient.describeCluster()
                    .nodes()
                    .get(5, TimeUnit.SECONDS);

            if (nodes.isEmpty()) {
                return ResponseEntity.status(503)
                        .body(Map.of("status", "DOWN", "kafka", "no brokers"));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "UP",
                    "kafka", Map.of("brokers", nodes.size())
            ));
        } catch (Exception e) {
            return ResponseEntity.status(503)
                    .body(Map.of("status", "DOWN", "error", e.getMessage()));
        }
    }
}