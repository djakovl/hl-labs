package digital.zil.hl.module1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crash")
public class CrashController {

    @PostMapping
    public ResponseEntity<Void> crash() {
        new Thread(() -> {
            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
            System.exit(1);
        }).start();
        return ResponseEntity.ok().build();
    }
}