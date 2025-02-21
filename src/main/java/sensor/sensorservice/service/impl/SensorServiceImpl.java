package sensor.sensorservice.service.impl;

import org.springframework.stereotype.Service;
import sensor.sensorservice.service.interfaces.SensorService;

@Service
public class SensorServiceImpl implements SensorService {
    @Override
    public double measureTemperature() {
        double temperature = Math.random() * 100;
        return Math.round(temperature * 100) / 100.0;
    }
}
