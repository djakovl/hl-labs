package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.Enrollment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.List;

@Component
public class EnrollmentClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public EnrollmentClient(RestTemplate restTemplate,
                            @Value("${main-service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }
    
    
    @CircuitBreaker(name = "mainService")
    public List<Enrollment> getAll() {
        return restTemplate.exchange(
            baseUrl + "/enrollments",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Enrollment>>() {}
        ).getBody();
    }
}
