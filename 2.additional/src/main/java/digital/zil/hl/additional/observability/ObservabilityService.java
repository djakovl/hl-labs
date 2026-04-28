package digital.zil.hl.additional.observability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Scheduled(fixedRateString = "${observability.schedule.ms:10000}")
public void reportStats() {
    // Сначала чистим всё старше максимального окна
    registry.evict(longWindowSec);

    Instant now = Instant.now();
    log.info("=== Observability Report @ {} ===", now);

    for (int window : List.of(shortWindowSec, mediumWindowSec, longWindowSec)) {
        var windowEvents = registry.getWindow(window);
        var grouped = windowEvents.stream()
            .collect(Collectors.groupingBy(
                TimingRegistry.TimingEvent::operation,
                Collectors.summarizingLong(TimingRegistry.TimingEvent::durationMs)
            ));
        log.info("  [{}s] total={}", window, windowEvents.size());
        grouped.forEach((op, s) ->
            log.info("    {} -> count={} avg={}ms min={}ms max={}ms",
                op, s.getCount(), (long)s.getAverage(), s.getMin(), s.getMax()));
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