package sensor.sensorservice.dto;

public record SensorDataDTO(
        String type,
        double value,
        String timestamp
) {
}
