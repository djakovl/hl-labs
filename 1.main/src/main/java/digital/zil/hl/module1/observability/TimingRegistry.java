package digital.zil.hl.main.observability;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;
@Component
public class TimingRegistry {

    public record TimingEvent(String operation, long durationMs, Instant timestamp) {}
    // Ограниченная очередь — не растёт бесконечно
    private final ConcurrentLinkedQueue<TimingEvent> events = new ConcurrentLinkedQueue<>();

    public void record(String operation, long startNano) {
        long durationMs = (System.nanoTime() - startNano) / 1_000_000;
        events.add(new TimingEvent(operation, durationMs, Instant.now()));
    }

    // Чистим старше maxWindow при каждой записи — или в Scheduled
    public List<TimingEvent> getWindow(int seconds) {
        Instant cutoff = Instant.now().minusSeconds(seconds);
        return events.stream()
            .filter(e -> e.timestamp().isAfter(cutoff))
            .toList();
    }
    public void evict(int maxSeconds) {
        Instant cutoff = Instant.now().minusSeconds(maxSeconds);
        events.removeIf(e -> e.timestamp().isBefore(cutoff));
    }
}

@Aspect
@Component
public class TimingAspect {

    private final TimingRegistry registry;

    public TimingAspect(TimingRegistry registry) {
        this.registry = registry;
    }

    // Покрывает все контроллеры и сервисы одной аннотацией
    @Around("within(@org.springframework.web.bind.annotation.RestController *)" +
            "|| within(@org.springframework.stereotype.Service *)")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            String op = pjp.getSignature().getDeclaringTypeName()
                       + "." + pjp.getSignature().getName();
            registry.record(op, start);
        }
    }
}