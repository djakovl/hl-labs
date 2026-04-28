package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.Course;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CourseClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CourseClient(RestTemplate restTemplate,
                        @Value("${main-service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Course> getAll() {
        return restTemplate.exchange(
            baseUrl + "/courses",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Course>>() {}
        ).getBody();
    }
}
