package sensor.sensorservice.model;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorData {
    private String type;
    private double value;
    private String unit;
    private Instant timestamp;
}
