package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class StudentClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public StudentClient(RestTemplate restTemplate,
                         @Value("${main-service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Student> getAll() {
        return restTemplate.exchange(
            baseUrl + "/students",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Student>>() {}
        ).getBody();
    }
}
