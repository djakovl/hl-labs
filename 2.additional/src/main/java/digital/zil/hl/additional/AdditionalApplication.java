package digital.zil.hl.additional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class AdditionalApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdditionalApplication.class, args);
    }
}