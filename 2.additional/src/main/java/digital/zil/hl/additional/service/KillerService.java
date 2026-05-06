package digital.zil.hl.additional.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KillerService {

    private static final Logger log = LoggerFactory.getLogger(KillerService.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public KillerService(RestTemplate restTemplate,
                         @Value("${main-service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Scheduled(fixedDelay = 50_000)
    public void kill() {
        try {
            restTemplate.postForEntity(baseUrl + "/crash", null, Void.class);
            log.info("[Killer] Sent crash signal to {}", baseUrl);
        } catch (Exception e) {
            log.warn("[Killer] Crash request failed (service may be restarting): {}", e.getMessage());
        }
    }
}