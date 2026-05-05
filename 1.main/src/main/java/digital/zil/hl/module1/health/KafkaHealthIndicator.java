package digital.zil.hl.module1.health;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("kafka")
public class KafkaHealthIndicator extends AbstractHealthIndicator {

    private final AdminClient adminClient;

    @Value("${kafka.health.timeout-seconds:5}")
    private long timeoutSeconds;

    public KafkaHealthIndicator(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        var nodes = adminClient.describeCluster()
                .nodes()
                .get(timeoutSeconds, TimeUnit.SECONDS);

        if (nodes.isEmpty()) {
            builder.down().withDetail("error", "No brokers available");
        } else {
            builder.up().withDetail("brokers", nodes.size());
        }
    }
}