package sensor.sensorservice.service.impl;

import org.springframework.stereotype.Service;
import sensor.sensorservice.service.interfaces.SensorService;

@Service
public class SensorServiceImpl implements SensorService {
    @Override
    public double measureTemperature() {
        double temperature = Math.random() * 50;
        return Math.round(temperature * 100) / 100.0;
    }

    @Override
    public double measureCO2() {
        double co2Level = Math.random() * 1000;
        return Math.round(co2Level * 1000) / 1000.0;
    }
}
