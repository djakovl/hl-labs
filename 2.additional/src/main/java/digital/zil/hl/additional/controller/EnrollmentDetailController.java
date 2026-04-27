package digital.zil.hl.additional.controller;

import digital.zil.hl.additional.model.EnrollmentDetail;
import digital.zil.hl.additional.service.EnrollmentDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/additional")
public class EnrollmentDetailController {

    private final EnrollmentDetailService service;

    public EnrollmentDetailController(EnrollmentDetailService service) {
        this.service = service;
    }

    @GetMapping("/enrollments")
    public List<EnrollmentDetail> getEnrollmentsWithDetails() {
        return service.getEnrollmentsWithDetails();
    }
}
