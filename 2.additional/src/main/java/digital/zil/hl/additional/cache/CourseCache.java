package digital.zil.hl.additional.cache;

import digital.zil.hl.additional.client.CourseClient;
import digital.zil.hl.additional.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CourseCache {

    private static final Logger log = LoggerFactory.getLogger(CourseCache.class);

    private final CourseClient courseClient;
    private final Map<UUID, Course> cache;

    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);

    public CourseCache(CourseClient courseClient,
                       @Value("${cache.course.max-size:50}") int maxSize) {
        this.courseClient = courseClient;
        this.cache = Collections.synchronizedMap(
            new LinkedHashMap<>(maxSize, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<UUID, Course> eldest) {
                    return size() > maxSize;
                }
            }
        );
    }

    public Course get(UUID id) {
        synchronized (cache) {
            if (cache.containsKey(id)) {
                hits.incrementAndGet();
                return cache.get(id);
            }
        }

        Course course = courseClient.getById(id);
        if (course != null) {
            synchronized (cache) {
                cache.put(id, course);
            }
            misses.incrementAndGet();
        }
        return course;
    }

    public void invalidate() {
        synchronized (cache) {
            cache.clear();
        }
        hits.set(0);
        misses.set(0);
    }

    @Scheduled(fixedRateString = "${cache.stats.interval.ms:30000}")
    public void printStats() {
        long h = hits.get();
        long m = misses.get();
        long total = h + m;
        double hitRatio = total == 0 ? 0.0 : (double) h / total * 100;
        synchronized (cache) {
            log.info("[CourseCache] size={} hits={} misses={} hit-ratio={}%",
                cache.size(), h, m, String.format("%.1f", hitRatio));
        }
    }
}