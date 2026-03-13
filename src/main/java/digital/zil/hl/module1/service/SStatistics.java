package digital.zil.hl.module1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class SStatistics {


    @Value("${statisticsservice.infostring:lines}")
    private String infoString;

    final int delay;

    private final SStudent sStudent;

    public SStatistics(int delay, SStudent sStudent) {
        this.delay = delay;
        this.sStudent = sStudent;
    }

    @Async(value = "applicationTaskExecutor")
    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + " - Fixed rate task async - "+ delay + " - " + infoString + " - "
                        + sStudent.getAll().size());
        Thread.sleep(delay);
    }
}
