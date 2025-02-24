package sensor.sensorservice.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sensor.sensorservice.handler.SensorWebSocketHandler;

@Component
public class SensorScheduler {
    @Autowired
    private SensorWebSocketHandler sensorWebSocketHandler;

    @Scheduled(fixedRate = 5000)
    public void schedule() throws Exception {
        sensorWebSocketHandler.sendData();
    }
}
