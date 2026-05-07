package digital.zil.hl.additional.cache;

import digital.zil.hl.additional.client.CourseClient;
import digital.zil.hl.additional.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CourseCache {

    private static final Logger log = LoggerFactory.getLogger(CourseCache.class);
    private static final String PREFIX = "course:";

    private final CourseClient courseClient;
    private final RedisTemplate<String, Object> redisTemplate;

    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);

    public CourseCache(CourseClient courseClient,
                       RedisTemplate<String, Object> redisTemplate) {
        this.courseClient = courseClient;
        this.redisTemplate = redisTemplate;
    }

    public Course get(UUID id) {
        String key = PREFIX + id;
        Course cached = (Course) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            hits.incrementAndGet();
            return cached;
        }

        Course course = courseClient.getById(id);
        if (course != null) {
            redisTemplate.opsForValue().set(key, course, Duration.ofMinutes(10));
            misses.incrementAndGet();
        }
        return course;
    }

    public void invalidate() {
        var keys = redisTemplate.keys(PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        hits.set(0);
        misses.set(0);
    }

    @Scheduled(fixedRateString = "${cache.stats.interval.ms:30000}")
    public void printStats() {
        long h = hits.get(), m = misses.get(), total = h + m;
        double ratio = total == 0 ? 0.0 : (double) h / total * 100;
        log.info("[CourseCache] hits={} misses={} hit-ratio={}%", h, m, String.format("%.1f", ratio));
    }
}